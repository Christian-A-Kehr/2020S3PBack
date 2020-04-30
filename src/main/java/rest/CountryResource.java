package rest;

import facades.CountryFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.CountryBasicInDTO;
import dtos.CountryInDTO;
import errorhandling.NotFoundException;
import utils.EMF_Creator;
import facades.FacadeExample;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
        long count = FACADE.getInCountryCount();
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllInCountries()
    {
        List<CountryBasicInDTO> cBasicDTOList;
        try
        {
            cBasicDTOList = FACADE.getAllInCountries();
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
            CountryInDTO cDTO = FACADE.getLatestCovidEntryForCountryById(code);
            return GSON.toJson(cDTO);
        }
        catch (NotFoundException ex)
        {
            return "{\"msg\":" + ex.getMessage() + "}";
        }
    }
    
}
