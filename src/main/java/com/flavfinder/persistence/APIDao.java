package com.flavfinder.persistence;

import com.flavfinder.APIdentity.TomTomResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
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
    private static final Logger log = LogManager.getLogger(GenericDao.class);
    private static final Client client = ClientBuilder.newClient();
    private Properties properties;

    /**
     * Instantiates a new APIDao and Initializes the
     * properties.
     */
    public APIDao() {
        loadProperties();
    }
//    look into reduce redundant code
    /**
     * Retrieves properties file values.
     */
    public void loadProperties() {
        try {
            properties = loadProperties("api.properties");
        } catch (IOException e) {
            log.debug("Issue reading properties file:" + e.getMessage(), e);
        } catch (Exception e) {
            log.debug("Problem locating class path:" + e.getMessage(), e);
        }
    }

    public void callTomTom(String rawAddress, double lat, double lon) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", properties.getProperty("tomtom_key"));
        params.put("lat", lat);
        params.put("lon", lon);

        TomTomResponse response = executeGetRequest(
                properties.getProperty("tomtom_geo_url"),
                rawAddress + ".json",
                params,
                null,
                TomTomResponse.class
        );
    }

    /**
     *
     * @param baseUrl The targeted URL
     * @param path The
     * @param queryParams The
     * @param headers
     * @param responseType targeted API e.g. tomtom or RapidAPI
     * @return
     * @param <T> The type of entity
     */
    public <T> T executeGetRequest(String baseUrl, String path, Map<String, Object> queryParams,
                                   Map<String, String> headers, Class<T> responseType)
    {
       // The url to target
       WebTarget target = client.target(baseUrl);

        // 1. Add Path (if any)
        if (path != null) {
            target = target.path(path);
        }

        // 2. Add Query Parameters dynamically
        if (queryParams != null) {
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }

        // 3. Add Headers and Execute
        // Saying im expecting a JSON response
        Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                // get the API key
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        log.debug("Target URL: " + target.getUri());

        // Maps the JSON TEXT e.g. the targeting API class
        // simulates: mapper.readValue(response, targetClass.class)
        return builder.get(responseType);
    }

}
