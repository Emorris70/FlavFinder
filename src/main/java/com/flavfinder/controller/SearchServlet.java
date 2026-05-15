package com.flavfinder.controller;

import com.flavfinder.APIdentity.BusinessItem;
import com.flavfinder.APIdentity.LocalBusinessResponse;
import com.flavfinder.APIdentity.TomTomResponse;
import com.flavfinder.entity.SavedLocation;
import com.flavfinder.entity.SavedRestaurant;
import com.flavfinder.entity.User;
import com.flavfinder.persistence.Resources;
import com.flavfinder.persistence.SavedRestaurantDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Handles free-text restaurant searches submitted from the header search bar.
 *
 * <p>Resolves the user's coordinates from session, calls the Local Business API
 * with the raw search term, caches results, and forwards to home.jsp.
 * Setting {@code requestScope.searchTerm} signals home.jsp to render search-results
 * mode instead of the default nearby-restaurants view.
 *
 * <p>Response behaviour:
 * <ul>
 *   <li>Blank/missing term → redirect to /home</li>
 *   <li>No location in session → forward to home.jsp with searchTerm set but no results</li>
 *   <li>API failure → forward to home.jsp with searchTerm set but no results</li>
 * </ul>
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(SearchServlet.class);

    private Resources resources;
    private SavedRestaurantDao savedRestaurantDao;

    /**
     * Initializes servlet-scoped dependencies from the application context.
     *
     * @throws ServletException If a servlet-level error occurs during initialization.
     */
    @Override
    public void init() throws ServletException {
        resources = (Resources) getServletContext().getAttribute("resources");
        savedRestaurantDao = new SavedRestaurantDao();
    }

    /**
     * Handles the GET request from the header search form.
     *
     * @param req Incoming request; expects a {@code search-term} query parameter.
     * @param resp Outgoing response; forwards to home.jsp with search results.
     * @throws ServletException If a servlet-level error occurs.
     * @throws IOException If writing the response fails.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String searchTerm = req.getParameter("search-term");
        if (searchTerm == null || searchTerm.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        searchTerm = searchTerm.trim();

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

        if (session.getAttribute("savedPlaceIds") == null) {
            User user = (User) session.getAttribute("dbUser");
            List<SavedRestaurant> savedList = savedRestaurantDao.findByUserId(user.getId());

            Set<String> placeIds = new HashSet<>();

            for (SavedRestaurant sr : savedList) {
                placeIds.add(sr.getRestaurant().getApiRestaurantId());
            }
            session.setAttribute("savedPlaceIds", placeIds);
        }

        req.setAttribute("searchTerm", searchTerm);

        if (lat != null) {
            try {
                LocalBusinessResponse results = resources.callLocalBusiness(lat, lon, searchTerm);
                req.setAttribute("nearbyRestaurants", results);
                cacheResults(session, results.getData() != null ? results.getData() : Collections.emptyList());
                log.info("SearchServlet — {} results for query '{}'",
                        results.getData() != null ? results.getData().size() : 0, searchTerm);
            } catch (Exception e) {
                log.error("SearchServlet — API call failed for query '{}'", searchTerm, e);
            }
        } else {
            log.info("SearchServlet — no location in session, skipping API call for query '{}'", searchTerm);
        }

        session.setAttribute("page", "Search - FlavFinder");
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }

    /**
     * Stores each result in the session-level {@code restaurantCache} map,
     * creating the map if it does not yet exist. Keyed by {@code place_id}.
     *
     * @param session The current HTTP session.
     * @param results The list of {@link BusinessItem} objects to cache.
     */
    @SuppressWarnings("unchecked")
    private void cacheResults(HttpSession session, List<BusinessItem> results) {
        Map<String, BusinessItem> cache =
                (Map<String, BusinessItem>) session.getAttribute("restaurantCache");
        if (cache == null) {
            cache = new HashMap<>();
            session.setAttribute("restaurantCache", cache);
        }
        for (BusinessItem item : results) {
            if (item.getPlaceId() != null) {
                cache.put(item.getPlaceId(), item);
            }
        }
    }
}
