package com.flavfinder.controller;

import com.flavfinder.APIdentity.BusinessItem;
import com.flavfinder.APIdentity.PhotoSample;
import com.flavfinder.entity.Restaurant;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the authenticated user's saved restaurants, hydrates them from the
 * app-level item cache where possible, and forwards to savedRestaurants.jsp.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/saved")
public class SavedRestaurantsServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(SavedRestaurantsServlet.class);

    private Resources resources;
    private SavedRestaurantDao savedRestaurantDao;

    @Override
    public void init() throws ServletException {
        resources = (Resources) getServletContext().getAttribute("resources");
        savedRestaurantDao = new SavedRestaurantDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        User user = (User) session.getAttribute("dbUser");
        List<SavedRestaurant> savedRows = savedRestaurantDao.findByUserId(user.getId());
        List<BusinessItem> displayItems = new ArrayList<>();

        for (SavedRestaurant row : savedRows) {
            Restaurant snapshot = row.getRestaurant();
            String placeId = snapshot.getApiRestaurantId();

            BusinessItem item = resources.getItem(placeId);

            if (item == null) {
                item = buildFromSnapshot(snapshot);
            }

            displayItems.add(item);
        }

        log.info("SavedRestaurantsServlet — {} saved restaurants for user {}", displayItems.size(), user.getId());

        session.setAttribute("page", "To-go \u2014 FlavFinder");
        req.setAttribute("savedRestaurants", displayItems);
        req.getRequestDispatcher("/WEB-INF/jsp/savedRestaurants.jsp").forward(req, resp);
    }

    /**
     * Builds a minimal BusinessItem from the DB snapshot for cache misses.
     * Provides enough data to render the card (name, image, lat/lon, category).
     */
    private BusinessItem buildFromSnapshot(Restaurant snapshot) {
        BusinessItem item = new BusinessItem();
        item.setPlaceId(snapshot.getApiRestaurantId());
        item.setName(snapshot.getName());
        item.setLatitude(snapshot.getLatitude());
        item.setLongitude(snapshot.getLongitude());
        item.setType(snapshot.getCategory());

        if (snapshot.getImageUrl() != null) {
            PhotoSample photo = new PhotoSample();
            photo.setPhotoUrl(snapshot.getImageUrl());
            photo.setPhotoUrlLarge(snapshot.getImageUrl());
            item.setPhotosSample(List.of(photo));
        }

        return item;
    }
}
