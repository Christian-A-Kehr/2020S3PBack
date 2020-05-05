package rest;

import facades.CountryFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;
import dtos.CountryBasicInDTO;
import dtos.CountryExDTO;
import dtos.CountryInDTO;
import dtos.CovidExDTO;
import errorhandling.DatabaseException;
import errorhandling.NotFoundException;
import java.io.IOException;
import utils.EMF_Creator;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utils.HttpUtils;

@Path("country")
public class CountryResource
{

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/2020S3P_test",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);
    private static final CountryFacade FACADE = CountryFacade.getCountryFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
        catch (NotFoundException ex)
        {
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCovidEntriesForCountryByCode(@PathParam("code") String code)
    {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/new/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewestCovidEntryForCountryByCode(@PathParam("code") String code)
    {
        try
        {
            CountryInDTO covDTO = FACADE.getNewestInternalCovidEntryForCountryByCode(code);
            return GSON.toJson(covDTO);
        }
        catch (NotFoundException ex)
        {
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
    }

    @GET
    @Path("/fetch/covid/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String fetchCovidDataForCountryByCode(@PathParam("code") String code)
    {
        Gson gson = new Gson();
        try
        {
            String covidData = HttpUtils.fetchData("https://api.covid19api.com/dayone/country/" + code);

            if (covidData == null)
            {
                return "{\"msg\": \"No data from https://api.covid19api.com/dayone/country/" + code + "\"}";
            }

            List<CovidExDTO> covidList = gson.fromJson(covidData, new TypeToken<List<CovidExDTO>>()
            {
            }.getType());

            FACADE.persistAllExternalCovidEntriesForCountryByCode(covidList, code);
            return GSON.toJson(covidList);
        }
        catch (NotFoundException ex)
        {
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
        catch (IOException ex)
        {
            return "{\"msg\": \"The provided URL is invalid.\"}";
        }
    }

    @GET
    @Path("/fetch/country/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String fetchCountryByCode(@PathParam("code") String code)
    {
        Gson gson = new Gson();
        try
        {
            String countryData = HttpUtils.fetchData("https://restcountries.eu/rest/v1/alpha?codes=" + code);

            if (countryData == null)
            {
                return "{\"msg\": \"No data from http://restcountries.eu/rest/v1/" + code + "\"}";
            }

            List<CountryExDTO> countryList = gson.fromJson(countryData, new TypeToken<List<CountryExDTO>>()
            {
            }.getType());

            CountryExDTO countryRes = countryList.get(0);
            FACADE.persistExternalCountry(countryRes);
            return GSON.toJson(countryRes);
        }
        catch (NotFoundException | DatabaseException ex)
        {
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
        catch (IOException ex)
        {
            return "{\"msg\": \"The provided URL is invalid.\"}";
        }
    }

    @GET
    @Path("/fetch/country")
    @Produces(MediaType.APPLICATION_JSON)
    public String fetchAllCountries() throws IOException
    {
        Gson gson = new Gson();
        try
        {
            String countryData = HttpUtils.fetchData("http://restcountries.eu/rest/v1/");

            if (countryData == null)
            {
                return "{\"msg\": \"No data from http://restcountries.eu/rest/v1\"}";
            }

            List<CountryExDTO> countryList = gson.fromJson(countryData, new TypeToken<List<CountryExDTO>>()
            {
            }.getType());

            FACADE.persistAllExternalCountries(countryList);
            return GSON.toJson(countryList);
        }
        catch (NotFoundException | DatabaseException ex)
        {
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
        catch (IOException ex)
        {
            return "{\"msg\": \"The provided URL is invalid.\"}\n\n" + ex.getMessage() + "";
        }
    }

    // for better stacktrace testing (remove later on)
    public static void main(String[] args) throws IOException, NotFoundException
    {
        CountryResource rest = new CountryResource();
//        System.out.println(rest.fetchCountryByCode("se"));
//        rest.fetchAllCountries();
        rest.fetchCovidDataForCountryByCode("ro");
//        System.out.println("Result: " + rest.getNewestCovidEntryForCountryByCode("de"));
    }
}
