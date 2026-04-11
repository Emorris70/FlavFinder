package com.flavfinder.persistence;

import com.flavfinder.entity.SavedLocation;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.engine.spi.SessionDelegatorBaseImpl;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

/**
 * DAO for the SavedLocation entity
 *
 * @author EmileM
 */
public class SavedLocationDao extends GenericDao<SavedLocation> {

    /**
     * Initializes the DAO to strictly work with SavedLocation entities.
     */
    public SavedLocationDao() {
        super(SavedLocation.class);
    }

    /**
     * Gets the saved location by the user id.
     *
     * @param userId The internal user id.
     * @return The saved location or null if not found.
     */
    public SavedLocation findByUserId(int userId) {
        return executeWithSession(session -> {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SavedLocation> query = builder.createQuery(SavedLocation.class);
            var root = query.from(SavedLocation.class);
            query.where(builder.equal(root.get("user").get("id"), userId));
            List<SavedLocation> results = session.createSelectionQuery(query).getResultList();
            return results.isEmpty() ? null : results.get(0);
        });
    }
}
