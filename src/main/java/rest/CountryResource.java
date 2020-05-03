package rest;

import facades.CountryFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dtos.CountryBasicInDTO;
import dtos.CountryExDataDTO;
import dtos.CountryInDTO;
import errorhandling.DatabaseException;
import errorhandling.NotFoundException;
import java.io.IOException;
import utils.EMF_Creator;
import java.util.List;
import java.util.logging.Level;
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
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountryCount()
    {
        long count = FACADE.getInternalCountryCount();
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
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
        } catch (NotFoundException ex)
        {
            return "{\"msg\":\"" + ex.getMessage() + "\"}";
        }
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewestCovidEntryByCountryCode(@PathParam("code") String code)
    {
        try
        {
            CountryInDTO cDTO = FACADE.getLatestInternalCovidEntryForCountryByCode(code);
            return GSON.toJson(cDTO);
        } catch (NotFoundException ex)
        {
            return "{\"msg\":\"" + ex.getMessage() + "\"}";
        }
    }

    @GET
    @Path("/fetch")
    @Produces(MediaType.APPLICATION_JSON)
    public String fetchAllCountries() throws IOException
    {
        Gson gson = new Gson();
        String countryData = HttpUtils.fetchData("http://restcountries.eu/rest/v1/");

        try
        {
            if (countryData == null)
            {
                return "{\"msg\":\"No data from http://restcountries.eu/rest/v1/\"}";
            }
            List<CountryExDataDTO> countries = gson.fromJson(countryData, new TypeToken<List<CountryExDataDTO>>()
            {
            }.getType());
            FACADE.persistAllExternalCountries(countries);
            return gson.toJson(countries);
        } catch (NotFoundException ex)
        {
            return "{\"msg\":\"" + ex.getMessage() + "\"}";
        }

    }

    @GET
    @Path("/fetch/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String fetchCountryByCode(@PathParam("code") String code)
    {
        Gson gson = new Gson();
        try
        {
            String country = HttpUtils.fetchData("https://restcountries.eu/rest/v1/alpha?codes=" + code);
            List<CountryExDataDTO> countryList = gson.fromJson(country, new TypeToken<List<CountryExDataDTO>>()
            {
            }.getType());

            String res = GSON.toJson(countryList.get(0));
            FACADE.persisteExCountry(countryList.get(0));
            return res;
        } catch (NotFoundException | IOException | DatabaseException ex)
        {
            return "{\"msg\":\"" + ex.getMessage() + "\"}";
        }

    }

    // for better stacktrace testning (remove later on)
    public static void main(String[] args) throws IOException, NotFoundException
    {
        CountryResource rest = new CountryResource();
//        System.out.println(rest.fetchCountryByCode("se"));
        rest.fetchAllCountries();
    }
}
