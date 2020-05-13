package rest;

import facades.CountryFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dtos.CountryBasicInDTO;
import dtos.CountryExDTO;
import dtos.CountryInDTO;
import dtos.CovidExDTO;
import errorhandling.DatabaseException;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import utils.EMF_Creator;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utils.HttpUtils;
import utils.RequestSender;

@Path("country")
public class CountryResource
{

    // external API endpoint URLs. Missing country code (alpha2) for completion
    private static final String covidURL = "https://api.covid19api.com/dayone/country/";
    private static final String countryURL = "https://restcountries.eu/rest/v1/alpha?codes=";
    // gets all countries
    private static final String countriesURL = "https://restcountries.eu/rest/v1";

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/2020S3P_test",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);

    private static final CountryFacade FACADE = CountryFacade.getCountryFacade(EMF);
    private static final RequestSender requestSender = RequestSender.getRequestSender();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private List<CovidExDTO> fetchCovidDataFromExternal(String url, String code)
            throws SocketTimeoutException, ProtocolException, IOException, IllegalArgumentException
    {
        Gson gson = new Gson();
        HashMap headers = new HashMap();
        headers.put("Accept", "application/json;charset=UTF-8");
        String covidData = requestSender.sendRequest(url + code, "GET", headers, 5000);

        List<CovidExDTO> covidList = gson.fromJson(covidData, new TypeToken<List<CovidExDTO>>()
        {
        }.getType());

        FACADE.persistAllExternalCovidEntriesForCountryByCode(covidList, code);
        return covidList;
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountryCount()
    {
        long count = FACADE.getInternalCountryCount();
        return "{\"count\": " + count + "}";  //Done manually so no need for a DTO
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllInternalBasicCountries()
    {
        List<CountryBasicInDTO> cBasicDTOList;
        try
        {
            cBasicDTOList = FACADE.getAllInternalCountries();
            return GSON.toJson(cBasicDTOList);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
    }

    @GET
    @Path("/new/{days}/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMultipleCovidEntriesByCountry(
            @PathParam("code") String code, @PathParam("days") int days)
    {
        try
        {
            List<CountryInDTO> covDTOs = FACADE.getMultipleInternalCovidEntriesByCountryByDays(code, days);

            // checking if data exists. Has to do this before comparing dates; otherwise nullpointer
            if (covDTOs == null || covDTOs.get(0).getDate() == null)
            {
                try
                {
                    fetchCovidDataFromExternal(covidURL, code);
                }
                catch (ProtocolException ex)
                {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }
                catch (SocketTimeoutException ex)
                {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                    return "{\"msg\": \"Request timeout. No data in database\"}";
                }
                catch (IOException ex)
                {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }

                covDTOs = FACADE.getMultipleInternalCovidEntriesByCountryByDays(code, days);
            }
            return GSON.toJson(covDTOs);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
    }

    @GET
    @Path("/new/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewestCovidEntryForCountryByCode(@PathParam("code") String code)
    {
        try
        {
            CountryInDTO result = FACADE.getNewestInternalCovidEntryForCountryByCode(code);

            // checking if data exists. Has to do this before comparing dates; otherwise nullpointer
            if (result == null || result.getDate() == null)
            {
                try
                {
                    fetchCovidDataFromExternal(covidURL, code);
                }
                catch (ProtocolException ex)
                {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }
                catch (SocketTimeoutException ex)
                {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                    return "{\"msg\": \"Request timeout. No data in database\"}";
                }
                catch (IOException ex)
                {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }

                result = FACADE.getNewestInternalCovidEntryForCountryByCode(code);
            }

            // formatting dates to prepare them for comparison
            DateTimeFormatter format = DateTimeFormatter.ofPattern("EE MMM dd HH:mm:ss 'CEST' yyyy", Locale.ENGLISH);
            LocalDate covidDate = LocalDate.parse(result.getDate(), format);
            LocalDate yesterdayDate = LocalDate.now().minusDays(1);

            // comparing dates from newest entry in DB to today's date
            if (!(covidDate.isEqual(yesterdayDate)))
            {
                try
                {
                    fetchCovidDataFromExternal(covidURL, code);
                }
                catch (ProtocolException ex)
                {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }
                catch (SocketTimeoutException ex)
                {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                    return "{\"msg\": \"Request timeout. No data in database\"}";
                }
                catch (IOException ex)
                {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }

                result = FACADE.getNewestInternalCovidEntryForCountryByCode(code);
            }

            return GSON.toJson(result);
        }
        catch (IllegalArgumentException ex)
        {
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
    }

    @GET
    @Path("/fetch/covid/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String fetchCovidDataForCountryByCode(@PathParam("code") String code)
    {
        try
        {
            List<CovidExDTO> covidList = new ArrayList<>();
            covidList = fetchCovidDataFromExternal(covidURL, code);
            return GSON.toJson(covidList);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
        catch (IOException ex)
        {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return "{\"msg\": \"The provided URL is invalid.\"}";
        }
    }
/**
 * author Christian
 * @param code
 * @return fetchs county from external API by country code
 */
    @GET
    @Path("/fetch/country/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String fetchCountryByCode(@PathParam("code") String code)
    {
        Gson gson = new Gson();
        try
        {
//            String countryData = HttpUtils.fetchData(countryURL + code);
            HashMap headers = new HashMap();
            headers.put("Accept", "application/json;charset=UTF-8");
            String countryData = requestSender.sendRequest(countryURL + code, "GET", headers, 5000);

            if (countryData == null)
            {
                return "{\"msg\": \"No data from " + countryURL + code + "\"}";
            }

            List<CountryExDTO> countryList = gson.fromJson(countryData, new TypeToken<List<CountryExDTO>>()
            {
            }.getType());

            CountryExDTO countryRes = countryList.get(0);
            FACADE.persistExternalCountry(countryRes);
            return GSON.toJson(countryRes);
        }
        catch (IllegalArgumentException | DatabaseException ex)
        {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
        catch (IOException ex)
        {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return "{\"msg\": \"The provided URL is invalid.\"}";
        }
    }
/**
 * author Christian
 * @return all countries from external API
 * @throws IOException 
 */
    @GET
    @Path("/fetch/country")
    @Produces(MediaType.APPLICATION_JSON)
    public String fetchAllCountries() throws IOException
    {
        Gson gson = new Gson();
        try
        {
//            String countryData = HttpUtils.fetchData(countriesURL);
            HashMap headers = new HashMap();
            headers.put("Accept", "application/json;charset=UTF-8");
            String countryData = requestSender.sendRequest(countriesURL, "GET", headers, 5000);

            if (countryData == null)
            {
                return "{\"msg\": \"No data from " + countriesURL + "\"}";
            }

            List<CountryExDTO> countryList = gson.fromJson(countryData, new TypeToken<List<CountryExDTO>>()
            {
            }.getType());

            FACADE.persistAllExternalCountries(countryList);
            return GSON.toJson(countryList);
        }
        catch (IllegalArgumentException | DatabaseException ex)
        {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
        catch (IOException ex)
        {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return "{\"msg\": \"The provided URL is invalid.\"}\n\n" + ex.getMessage() + "";
        }
    }

    // for better stacktrace testing (remove later on)
    public static void main(String[] args) throws IOException, IllegalArgumentException
    {
        CountryResource rest = new CountryResource();
//        System.out.println(rest.fetchCountryByCode("se"));
//        rest.fetchAllCountries();
        rest.fetchCovidDataForCountryByCode("de");
//        System.out.println("Result: " + rest.getNewestCovidEntryForCountryByCode("de"));
//        rest.getNewestCovidEntryForCountryByCode("no");

    }

}
