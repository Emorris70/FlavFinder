package com.flavfinder.controller;

import com.flavfinder.APIdentity.AuthenticatedUser;
import com.flavfinder.APIdentity.ResultsItem;
import com.flavfinder.APIdentity.TomTomResponse;
import com.flavfinder.entity.SavedLocation;
import com.flavfinder.entity.User;
import com.flavfinder.persistence.GenericDao;
import com.flavfinder.persistence.Resources;
import com.flavfinder.persistence.SavedLocationDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Handles location-related requests. In addition,
 * stores the user's location in the database.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/location")
public class LocationServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(LocationServlet.class);
    // GET the one instance of the resources class
    Resources resources;
    SavedLocationDao locationDao;
    GenericDao<User> userDao;

    public void init() throws ServletException {
        log.info("LocationServlet initialized");

        resources = (Resources)getServletContext().getAttribute("resources");
        locationDao = new SavedLocationDao();
        userDao = new GenericDao<>(User.class);
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

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Validates the input
        if (customLocation == null || customLocation.trim().isEmpty()) {
            session.setAttribute("locationError", "Please enter a valid location");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }


        // Call TomTom API and pass in user input
        TomTomResponse response = resources.callTomTom(customLocation);

        // In case if no results are found
        if (response.getResults() == null || response.getResults().isEmpty()) {
            log.warn("TomTom returned no results for: " + customLocation);
            session.setAttribute("locationError", "Location not found. Please try a different search.");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // Items to store in the db
        ResultsItem result = response.getResults().get(0);
        double lat = result.getPosition().getLat();
        double lon = result.getPosition().getLon();
        String city = result.getAddress().getMunicipality();
        String zip = result.getAddress().getPostalCode();

        AuthenticatedUser authUser = (AuthenticatedUser) session.getAttribute("user");
        // Gets the user from the db based on the sub id
        List<User> users = userDao.findBy("sub", authUser.getSub());

        if (users.isEmpty()) {
            log.error("User not found in the database");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // Check existing location based on user(id)
        User dbUser = users.get(0);
        getExistingLocation(dbUser, false, city, zip, lat, lon);


        // Store the response in the session
        session.setAttribute("userLocation", response);
        session.removeAttribute("userLat");
        session.removeAttribute("userLon");
        // Simple refresh of the page
        resp.sendRedirect(req.getContextPath() + "/home");
    }



    /**
     * Handles GET requests for the current location.
     * Passed in as a parameter from the JS file.
     *
     * @param req Client's request.
     * @param resp Server's response.
     * @throws ServletException If a ServletException occurs.
     * @throws IOException If an Input/Output exception occurs.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String lat = req.getParameter("lat");
        String lon = req.getParameter("lon");
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Edge case user denied browser geolocation
        if (lat == null || lon == null) {
            log.warn("Geolocation params missing: user may have denied access");
            session.setAttribute("locationError", "Location access was denied. Please enter a location manually.");

            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        double parsedLat;
        double parsedLon;
        try {
            parsedLat = Double.parseDouble(lat);
            parsedLon = Double.parseDouble(lon);
        } catch (NumberFormatException e) {
            log.warn("Invalid geolocation params: lat={}, lon={}", lat, lon);
            session.setAttribute("locationError", "Invalid location data. Please try again.");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        AuthenticatedUser authUser = (AuthenticatedUser) session.getAttribute("user");
        List<User> users = userDao.findBy("sub", authUser.getSub());

        if (users.isEmpty()) {
            log.error("No DB user found for sub: " + authUser.getSub());
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        User dbUser = users.get(0);
        getExistingLocation(dbUser, true, null, null, parsedLat, parsedLon);

        session.setAttribute("userLat", parsedLat);
        session.setAttribute("userLon", parsedLon);
        session.removeAttribute("userLocation");
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    /**
     * Helper method to upsert a saved location for a given user.
     *
     * @param user The database user.
     * @param status Whether the location is the default.
     * @param city The city name.
     * @param zip The zip code.
     * @param lat Latitude of the location.
     * @param lon Longitude of the location.
     */
    public void getExistingLocation(User user, boolean status,
                                    String city, String zip,
                                    double lat, double lon) {
        SavedLocation existing = locationDao.findByUserId(user.getId());

        if (existing == null) {
            SavedLocation newLocation = SavedLocation.builder()
                    .cityName(city)
                    .zipCode(zip)
                    .latitude(lat)
                    .longitude(lon)
                    .isDefault(status)
                    .user(user)
                    .build();
            locationDao.insert(newLocation);
        } else {
            existing.setCityName(city);
            existing.setZipCode(zip);
            existing.setLatitude(lat);
            existing.setLongitude(lon);
            existing.setDefault(status);
            locationDao.update(existing);
        }
    }
}