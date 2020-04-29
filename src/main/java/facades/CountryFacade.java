package facades;

import dtos.CountryBasicInDTO;
import dtos.CountryInDTO;
import entities.Country;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Brandstrup
 */
public class CountryFacade
{

    private static CountryFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CountryFacade()
    {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static CountryFacade getCountryFacade(EntityManagerFactory _emf)
    {
        if (instance == null)
        {
            emf = _emf;
            instance = new CountryFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    /**
     * Counts the amount of entries existing in the database.
     *
     * @return The amount of existing entries in the database.
     */
    public long getInCountryCount()
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            long countryCount = (long) em.createQuery("SELECT COUNT(o) FROM Country o").getSingleResult();
            return countryCount;
        }
        finally
        {
            em.close();
        }

    }

    /**
     * Retrieves all entries from the database as DTO objects.
     *
     * @return a List of DTO objects.
     */
    public List<CountryBasicInDTO> getAllInCountries() throws NotFoundException
    {
        EntityManager em = getEntityManager();
        try
        {
            List<CountryBasicInDTO> countryBasicDTOList = new ArrayList<>();
            TypedQuery<Country> query
                    = em.createQuery("SELECT o FROM Country o", Country.class);

            if (query.getResultList().isEmpty() || query.getResultList() == null)
            {
                throw new NotFoundException("No objects retreived from database.");
            }
            
            for (Country o : query.getResultList())
            {
                countryBasicDTOList.add(new CountryBasicInDTO(o));
            }

            return countryBasicDTOList;
        }
        finally
        {
            em.close();
        }
    }

    public CountryInDTO getInCountryById(int id) throws NotFoundException
    {
        EntityManager em = getEntityManager();
        try
        {
            Country o = em.find(Country.class, id);
            if (o == null)
            {
                throw new NotFoundException("No object matching provided id exists in database.");
            }
            return new CountryInDTO(o);
        }
        catch (IllegalArgumentException ex)
        {
            throw new NotFoundException("No object matching provided id exists in database. IllegalArgumentException.");
        }
        finally
        {
            em.close();
        }
    }

}
