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
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import utils.EMF_Creator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // https://www.baeldung.com/java-http-request
    // for future modification can add search parameters, cookies, redirects
    /**
     *
     * @param URL a String containing the target website url.
     * @param method a String containing the request method: GET, POST, HEAD,
     * OPTIONS, PUT, DELETE, TRACE.
     * @param headers a Map of Strings containing all request headers as
     * key/value pairs. Example: "Content-Type", "application/json".
     * @param timeout an int deciding the duration (in milliseconds) before
     * connection timeout.
     *
     * @throws MalformedURLException if the provided URL is invalid.
     * @throws SocketTimeoutException if the request times out.
     * @throws ProtocolException if the provided request method is invalid.
     * @throws IOException if either there is an issue with the connection or
     * there is an issue with reading the response.
     */
    private String sendRequest(String URL, String method, Map<String, String> headers, int timeout)
            throws MalformedURLException, SocketTimeoutException, ProtocolException, IOException
    {
        URL target = new URL(URL);
        HttpURLConnection con = (HttpURLConnection) target.openConnection();

        con.setConnectTimeout(timeout);
        con.setRequestMethod(method);
        con.setDoOutput(false);

        // adding headers
        for (Map.Entry<String, String> map : headers.entrySet())
        {
            con.setRequestProperty(map.getKey(), map.getValue());
        }

        // checking response code
        int responseCode = con.getResponseCode();
        Reader streamReader = null;

        // if 200s (HTTP_OK) read the response body.
        // if 300+ (request fail) read the error response.
        if (responseCode > 299)
        {
//            streamReader = new InputStreamReader(con.getErrorStream());
            return null;
        }
        else
        {
            streamReader = new InputStreamReader(con.getInputStream());
        }

        // build String from response
        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        con.disconnect();
        return response.toString();
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

            System.out.println(covDTO);
            // checking if data exists. Has to do this before comparing dates; otherwise nullpointer

            if (covDTO.getDate() == null)
            {
                synchronized (this)
                {
                    fetchCovidDataForCountryByCode(code);
                    covDTO = FACADE.getNewestInternalCovidEntryForCountryByCode(code);
                }

//                return "{\"msg\": \"The database was empty. Please wait a few seconds and then call this endpoint again.\"}";
                // this is a poor solution. Look into promises or runnables; worst case while loop
//                synchronized (this)
//                {
//                    try
//                    {
//                        fetchCovidDataForCountryByCode(code);
//                        System.out.println("Start" + System.nanoTime());
//                        Thread.sleep(5000);
////                        wait(5000);
//                        covDTO = FACADE.getNewestInternalCovidEntryForCountryByCode(code);
//                        System.out.println("End" + System.nanoTime());
//                    }
//                    catch (InterruptedException ex)
//                    {
//                        Logger.getLogger(CountryResource.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
            }

            System.out.println(covDTO);
            // formatting dates to prepare them for comparison
            DateTimeFormatter format = DateTimeFormatter.ofPattern("EE MMM dd HH:mm:ss 'CEST' yyyy", Locale.ENGLISH);
            LocalDate covidDate = LocalDate.parse(covDTO.getDate(), format);
            LocalDate yesterdayDate = LocalDate.now().minusDays(1);

            System.out.println("Covid String: " + covidDate.toString());
            System.out.println("Yesterday String: " + yesterdayDate.toString());
            String equals = "No";
            if (covidDate.isEqual(yesterdayDate))
            {
                equals = "Yes";
            }
            System.out.println("Are these the same? " + equals);

            // comparing dates from newest entry in DB to today's date
            if (!(covidDate.isEqual(yesterdayDate)))
            {
                synchronized (this)
                {
                    fetchCovidDataForCountryByCode(code);
                    covDTO = FACADE.getNewestInternalCovidEntryForCountryByCode(code);
                }
            }

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
//            String covidData = HttpUtils.fetchData(covidURL + code);

            HashMap headers = new HashMap();
            headers.put("Accept", "application/json;charset=UTF-8");
            String covidData = sendRequest(covidURL + code, "GET", headers, 5000);

            if (covidData == null || covidData.length() < 5)
            {
                return "{\"msg\": \"No data from" + covidURL + code + "\"}";
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
            String countryData = HttpUtils.fetchData(countryURL + code);

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
        catch (NotFoundException | DatabaseException ex)
        {
            return "{\"msg\": \"" + ex.getMessage() + "\"}";
        }
        catch (IOException ex)
        {
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
            String countryData = HttpUtils.fetchData(countriesURL);

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
//        rest.fetchCovidDataForCountryByCode("de");
//        System.out.println("Result: " + rest.getNewestCovidEntryForCountryByCode("de"));
//        rest.getNewestCovidEntryForCountryByCode("no");

    }

}
