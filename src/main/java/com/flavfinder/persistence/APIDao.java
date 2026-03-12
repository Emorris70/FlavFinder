package com.flavfinder.persistence;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Properties;

/**
 * @author EmileM
 */
public class APIDao implements PropertiesLoader {
    private Properties properties;

    // create a helper method
    public APIDao() {

    }

    public void loadProperties() {
        try {
            properties = loadProperties("api.properties");
        } catch (IOException e) {

        } catch () {

        }
    }
}
