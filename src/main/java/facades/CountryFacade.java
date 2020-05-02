package facades;

import dtos.CountryBasicInDTO;
import dtos.CountryExDataDTO;
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
 * @author Brandstrup & Christian
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
     * This facade contains the following methods in order:
     *
     * getInternalCountryCount getAllInternalCountries
     * getLatestInternalCovidEntryForCountryByCode
     * getAllInternalCovidEntriesForCountryByCode
     * getInternalCovidEntryForCountryByCodeByDate getAllExternalCountries
     * getExternalCountryByCode getAllExternalCovidEntriesForCountryByName
     */
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
     * @author Brandstrup
     */
    public long getInternalCountryCount()
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            long countryCount = (long) em.createQuery("SELECT COUNT(o) FROM CountryData o").getSingleResult();
            return countryCount;
        } finally
        {
            em.close();
        }

    }

    /**
     * Retrieves all entries from the database as DTO objects.
     *
     * @return a List of DTO objects.
     * @author Brandstrup
     */
    public List<CountryBasicInDTO> getAllInternalCountries() throws NotFoundException
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
        } finally
        {
            em.close();
        }
    }

    /**
     *
     * @param code
     * @return
     * @throws NotFoundException
     * @author Brandstrup
     */
    public CountryInDTO getLatestInternalCovidEntryForCountryByCode(String code) throws NotFoundException
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
        } catch (IllegalArgumentException ex)
        {
            throw new NotFoundException("No object matching provided id exists in database. IllegalArgumentException.");
        } finally
        {
            em.close();
        }
    }

    /**
     * @author Brandstrup
     */
    public void getAllInternalCovidEntriesForCountryByCode()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * @author Brandstrup
     */
    public void getInternalCovidEntryForCountryByCodeByDate()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * @author Christian
     * @param DTOList
     * @return
     */
    public String getAllExternalCountries(List<CountryExDataDTO> DTOList) throws NotFoundException
    {
        if (DTOList == null)
        {
            throw new NotFoundException("No objects retreived from database.");
        }

        if (DTOList.isEmpty())
        {
            throw new NotFoundException("Database is empty.");
        }

        EntityManager em = emf.createEntityManager();
        List<CountryExDataDTO> dtos = DTOList;
        try
        {
            em.getTransaction().begin();
            for (CountryExDataDTO i : dtos)
            {
                CountryData cd = new CountryData(i.getName(), i.getAlpha2Code(), i.getPopulation(), null, null);
                em.persist(cd);
            }
            em.getTransaction().commit();
            return "countries added";
        } catch (Exception ex)
        {
            return "Failed doing persist to DB";
        } finally
        {
            em.close();
        }
    }

    /**
     * @author Christian
     * @param dto
     * @return
     * @throws errorhandling.NotFoundException
     */
    // tidligere getExternalCountryByCode
    public CountryData persisteExCountry(CountryExDataDTO dto) throws NotFoundException
    {
        if (dto == null)
        {
            throw new NotFoundException("No objects passed");
        }
        CountryData cd;
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            cd = new CountryData(dto.getName(), dto.getAlpha2Code(), dto.getPopulation(), null, null);
            em.persist(cd);
            em.getTransaction().commit();
            return cd;
        } catch (Exception ex)
        {
            throw new NotFoundException("Failed to persist") ;
        } finally
        {
            em.close();
        }
        //http://restcountries.eu/rest/v1/alpha?codes=de
    }

    /**
     * @author Brandstrup
     */
    public void getAllExternalCovidEntriesForCountryByName()
    {
        //https://api.covid19api.com/total/dayone/country/germany
        throw new UnsupportedOperationException();
    }
    
    
    /**
     * author Christian
     * @param countrycode
     * @return
     * @throws NotFoundException 
     */
    // Redundent! use in getLatestInternalCovidEntryForCountryByCode
    public CountryData getCountryFromDatabaseByCountrycode(String countrycode) throws NotFoundException
    {
        EntityManager em = emf.createEntityManager();
        if (countrycode == null)
        {
            throw new NotFoundException("No objects passed");
        }
        try
        {
            TypedQuery<CountryData> query = em.createQuery("SELECT e From CountryData e where e.countryCode = :code", CountryData.class).setParameter("code", countrycode);
            return query.getSingleResult(); 
        } finally
        {
            em.close();
        }
    }

}
