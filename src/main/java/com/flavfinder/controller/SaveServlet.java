package com.flavfinder.controller;

import com.flavfinder.APIdentity.BusinessItem;
import com.flavfinder.APIdentity.PhotoSample;
import com.flavfinder.entity.Restaurant;
import com.flavfinder.entity.SavedRestaurant;
import com.flavfinder.entity.User;
import com.flavfinder.persistence.GenericDao;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles saving and unsaving restaurants for the authenticated user.
 *
 * POST /save?placeId=X  — saves the restaurant (find-or-create in restaurants table,
 *                          insert into saved_restaurants, cache the BusinessItem)
 * DELETE /save?placeId=X — removes the saved_restaurants row
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/save")
public class SaveServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(SaveServlet.class);

    private Resources resources;
    private SavedRestaurantDao savedRestaurantDao;
    private GenericDao<Restaurant> restaurantDao;

    @Override
    public void init() throws ServletException {
        resources = (Resources) getServletContext().getAttribute("resources");
        savedRestaurantDao = new SavedRestaurantDao();
        restaurantDao = new GenericDao<>(Restaurant.class);
    }

    /**
     * Saves a restaurant for the current user.
     * Expects ?placeId= query param. The BusinessItem must already be in the session cache.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String placeId = req.getParameter("placeId");
        if (placeId == null || placeId.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "placeId is required");
            return;
        }

        @SuppressWarnings("unchecked")
        java.util.Map<String, BusinessItem> sessionCache =
                (java.util.Map<String, BusinessItem>) session.getAttribute("restaurantCache");

        BusinessItem item = sessionCache != null ? sessionCache.get(placeId) : null;
        if (item == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Restaurant not in session cache");
            return;
        }

        User user = (User) session.getAttribute("dbUser");

        try {
            Restaurant restaurant = findOrCreateRestaurant(item);

            if (savedRestaurantDao.findByUserAndRestaurant(user.getId(), restaurant.getId()) != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            SavedRestaurant saved = SavedRestaurant.builder()
                    .user(user)
                    .restaurant(restaurant)
                    .build();
            savedRestaurantDao.insert(saved);

            resources.putItem(placeId, item);
            addToSavedSet(session, placeId);

            log.info("SaveServlet — saved '{}' for user {}", item.getName(), user.getId());
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            log.error("SaveServlet — failed to save restaurant '{}'", placeId, e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a saved restaurant for the current user.
     * Expects ?placeId= query param.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String placeId = req.getParameter("placeId");
        if (placeId == null || placeId.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "placeId is required");
            return;
        }

        User user = (User) session.getAttribute("dbUser");

        try {
            List<Restaurant> matches = restaurantDao.findBy("apiRestaurantId", placeId);
            if (matches.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            Restaurant restaurant = matches.get(0);
            SavedRestaurant saved = savedRestaurantDao.findByUserAndRestaurant(user.getId(), restaurant.getId());
            if (saved != null) {
                savedRestaurantDao.delete(saved);
            }

            removeFromSavedSet(session, placeId);

            log.info("SaveServlet — unsaved '{}' for user {}", placeId, user.getId());
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            log.error("SaveServlet — failed to unsave restaurant '{}'", placeId, e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Finds an existing Restaurant row by api_restaurant_id, or inserts a new one.
     * @param item The BusinessItem to save.
     */
    private Restaurant findOrCreateRestaurant(BusinessItem item) {
        List<Restaurant> existing = restaurantDao.findBy("apiRestaurantId", item.getPlaceId());
        if (!existing.isEmpty()) return existing.get(0);

        String imageUrl = null;
        List<PhotoSample> photos = item.getPhotosSample();
        if (photos != null && !photos.isEmpty()) {
            imageUrl = photos.get(0).getPhotoUrl();
        }

        Restaurant restaurant = Restaurant.builder()
                .apiRestaurantId(item.getPlaceId())
                .name(item.getName())
                .category(item.getType())
                .imageUrl(imageUrl)
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .build();

        int id = restaurantDao.insert(restaurant);
        restaurant = restaurantDao.getById(id);
        return restaurant;
    }

    @SuppressWarnings("unchecked")
    private void addToSavedSet(HttpSession session, String placeId) {
        Set<String> saved = (Set<String>) session.getAttribute("savedPlaceIds");
        if (saved == null) saved = new HashSet<>();
        saved.add(placeId);
        session.setAttribute("savedPlaceIds", saved);
    }

    @SuppressWarnings("unchecked")
    private void removeFromSavedSet(HttpSession session, String placeId) {
        Set<String> saved = (Set<String>) session.getAttribute("savedPlaceIds");
        if (saved != null) saved.remove(placeId);
    }
}
