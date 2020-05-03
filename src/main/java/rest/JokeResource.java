        package rest;

import com.google.gson.Gson;
import dtos.ChuckDTO;
import dtos.DadDTO;
import dtos.OurDTO;
import java.io.IOException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import utils.HttpUtils;

/**
 * REST Web Service
 *
 * @author Christian
 */
@Path("jokes")
public class JokeResource {

    @Context
    private UriInfo context;

   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJokes() throws IOException {
       Gson gson = new Gson();
        String chuck1 = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        ChuckDTO chuckDTO1 = gson.fromJson(chuck1, ChuckDTO.class);
        
        String chuck2 = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        ChuckDTO chuckDTO2 = gson.fromJson(chuck2, ChuckDTO.class);
        
        String chuck3 = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        ChuckDTO chuckDTO3 = gson.fromJson(chuck3, ChuckDTO.class);
        
        String chuck4 = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        ChuckDTO chuckDTO4 = gson.fromJson(chuck4, ChuckDTO.class);

        String dad = HttpUtils.fetchData("https://icanhazdadjoke.com");
        DadDTO dadDTO = gson.fromJson(dad, DadDTO.class);

        OurDTO combinedDTO = new OurDTO(chuckDTO1, chuckDTO2, chuckDTO3, chuckDTO4, dadDTO);

        //This is what your endpoint should return
        String combinedJSON = gson.toJson(combinedDTO);


        return combinedJSON;
    }

   
}
