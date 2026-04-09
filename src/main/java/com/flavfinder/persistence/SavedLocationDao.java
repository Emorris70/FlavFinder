package com.flavfinder.persistence;

import com.flavfinder.entity.SavedLocation;

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
     * @param userId The user id.
     * @return The saved location.
     */
    public SavedLocation findByUserId(String userId) {
        List<SavedLocation> results = findBy("user_id", userId);

        // IF the results are empty, return null else return the first result
        return results.isEmpty() ? null : results.get(0);
    }
}
