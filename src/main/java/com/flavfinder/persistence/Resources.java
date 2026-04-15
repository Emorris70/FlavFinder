package com.flavfinder.persistence;

import com.flavfinder.APIdentity.LocalBusinessResponse;
import com.flavfinder.APIdentity.TomTomResponse;
import jakarta.servlet.ServletContext;
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
     * Instantiates a new Resources.
     */
    public Resources() {

    }

    /**
     * Instantiates a new Resources and populates the properties variable.
     *
     * @param properties The read properties file context.
     */
    public Resources(Properties properties) {
        this();
        this.properties = properties;
    }


    /**
     * HTTP GET request to TomTom URL endpoint. And
     * returns the mapped JSON response.
     *
     * @param rawAddress The users location.
     * @return TomTomResponse the mapped JSON response.
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
     * HTTP GET request to Local Business API endpoint.
     * And returns the mapped JSON response.
     *
     * @param lat users latitude
     * @param lon users longitude
     * @param query users search query
     * @return LocalBusinessResponse the mapped JSON response.
     */
    public LocalBusinessResponse callLocalBusiness(double lat, double lon, String query) {
        Map<String, Object> params = new HashMap<>();

        params.put("query", query);
        params.put("lat", lat);
        params.put("lng", lon);
        params.put("limit", 5);
        params.put("language", "en");
        params.put("region", "us");
        params.put("subtypes", properties.getProperty("rapidapi_subtypes"));


        Map<String, String> headers = new HashMap<>();
        headers.put("x-rapidapi-key", properties.getProperty("rapidapi_key"));
        headers.put("x-rapidapi-host", properties.getProperty("rapidapi_host"));

        return executeGetRequest(
                properties.getProperty("rapidapi_url"),
                null,
                params,
                headers,
                LocalBusinessResponse.class
        );
    }
}
