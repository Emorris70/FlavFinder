package com.flavfinder.controller;

import com.flavfinder.APIdentity.LocalBusinessResponse;
import com.flavfinder.APIdentity.TomTomResponse;
import com.flavfinder.entity.SavedLocation;
import com.flavfinder.persistence.Resources;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Handles forwarding authenticated users to the home page.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/home")
public class HomeServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(HomeServlet.class);
    private Resources resources;

    @Override
    public void init() throws ServletException {
        resources = (Resources) getServletContext().getAttribute("resources");
    }

    /**
     * Forwards authenticated users to the home page.
     * --
     * Performs a double check accompanied by AuthFilter.
     * Acting as the general protection.
     * --
     * AuthFilter (General) -> HomeServlet(Specific) -> home.jsp
     *
     * @param req  Client's Request.
     * @param resp Server's Response.
     * @throws ServletException If a ServletException occurs.
     * @throws IOException If an Input/Output exception occurs.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Resolve lat/lon from whichever location state is in session
        Double lat = null;
        Double lon = null;

        if (session.getAttribute("userLat") != null) {
            lat = (Double) session.getAttribute("userLat");
            lon = (Double) session.getAttribute("userLon");
            log.info("HomeServlet: using geolocation: {}, {}", lat, lon);

        } else if (session.getAttribute("userLocation") != null) {
            TomTomResponse loc = (TomTomResponse) session.getAttribute("userLocation");
            lat = loc.getResults().get(0).getPosition().getLat();
            lon = loc.getResults().get(0).getPosition().getLon();
            log.info("HomeServlet: using TomTom custom location: {}, {}", lat, lon);

        } else if (session.getAttribute("savedLocation") != null) {
            SavedLocation saved = (SavedLocation) session.getAttribute("savedLocation");
            lat = saved.getLatitude();
            lon = saved.getLongitude();
            log.info("HomeServlet: using saved location from DB: {}, {}", lat, lon);
        }

        // Only call the API if we have coords
        if (lat != null && lon != null) {
            try {
                log.info("HomeServlet - fetching nearby restaurants for {}, {}", lat, lon);
                LocalBusinessResponse nearbyRestaurants = resources.callLocalBusiness(lat, lon, "restaurant");
                req.setAttribute("nearbyRestaurants", nearbyRestaurants);
                log.info("HomeServlet - {} results returned", nearbyRestaurants.getData() != null
                        ? nearbyRestaurants.getData().size() : 0);
            } catch (Exception e) {
                log.error("HomeServlet - failed to fetch nearby restaurants", e);
                // Don't block the page load if the API call fails
            }
        } else {
            log.info("HomeServlet - no location in session, skipping nearby fetch");
        }

        log.info("HomeServlet - forwarding to home.jsp");
        session.setAttribute("page", "Home - FlavFinder");
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}
