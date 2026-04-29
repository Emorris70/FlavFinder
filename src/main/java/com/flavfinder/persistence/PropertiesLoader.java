package com.flavfinder.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Processes the content of a .properties file
 * @author EmileM
 */
public interface PropertiesLoader {

    Logger log = LogManager.getLogger(PropertiesLoader.class);

    /**
     * Loads properties from environment variables when deployed on Railway,
     * falling back to a classpath file for local development.
     *
     * @param propertiesFilePath classpath path used only in local dev
     * @return populated Properties instance
     */
    default Properties loadProperties(String propertiesFilePath) throws IOException, Exception {
        if (System.getenv("MYSQL_URL") != null) {
            log.info("Railway environment detected - loading {} from env vars", propertiesFilePath);
            return loadFromEnvironment(propertiesFilePath);
        }

        InputStream stream = this.getClass().getResourceAsStream(propertiesFilePath);
        if (stream == null) {
            throw new IOException("Properties file not found on classpath: " + propertiesFilePath);
        }
        Properties properties = new Properties();
        properties.load(stream);
        return properties;
    }

    private Properties loadFromEnvironment(String filePath) {
        Properties p = new Properties();
        if (filePath.contains("cognito")) {
            p.setProperty("aws.cognito.userPoolId",   System.getenv("COGNITO_USER_POOL_ID"));
            p.setProperty("aws.cognito.clientId",     System.getenv("COGNITO_CLIENT_ID"));
            p.setProperty("aws.cognito.clientSecret", System.getenv("COGNITO_CLIENT_SECRET"));
            p.setProperty("aws.cognito.region",       System.getenv("COGNITO_REGION"));
        } else if (filePath.contains("config")) {
            p.setProperty("tomtom_key",          env("TOMTOM_KEY"));
            p.setProperty("tomtom_geo_url",      "https://api.tomtom.com/search/2/geocode/");
            p.setProperty("default_lat",         "43.0731");
            p.setProperty("default_lon",         "-89.4012");
            p.setProperty("rapidapi_key",        env("RAPIDAPI_KEY"));
            p.setProperty("rapidapi_host",       "local-business-data.p.rapidapi.com");
            p.setProperty("rapidapi_url",        "https://local-business-data.p.rapidapi.com/search-nearby");
            p.setProperty("rapidapi_detail_url", "https://local-business-data.p.rapidapi.com/business-details");
            p.setProperty("rapidapi_subtypes",   "Restaurant,American restaurant,Italian restaurant,Chinese restaurant,Japanese restaurant,Mexican restaurant,Indian restaurant,Thai restaurant,Mediterranean restaurant,French restaurant,Korean restaurant,Vietnamese restaurant,Pizza restaurant,Hamburger restaurant,Seafood restaurant,Sushi restaurant,Steak house,Cafe,Coffee shop,Bakery,Fast food restaurant,Sandwich shop,Bar,Pub,Ramen restaurant,Buffet restaurant,Breakfast restaurant,Brunch restaurant,Soul food restaurant,Southern restaurant,Greek restaurant,Spanish restaurant,Middle Eastern restaurant,African restaurant,Caribbean restaurant,Latin American restaurant,Asian restaurant,Food court,Diner,Bistro,Brasserie,Tapas bar,Wine bar,Gastropub");
        }
        return p;
    }

    private String env(String key) {
        String value = System.getenv(key);
        if (value == null) {
            log.warn("Environment variable {} is not set", key);
            return "";
        }
        return value;
    }
}
