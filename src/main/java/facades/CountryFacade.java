package facades;

import dtos.CountryBasicInDTO;
import dtos.CountryInDTO;
import entities.CountryData;
import entities.CovidData;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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
            long countryCount = (long) em.createQuery("SELECT COUNT(o) FROM CountryData o").getSingleResult();
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
            TypedQuery<CountryData> query
                    = em.createQuery("SELECT o FROM CountryData o", CountryData.class);

            if (query.getResultList() == null)
            {
                throw new NotFoundException("No objects retreived from database.");
            }

            if (query.getResultList().isEmpty())
            {
                throw new NotFoundException("Database is empty.");
            }

            query.getResultList().forEach((o) ->
            {
                countryBasicDTOList.add(new CountryBasicInDTO(o));
            });

            return countryBasicDTOList;
        }
        finally
        {
            em.close();
        }
    }

    /**
     *
     * @param code
     * @return
     * @throws NotFoundException
     */
    public CountryInDTO getLatestCovidEntryForCountryById(String code) throws NotFoundException
    {
        EntityManager em = getEntityManager();
        try
        {
            CountryData cou;
            TypedQuery<CountryData> query = em.createQuery("SELECT cou FROM Country cou "
                    + "WHERE cou.countrycode = :code", CountryData.class)
                    .setParameter("code", code);
            cou = query.getSingleResult();

            if (cou == null)
            {
                throw new NotFoundException("No object matching provided id exists in database.");
            }

            if (!(cou.getCovidEntries().isEmpty()))
            {
                HashSet<CovidData> covidEntries = new HashSet<CovidData>(cou.getCovidEntries());
                CovidData latestEntry = Collections.max(covidEntries, (CovidData o1, CovidData o2) ->
                {
                    return o1.getDate().compareTo(o2.getDate());
                });
                long newestCovidId = latestEntry.getId();
                // previous 6 lines really need testing!

                CovidData cov = em.find(CovidData.class, newestCovidId);

                return new CountryInDTO(cou, cov);
            }
            
            return new CountryInDTO(cou);
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
