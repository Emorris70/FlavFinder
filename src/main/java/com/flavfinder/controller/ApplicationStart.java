package com.flavfinder.controller;

import com.flavfinder.persistence.PropertiesLoader;
import com.flavfinder.persistence.Resources;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;

import java.io.IOException;
import java.util.Properties;

/**
 * Initializes the application with essential properties
 * values.
 *
 * @author EmileM
 */
@WebServlet(
        urlPatterns = "/appStart",
        loadOnStartup = 1
)
public class ApplicationStart extends HttpServlet implements PropertiesLoader {
    private static final Logger log = LogManager.getLogger(ApplicationStart.class);
    private Properties properties;

    /**
     * Stores read properties context and passes the values
     * to the Resources class.
     *
     * @throws ServletException if a servlet exception occurs.
     */
    public void init() throws ServletException {
        try {
            this.properties = loadProperties("/api.properties");

            ServletContext context = getServletContext();
            context.setAttribute("properties", properties);

            // Create one Resource instance through-out the application
            Resources resource = new Resources(properties);
            context.setAttribute("resources", resource);

        } catch (IOException e) {
            log.error("Issue reading properties file:" + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Problem locating properties file:" + e.getMessage(), e);
        }
    }
}
