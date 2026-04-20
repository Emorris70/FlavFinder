package com.flavfinder.persistence;

import com.flavfinder.entity.SavedLocation;
import com.flavfinder.entity.User;
import com.flavfinder.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic CRUD operations test on the saved_locations table.
 */
class LocationDaoTest {
    GenericDao<SavedLocation> locationDao;
    GenericDao<User> userDao;

    /**
     * Initializes the application before performing a test.
     */
    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");

        locationDao = new GenericDao<>(SavedLocation.class);
        userDao = new GenericDao<>(User.class);
    }

    /**
     * Get the users current saved location.
     */
    @Test
    void getById() {
        // Gets the row
        SavedLocation location = locationDao.getById(3);

        // Check their current location(aka city)
        String ExpectedCity = location.getCityName();

        //verify
        assertNotNull(location);
        assertEquals(ExpectedCity, location.getCityName());
    }

    /**
     * Update the current location (city name).
     */
    @Test
    void update() {
        // Gets the COLUMN id field NOT the user_id
        SavedLocation cityToUpdate = locationDao.getById(3);

        // change the city name
        cityToUpdate.setCityName("Milwaukee");
        locationDao.update(cityToUpdate);

        // Verify
        SavedLocation user = locationDao.getById(3);
        // Compares the updated object values match.
        assertEquals(cityToUpdate, user);

    }

    /**
     * Insert a new location; this should simulate
     * going from current location to a custom location.
     */
    @Test
    void insert() {
        // Get the user(id)
        User user = userDao.getById(1);

        // Add a new location
        SavedLocation loc = SavedLocation.builder()
                .cityName("Madison")
                .zipCode("53704")
                .latitude(43.07)
                .longitude(-89.40)
                .isDefault(true)
                .user(user)
                .build();

        // Insert the location
        // Retrieve the returned id

        int insertedLocation = locationDao.insert(loc);

        // Retrieve the new city name
        SavedLocation expectedLocation = locationDao.getById(insertedLocation);

        // verify
        assertNotNull(expectedLocation);
        assertEquals(loc.getCityName(), expectedLocation.getCityName());
    }

    /**
     * Deletion of a location.
     *
     * The deletion of a location SHOULD not affect the
     * associated user simply removes the location.
     */
    @Test
    void deleteLocation() {
        // Delete the location by
        locationDao.delete(locationDao.getById(3));
        assertNull(locationDao.getById(3));
    }

    /**
     * Test user constraint - If a user is deleted associated fields
     * within the location table should be too.
     */
    @Test
    void deleteUserConstraintTest() {
        User user = userDao.getById(1);
        userDao.delete(user);

        // Verify
        SavedLocation location = locationDao.getById(3);
        assertNull(location);
    }
}