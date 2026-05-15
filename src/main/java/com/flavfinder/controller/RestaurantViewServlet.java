package com.flavfinder.controller;

import com.flavfinder.APIdentity.BusinessItem;
import com.flavfinder.APIdentity.PhotoSample;
import com.flavfinder.entity.Restaurant;
import com.flavfinder.persistence.GenericDao;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles rendering the restaurant detail view page.
 *
 * <p>Resolves the target {@link BusinessItem} from the session-level
 * {@code restaurantCache} (populated by {@link RestaurantApiServlet} on every
 * category fetch) using the {@code placeId} query parameter. No external API
 * call is made — the cache always holds the full object already returned by
 * the list search.
 *
 * <p>On a successful lookup the restaurant is pushed to the front of the
 * {@code recentlyViewed} session list (capped at 6 entries) and the request
 * is forwarded to {@code cardView.jsp}.
 *
 * <p>Response behaviour:
 * <ul>
 *   <li>Redirect → {@code /index.jsp} – unauthenticated request</li>
 *   <li>Redirect → {@code /home}  – missing {@code placeId} param, or
 *       placeId not found in session cache (stale/direct link)</li>
 *   <li>Forward  → {@code cardView.jsp} – happy path</li>
 * </ul>
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/restaurant")
public class RestaurantViewServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(RestaurantViewServlet.class);
    private static final int MAX_RECENTLY_VIEWED = 6;
    private Resources resources;
    private GenericDao<Restaurant> restaurantDao;

    @Override
    public void init() throws ServletException {
        resources = (Resources) getServletContext().getAttribute("resources");
        restaurantDao = new GenericDao<>(Restaurant.class);
    }

    /**
     * Resolves and renders the restaurant detail page.
     *
     * @param req Incoming request; must contain a {@code placeId} query parameter.
     * @param resp Outgoing response.
     * @throws ServletException If a servlet-level error occurs.
     * @throws IOException If writing the response or forwarding fails.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String placeId = req.getParameter("placeId");
        if (placeId == null || placeId.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, BusinessItem> cache =
                (Map<String, BusinessItem>) session.getAttribute("restaurantCache");

        BusinessItem restaurant = cache != null ? cache.get(placeId) : null;

        if (restaurant == null) {
            restaurant = resources.getItem(placeId);
            if (restaurant != null && cache != null) {
                cache.put(placeId, restaurant);
            }
        }

        if (restaurant == null) {
            log.info("RestaurantViewServlet — placeId '{}' not in any cache, calling API", placeId);
            restaurant = resources.callBusinessDetails(placeId);
            if (restaurant != null) {
                injectSnapshotPhotoIfNeeded(restaurant, placeId);
                if (cache != null) cache.put(placeId, restaurant);
            }
        }

        if (restaurant == null) {
            log.warn("RestaurantViewServlet — placeId '{}' not found after API call, redirecting home", placeId);
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        updateRecentlyViewed(session, restaurant);
        log.info("RestaurantViewServlet — rendering detail for '{}'", restaurant.getName());

        session.setAttribute("page", restaurant.getName() + " \u2014 FlavFinder");
        req.setAttribute("restaurant", restaurant);
        req.getRequestDispatcher("/WEB-INF/jsp/cardView.jsp").forward(req, resp);
    }

    /**
     * Prepends the reliable DB thumbnail as index-0 of photosSample so the hero
     * image always loads, regardless of whether the API returned its own photos.
     * Any API photos are kept after index-0 for the photo strip.
     *
     */
    private void injectSnapshotPhotoIfNeeded(BusinessItem item, String placeId) {
        List<Restaurant> matches = restaurantDao.findBy("apiRestaurantId", placeId);
        if (matches.isEmpty() || matches.get(0).getImageUrl() == null) return;

        String url = matches.get(0).getImageUrl();
        PhotoSample photo = new PhotoSample();

        photo.setPhotoUrl(url);
        photo.setPhotoUrlLarge(url);

        List<PhotoSample> photos = new ArrayList<>();
        photos.add(photo);

        if (item.getPhotosSample() != null) photos.addAll(item.getPhotosSample());
        item.setPhotosSample(photos);
        log.info("RestaurantViewServlet — prepended DB snapshot photo for '{}'", placeId);
    }

    /**
     * Moves the given restaurant to the front of the {@code recentlyViewed}
     * session list, creating the list if absent, and trims it to
     * {@value #MAX_RECENTLY_VIEWED} entries.
     *
     * @param session The current HTTP session.
     * @param restaurant The restaurant to record.
     */
    @SuppressWarnings("unchecked")
    private void updateRecentlyViewed(HttpSession session, BusinessItem restaurant) {
        List<BusinessItem> recent =
                (List<BusinessItem>) session.getAttribute("recentlyViewed");
        if (recent == null) recent = new ArrayList<>();

        recent.removeIf(r -> r.getPlaceId().equals(restaurant.getPlaceId()));
        recent.add(0, restaurant);
        if (recent.size() > MAX_RECENTLY_VIEWED) {
            recent = new ArrayList<>(recent.subList(0, MAX_RECENTLY_VIEWED));
        }

        session.setAttribute("recentlyViewed", recent);
    }
}
