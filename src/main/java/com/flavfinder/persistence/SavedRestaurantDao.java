package com.flavfinder.persistence;

import com.flavfinder.entity.SavedRestaurant;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

/**
 * DAO for the SavedRestaurant join entity.
 *
 * @author EmileM
 */
public class SavedRestaurantDao extends GenericDao<SavedRestaurant> {

    /**
     * Initializes the DAO to strictly work with SavedRestaurant entities.
     */
    public SavedRestaurantDao() {
        super(SavedRestaurant.class);
    }

    /**
     * Returns all saved restaurants for a given user.
     *
     * @param userId The internal user id.
     * @return List of SavedRestaurant rows for that user.
     */
    public List<SavedRestaurant> findByUserId(int userId) {
        return joinBy("user", "id", userId);
    }

    /**
     * Returns the saved restaurant row for a specific user + restaurant pair, or null.
     *
     * @param userId Internal user id.
     * @param restaurantId Internal restaurant id.
     * @return The matching SavedRestaurant or null if not found.
     */
    public SavedRestaurant findByUserAndRestaurant(int userId, int restaurantId) {
        return executeWithSession(session -> {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SavedRestaurant> query = builder.createQuery(SavedRestaurant.class);
            var root = query.from(SavedRestaurant.class);
            query.where(
                    builder.and(
                            builder.equal(root.get("user").get("id"), userId),
                            builder.equal(root.get("restaurant").get("id"), restaurantId)
                    )
            );
            List<SavedRestaurant> results = session.createSelectionQuery(query).getResultList();
            return results.isEmpty() ? null : results.get(0);
        });
    }
}
