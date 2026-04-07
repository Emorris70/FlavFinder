package com.flavfinder.controller;

import com.flavfinder.APIdentity.TomTomResponse;
import com.flavfinder.persistence.PropertiesLoader;
import com.flavfinder.persistence.Resources;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Handles location-related requests.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/location")
public class LocationServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(LocationServlet.class);
    // GET the one instance of the resources class
    Resources resources;

    public void init() {
        log.info("LocationServlet initialized");

        resources = (Resources)getServletContext().getAttribute("resources");
    }

    /**
     * Handles POST requests for custom locations.
     *
     * @param req Client's Request.
     * @param resp Server's Response.
     * @throws ServletException If a ServletException occurs.
     * @throws IOException If an Input/Output exception occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get the location input from the form
        String customLocation = req.getParameter("cust-location");
        HttpSession session = req.getSession(false);

        // Call TomTom API and get the coordinates
        TomTomResponse response = resources.callTomTom(customLocation);

        // Store the response in the session
        session.setAttribute("userLocation", response);

        // Simple refresh of the page
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
