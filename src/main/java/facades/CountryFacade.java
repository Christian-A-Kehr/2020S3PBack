package facades;

import dtos.CountryBasicInDTO;
import dtos.CountryExDTO;
import dtos.CountryInDTO;
import dtos.CovidExDTO;
import entities.CountryData;
import entities.CovidData;
import errorhandling.DatabaseException;
import errorhandling.NotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityExistsException;
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
     * getInternalCountryCount
     *
     * getInternalCountryByCode
     *
     * getAllInternalCountries
     *
     * getNewestInternalCovidEntryForCountryByCode
     *
     * getAllInternalCovidEntriesForCountryByCode
     *
     * getInternalCovidEntryForCountryByCodeByDate
     *
     * persistAllExternalCountries
     *
     * persisteExternalCountry
     *
     * persistAllExternalCovidEntriesForCountryByCode
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
     * author Brandstrup
     *
     * @return The amount of existing entries in the database.
     */
    public long getInternalCountryCount()
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
     * Retrieves an entry from the database as a DTO object.
     *
     * author Christian
     *
     * @param code
     * @return
     * @throws NotFoundException
     */
    // Redundent! use in getNewestInternalCovidEntryForCountryByCode
    public CountryData getInternalCountryByCode(String code) throws NotFoundException
    {
        EntityManager em = emf.createEntityManager();
        if (code == null)
        {
            throw new NotFoundException("No objects passed");
        }
        try
        {
            CountryData country;
            TypedQuery<CountryData> query = em.createQuery("SELECT o FROM CountryData o "
                    + "WHERE o.countryCode = :code", CountryData.class)
                    .setParameter("code", code);
            country = query.getSingleResult();

            if (country == null)
            {
                throw new NotFoundException("No object matching provided id exists in database.");
            }

            return country;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * Retrieves all entries from the database as DTO objects.
     *
     * author Brandstrup
     *
     * @return a List of DTO objects.
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
        }
        finally
        {
            em.close();
        }
    }

    /**
     *
     * author Brandstrup
     *
     * @param code
     * @return
     * @throws NotFoundException
     */
    public CountryInDTO getNewestInternalCovidEntryForCountryByCode(String code) throws NotFoundException
    {
        EntityManager em = getEntityManager();
        try
        {
            CountryData country;
            TypedQuery<CountryData> query = em.createQuery("SELECT cou FROM Country cou "
                    + "WHERE cou.countrycode = :code", CountryData.class)
                    .setParameter("code", code);
            country = query.getSingleResult();

            if (country == null)
            {
                throw new NotFoundException("No object matching provided id exists in database.");
            }

            if (!(country.getCovidEntries().isEmpty()))
            {
                HashSet<CovidData> covidEntries = new HashSet<CovidData>(country.getCovidEntries());
                CovidData latestEntry = Collections.max(covidEntries, (CovidData o1, CovidData o2) ->
                {
                    return o1.getDate().compareTo(o2.getDate());
                });
                long newestCovidId = latestEntry.getId();
                // previous 6 lines really need testing!

                CovidData covid = em.find(CovidData.class, newestCovidId);

                return new CountryInDTO(country, covid);
            }

            return new CountryInDTO(country);
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

    /**
     * author Brandstrup
     *
     */
    public void getAllInternalCovidEntriesForCountryByCode()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * author Brandstrup
     *
     */
    public void getInternalCovidEntryForCountryByCodeByDate()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * author Christian
     *
     * @param DTOList
     * @return
     */
    public void persistAllExternalCountries(List<CountryExDTO> DTOList) throws NotFoundException
    {
        if (DTOList == null)
        {
            throw new NotFoundException("No objects retreived from http://restcountries.eu/rest/v1.");
        }

        if (DTOList.isEmpty())
        {
            throw new NotFoundException("No data was fetched.");
        }

        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            for (CountryExDTO o : DTOList)
            {
                CountryData cd = new CountryData(o.getName(), o.getAlpha2Code(), o.getPopulation(), null, null);
                em.persist(cd);
            }
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    /**
     * author Christian
     *
     * @param dto
     * @return
     * @throws NotFoundException
     */
    public CountryData persisteExternalCountry(CountryExDTO dto) throws NotFoundException, DatabaseException
    {
        if (dto == null)
        {
            throw new NotFoundException("No objects passed");
        }
//        TBD
//        if (
//                getInternalCountryByCode(dto.getAlpha2Code())
//                {
//                }
        CountryData cd;
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            cd = new CountryData(dto.getName(), dto.getAlpha2Code(), dto.getPopulation(), null, null);
            em.persist(cd);
            em.getTransaction().commit();
            return cd;
        }
        catch (EntityExistsException ex)
        {
            throw new DatabaseException("An identical object entry already exists in the database.");
        }
        finally
        {
            em.close();
        }
        //http://restcountries.eu/rest/v1/alpha?codes=de
    }

    /**
     * author Brandstrup
     *
     * @param DTOList
     * @param code
     * @throws NotFoundException
     */
    public void persistAllExternalCovidEntriesForCountryByCode(List<CovidExDTO> DTOList, String code) throws NotFoundException
    {
        if (DTOList == null)
        {
            throw new NotFoundException("No objects retreived from https://api.covid19api.com/total/dayone/country/" + code + ".");
        }

        if (DTOList.isEmpty())
        {
            throw new NotFoundException("No data was fetched.");
        }

        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            for (CovidExDTO o : DTOList)
            {

                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
                LocalDate localDate = LocalDate.parse(o.getDate(), inputFormatter);
                
                String formattedDate = outputFormatter.format(localDate);
                System.out.println(formattedDate);
                
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                //.plusSeconds(86400)
                long newConfirmed = 0;
                long newRecovered = 0;
                long newDeaths = 0;

                CovidData cd = new CovidData(date, null, newConfirmed, o.getConfirmed(),
                        newRecovered, o.getRecovered(), newDeaths, o.getDeaths());
                em.persist(cd);
            }
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
        //https://api.covid19api.com/total/dayone/country/de
    }

}
