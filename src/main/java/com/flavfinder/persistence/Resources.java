package com.flavfinder.persistence;

import com.flavfinder.APIdentity.TomTomResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class is the central point for interacting with external
 * RESTful APIs.
 *
 * @author EmileM
 */
public class Resources extends GenericRequest implements PropertiesLoader {
    private static final Logger log = LogManager.getLogger(Resources.class);
    private static final double DEFAULT_LAT = 43.0731;
    private static final double DEFAULT_LON = -89.4012;
    private Properties properties;

    /**
     * Instantiates a new APIDao and Initializes the
     * properties.
     */
    public Resources() {
        loadProperties();
    }

    // TODO Add application start up
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
     * HTTP GET request to TomTom URL endpoint. And
     * returns the mapped JSON response.
     *
     * @param rawAddress The users location.
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
}
