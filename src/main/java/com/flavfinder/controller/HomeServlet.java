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
     * <p>
     * Performs a double check accompanied by AuthFilter.
     * Acting as the general protection.
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

        log.info("HomeServlet — session state: userLat={}, userLocation={}, savedLocation={}",
                session.getAttribute("userLat"),
                session.getAttribute("userLocation") != null ? "present" : "null",
                session.getAttribute("savedLocation") != null ? "present" : "null");

        // Only call the API if we have coords
        // TODO Note: commented out this section to reduce API calls uncomment when needed.
//        if (lat != null && lon != null) {
//            try {
//                log.info("HomeServlet — calling API with lat={}, lon={}", lat, lon);
//                LocalBusinessResponse nearbyRestaurants = resources.callLocalBusiness(lat, lon, "food near me");
//                req.setAttribute("nearbyRestaurants", nearbyRestaurants);
//
//                if (nearbyRestaurants.getData() != null) {
//                    log.info("HomeServlet — {} results returned", nearbyRestaurants.getData().size());
//                    for (var business : nearbyRestaurants.getData()) {
//                        log.info("HomeServlet — result: name='{}', lat={}, lon={}, type='{}'",
//                                business.getName(),
//                                business.getLatitude(),
//                                business.getLongitude(),
//                                business.getType());
//                    }
//                } else {
//                    log.warn("HomeServlet - API returned null data");
//                }
//            } catch (Exception e) {
//                log.error("HomeServlet — failed to fetch nearby restaurants", e);
//            }
//        }

        log.info("HomeServlet - forwarding to home.jsp");
        session.setAttribute("page", "Home - FlavFinder");
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}
