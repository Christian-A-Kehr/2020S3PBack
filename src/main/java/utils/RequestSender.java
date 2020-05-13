/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author Brandstrup
 */
public class RequestSender
{

    private static RequestSender instance;

    //Private Constructor to ensure Singleton
    private RequestSender()
    {
    }

    /**
     *
     * @return an instance of this facade class.
     */
    public static RequestSender getRequestSender()
    {
        if (instance == null)
        {
            instance = new RequestSender();
        }
        return instance;
    }

    // https://www.baeldung.com/java-http-request
    // for future modification can add search parameters, cookies, redirects
    /**
     *
     * @param URL a String containing the target website url.
     * @param method a String containing the request method: GET, POST, HEAD,
     * OPTIONS, PUT, DELETE, TRACE.
     * @param headers a Map of Strings containing all request headers as
     * key/value pairs. Example: "Content-Type", "application/json" or 
     * "Accept", "application/json".
     * @param timeout an int deciding the duration (in milliseconds) before
     * connection timeout.
     *
     * @return a Response as a String.
     *
     * @throws MalformedURLException if the provided URL is invalid.
     * @throws SocketTimeoutException if the request times out.
     * @throws ProtocolException if the provided request method is invalid.
     * @throws IOException if either there is an issue with the connection or
     * there is an issue with reading the response. Will also be thrown if the
     * request times out (SocketTimeoutException).
     */
    public String sendRequest(String URL, String method, Map<String, String> headers, int timeout)
            throws MalformedURLException, SocketTimeoutException, ProtocolException, IOException
    {
        URL target = new URL(URL);
        HttpURLConnection con = (HttpURLConnection) target.openConnection();

        con.setConnectTimeout(timeout);
        con.setRequestMethod(method);
        // set to true if you want to allow parameters
        con.setDoOutput(false);

        if (headers.isEmpty())
        {
            throw new IllegalArgumentException("No headers attached to the request.");
        }

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
}
