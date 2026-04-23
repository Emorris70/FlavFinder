package com.flavfinder.persistence;

import com.flavfinder.APIdentity.LocalBusinessResponse;
import com.flavfinder.APIdentity.TomTomResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class is the central point for interacting with external
 * RESTful APIs.
 *
 * @author EmileM
 */
public class Resources extends GenericRequest implements PropertiesLoader {
    private static final Logger log = LogManager.getLogger(Resources.class);
    private static final long CACHE_TTL_MS = 15 * 60 * 1000L; // 15 minutes

    private static final class CacheEntry {
        final LocalBusinessResponse response;
        final long createdAt;
        CacheEntry(LocalBusinessResponse response) {
            this.response = response;
            this.createdAt = System.currentTimeMillis();
        }
        boolean isExpired() {
            return System.currentTimeMillis() - createdAt > CACHE_TTL_MS;
        }
    }

    private final ConcurrentHashMap<String, CacheEntry> businessCache = new ConcurrentHashMap<>();

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
        params.put("lat", properties.getProperty("default_lat"));
        params.put("lon", properties.getProperty("default_lon"));
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
        // Round coords to 1km precision to improve cache hit rate
        String cacheKey = String.format("%.3f:%.3f:%s", lat, lon, query.trim().toLowerCase());

        CacheEntry entry = businessCache.get(cacheKey);
        if (entry != null && !entry.isExpired()) {
            log.info("Resources — cache hit for key '{}'", cacheKey);
            return entry.response;
        }

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

        log.info("Resources — cache miss, calling API for key '{}'", cacheKey);
        LocalBusinessResponse response = executeGetRequest(
                properties.getProperty("rapidapi_url"),
                null,
                params,
                headers,
                LocalBusinessResponse.class
        );

        businessCache.put(cacheKey, new CacheEntry(response));
        return response;
    }
}
