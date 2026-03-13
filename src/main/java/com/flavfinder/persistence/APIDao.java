package com.flavfinder.persistence;

import com.flavfinder.APIdentity.TomTomResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author EmileM
 */
public class APIDao implements PropertiesLoader {
    private static final Logger log = LogManager.getLogger(APIDao.class);
    private static final double DEFAULT_LAT = 43.0731;
    private static final double DEFAULT_LON = -89.4012;
    private Properties properties;

    /**
     * Instantiates a new APIDao and Initializes the
     * properties.
     */
    public APIDao() {
        loadProperties();
    }

    /**
     * Retrieves properties file values.
     */
    public void loadProperties() {
        try {
            properties = loadProperties("/api.properties");
        } catch (IOException e) {
            log.debug("Issue reading properties file:" + e.getMessage(), e);
        } catch (Exception e) {
            log.debug("Problem locating class path:" + e.getMessage(), e);
        }
    }

    /**
     * HTTP GET request to TomTom URL endpoint
     *
     * @param rawAddress The users location
     */
    public TomTomResponse callTomTom(String rawAddress) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", properties.getProperty("tomtom_key"));
        params.put("limit", 1);
        params.put("countrySet", "US");
        params.put("lat", DEFAULT_LAT);
        params.put("lon", DEFAULT_LON);
        params.put("storeResult", false);
        params.put("view", "Unified");

        return executeGetRequest(
                properties.getProperty("tomtom_geo_url"),
                rawAddress + ".json",
                params,
                null,
                TomTomResponse.class
        );
    }

    /**
     * Performs a synchronous GET request to a REST resource and maps the JSON response
     * to a specified Java type(aka POJO class)
     *
     * This method dynamically builds the target URL using the base URL and path,
     * appends query parameters, sets required headers, and handles connection cleanup
     * automatically.
     *
     * @param baseUrl The base URL of the API service.
     * @param action users search/input criteria.
     * @param queryParams Map of key-value pairs to be appended as query strings.
     * @param headers Map of HTTP headers required by the API.
     * @param responseType The Class of the POJO the response should be mapped into.
     * @return An instance of T containing the mapped API data.
     * @param <T> The type of the response entity.
     */
    public <T> T executeGetRequest(String baseUrl, String action, Map<String, Object> queryParams,
                                   Map<String, String> headers, Class<T> responseType)
    {
        Client client = ClientBuilder.newClient();
        try  {
           // The url to target
            // https://example.api/
           WebTarget target = client.target(baseUrl);

            // 1. Add Path (if any)
            // represents the users entered value.
            // e.g. if user enters 1234 mc street translate to
            // 1234%20mc%20street...
            if (action != null) {
                target = target.path(action);
            }

            // 2. Add Query Parameters dynamically
            if (queryParams != null) {

                for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    target = target.queryParam(entry.getKey(), entry.getValue());
                }
            }

            // 3. Add Headers and Execute
            // The response im expecting back i.e. JSON
            Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);

            if (headers != null) {

                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.header(entry.getKey(), entry.getValue());
                }
            }

            log.debug("Target URL: " + target.getUri());

            // Maps the JSON TEXT e.g. the targeting API class
            // simulates: mapper.readValue(response, targetClass.class)
            return builder.get(responseType);
        } catch (ProcessingException e) {
            log.error("JSON Structure didn't match the POJO" + e.getMessage(), e);
            throw e;
        } catch (WebApplicationException e) {
            log.error("Server returned an error status" + e.getMessage(), e);
            throw e;
        } finally {
             if (client != null) {
                 client.close();
             }
        }
    }

}
