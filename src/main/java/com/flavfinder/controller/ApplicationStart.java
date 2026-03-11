package com.flavfinder.controller;

import com.flavfinder.persistence.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
     * Stores read .properties values, granting application access to
     * said items.
     *
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    public void init() throws ServletException {
        try {
            properties = loadProperties("/api.properties");
            ServletContext context = getServletContext();
            context.setAttribute( "values" ,properties);

        }  catch (IOException ioException) {
            log.error("Cannot load properties:" + ioException.getMessage(), ioException);
        } catch (Exception e) {
            log.error("Error loading properties:" + e.getMessage(), e);
        }
    }
}
