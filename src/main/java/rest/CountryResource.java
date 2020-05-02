package rest;

import facades.CountryFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dtos.CountriesDTO;
import dtos.CountryBasicInDTO;
import dtos.CountryExDataDTO;
import dtos.CountryInDTO;
import entities.CountryData;
import errorhandling.NotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import utils.EMF_Creator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String demo()
//    {
//        return "{\"msg\":\"Hello World\"}";
//    }
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountryCount()
    {
        long count = FACADE.getInternalCountryCount();
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

//    @GET
//    @Path("in")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getAllInBasicCountries()
//    {
//        List<CountryBasicInDTO> cBasicDTOList;
//        try
//        {
//            cBasicDTOList = FACADE.getAllInternalCountries();
//            return GSON.toJson(cBasicDTOList);
//        } catch (NotFoundException ex)
//        {
//            return "{\"msg\":" + ex.getMessage() + "}";
//        }
//    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLatestByCountryCode(@PathParam("code") String code)
    {
        try
        {
            CountryInDTO cDTO = FACADE.getLatestInternalCovidEntryForCountryByCode(code);
            return GSON.toJson(cDTO);
        } catch (NotFoundException ex)
        {
            return "{\"msg\":" + ex.getMessage() + "}";
        }
    }

    @GET
    @Path("/filldatabase")
    @Produces(MediaType.APPLICATION_JSON)
    public String fillDbWithCountry() throws IOException
    {
        Gson gson = new Gson();
        String countryData = HttpUtils.fetchData("http://restcountries.eu/rest/v1/");

        List<CountryExDataDTO> countries = gson.fromJson(countryData, new TypeToken<List<CountryExDataDTO>>()
        {
        }.getType());

        return gson.toJson(countries);
        // how to get mulitiple list obj and persiste to DB
//        try
//        {
//        if (countryData == null){
//            throw NotFoundException
//        }
//        else{
        // laves i Set/List?
//        CountriesDTO CDTOList = gson.fromJson(countryData, CountriesDTO.class);
//        return gson.toJson(CDTOList);
//        return countryData;
//        }
    }
//        catch (NotFoundException ex)
//        {
//            return "{\"msg\":" + ex.getMessage() + "}";
//        }

    @GET
    @Path("/ex")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountry() throws IOException 
    {
        Gson gson = new Gson();
        String country = HttpUtils.fetchData("https://restcountries.eu/rest/v1/alpha?codes=de");
        CountryExDataDTO dto = gson.fromJson(country, CountryExDataDTO.class);
//        FACADE.persisteExCountry(dto);
        String res = gson.toJson(dto);
        return res;
    }

    // for better stacktrace testning (remove later on)
//      private static CountryResource rest;
//    public static void main(String[] args) throws IOException, NotFoundException
//    {
//        System.out.println(rest.getCountry("de"));
//       
//               
//    }
}
