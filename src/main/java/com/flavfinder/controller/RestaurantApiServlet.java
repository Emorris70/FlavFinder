package com.flavfinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavfinder.APIdentity.BusinessItem;
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
import java.util.Collections;
import java.util.List;

/**
 * JSON endpoint that returns nearby restaurants filtered by cuisine category.
 * Consumed by the category pill buttons on the home page via fetch().
 *
 * <p>Reads the user's coordinates from whichever location state is present in the
 * session (geolocation → TomTom custom → saved DB location), then delegates to
 * the Local Business API using the requested category as the search query.
 *
 * <p>Response codes:
 * <ul>
 *   <li>200 – JSON array of {@link BusinessItem} results</li>
 *   <li>204 – authenticated but no location is set in session</li>
 *   <li>400 – missing or blank {@code category} query param</li>
 *   <li>401 – no active session / unauthenticated user</li>
 *   <li>500 – upstream API call failed</li>
 * </ul>
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/api/restaurants")
public class RestaurantApiServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(RestaurantApiServlet.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private Resources resources;

    @Override
    public void init() throws ServletException {
        resources = (Resources) getServletContext().getAttribute("resources");
    }

    /**
     * Handles GET requests for category-filtered restaurant results.
     *
     * Resolves the user's coordinates from session, calls the Local Business API
     * with {@code "<category> restaurants"} as the query, and writes the resulting
     * list as a JSON array to the response body.
     *
     * @param req  Incoming request; must include a {@code category} query param.
     * @param resp Outgoing response; content-type is {@code application/json} on 200.
     * @throws ServletException If a servlet-level error occurs.
     * @throws IOException      If writing the response body fails.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = req.getParameter("category");
        if (category == null || category.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "category param is required");
            return;
        }

        Double lat = null;
        Double lon = null;

        if (session.getAttribute("userLat") != null) {
            lat = (Double) session.getAttribute("userLat");
            lon = (Double) session.getAttribute("userLon");

        } else if (session.getAttribute("userLocation") != null) {
            TomTomResponse loc = (TomTomResponse) session.getAttribute("userLocation");
            lat = loc.getResults().get(0).getPosition().getLat();
            lon = loc.getResults().get(0).getPosition().getLon();

        } else if (session.getAttribute("savedLocation") != null) {
            SavedLocation saved = (SavedLocation) session.getAttribute("savedLocation");
            lat = saved.getLatitude();
            lon = saved.getLongitude();
        }

        if (lat == null) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        List<BusinessItem> results;
        try {
            LocalBusinessResponse apiResp = resources.callLocalBusiness(lat, lon, category + " restaurants");
            results = apiResp.getData() != null ? apiResp.getData() : Collections.emptyList();
            log.info("RestaurantApiServlet — {} results for category '{}'", results.size(), category);
        } catch (Exception e) {
            log.error("RestaurantApiServlet — API call failed for category '{}'", category, e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), results);
    }
}
