package rest;

import facades.CountryFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.CountryBasicInDTO;
import dtos.CountryExDataDTO;
import dtos.CountryInDTO;
import errorhandling.NotFoundException;
import java.io.IOException;
import utils.EMF_Creator;
import java.util.List;
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
    public String getRenameMeCount()
    {
        long count = FACADE.getInternalCountryCount();
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllInBasicCountries()
    {
        List<CountryBasicInDTO> cBasicDTOList;
        try
        {
            cBasicDTOList = FACADE.getAllInternalCountries();
            return GSON.toJson(cBasicDTOList);
        }
        catch (NotFoundException ex)
        {
            return "{\"msg\":" + ex.getMessage() + "}";
        }
    }
    
    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLatestByCountryCode(@PathParam("code") String code)
    {
        try
        {
            CountryInDTO cDTO = FACADE.getLatestInternalCovidEntryForCountryByCode(code);
            return GSON.toJson(cDTO);
        }
        catch (NotFoundException ex)
        {
            return "{\"msg\":" + ex.getMessage() + "}";
        }
    }
    
    @GET
    @Path("/filldatabase")
    @Produces(MediaType.APPLICATION_JSON)
    public String fillDbWithCountry() throws IOException
    {
        // how to get mulitiple list obj and persiste to DB
//        try
//        {
            String countryData = HttpUtils.fetchData("http://restcountries.eu/rest/v1/");
//        if (countryData == null){
//            throw NotFoundException
//        }
//        else{
            CountryExDataDTO DTO = GSON.fromJson(countryData, CountryExDataDTO.class);
            
            //CountryInDTO cDTO = FACADE.getLatestInternalCovidEntryForCountryByCode(code);
            
            return GSON.toJson(DTO);
//        }
        }
//        catch (NotFoundException ex)
//        {
//            return "{\"msg\":" + ex.getMessage() + "}";
//        }
    
    @GET
    @Path("/ex/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountry(@PathParam("code") String code) throws IOException, NotFoundException
    {
        String country = HttpUtils.fetchData("http://restcountries.eu/rest/v1/alpha?codes=" + code);
        CountryExDataDTO dto = GSON.fromJson(country, CountryExDataDTO.class);
        FACADE.getExternalCountryByCode(dto);
    return GSON.toJson(dto);
    }

    }
    

