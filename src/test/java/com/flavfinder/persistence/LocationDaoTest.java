package com.flavfinder.persistence;

import com.flavfinder.entity.SavedLocation;
import com.flavfinder.entity.User;
import com.flavfinder.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for SavedLocation class
 *
 * @author EmileM
 */
class LocationDaoTest {
    GenericDao genericDao;

    /**
     * Initializes the application before performing a test.
     */
    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");

        genericDao = new GenericDao(SavedLocation.class);
    }

    /**
     * Get the users current saved location
     */
    @Test
    void getById() {
        // Get the user_id -> user(id)
        SavedLocation user = (SavedLocation) genericDao.getById(1);

        // Check their current location(aka city)
        String ExpectedCity = user.getCityName();

        //verify
        assertNotNull(user);
        assertEquals(ExpectedCity, user.getCityName());
    }

    /**
     * Update the current location (city name)
     */
    @Test
    void update() {
        // Get the user
        SavedLocation cityToUpdate = (SavedLocation) genericDao.getById(1);

        // change the city name
        cityToUpdate.setCityName("Milwaukee");
        genericDao.update(cityToUpdate);

        // Verify
        SavedLocation user = (SavedLocation) genericDao.getById(1);
        String expectedCity = user.getCityName();
        assertEquals(expectedCity, user.getCityName());
    }

    /**
     * Insert a new location; this should simulate
     * going from current location to a custom location.
     */
    @Test
    void insert() {

    }

    @Test
    void delete() {
    }
}