package com.flavfinder.persistence;

import com.flavfinder.entity.Location;
import com.flavfinder.entity.User;
import com.flavfinder.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic CRUD operations test on the saved_locations table.
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

        genericDao = new GenericDao(Location.class);
    }

    /**
     * Get the users current saved location.
     */
    @Test
    void getById() {
        // Gets the row
        Location location = (Location) genericDao.getById(2);

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
        Location cityToUpdate = (Location) genericDao.getById(2);

        // change the city name
        cityToUpdate.setCityName("Milwaukee");
        genericDao.update(cityToUpdate);

        // Verify
        Location user = (Location) genericDao.getById(2);
        String expectedCity = user.getCityName();
        assertEquals(expectedCity, user.getCityName());
    }

    /**
     * Insert a new location; this should simulate
     * going from current location to a custom location.
     */
    @Test
    void insert() {
        // Get the user(id)
        GenericDao<User> userDao = new GenericDao<>(User.class);
        User user = userDao.getById(1);

        // Add a new location
        Location location = new Location("New York", "1234" ,
                40.71, -74.01, false, user);

        // Insert the location
        // Retrieve the returned id
        int insertedLocation = genericDao.insert(location);

        // Retrieve the new city name
        Location retrievedCityName = (Location) genericDao.getById(insertedLocation);
        // verify
        assertNotNull(retrievedCityName);
        assertEquals(location.getCityName(), retrievedCityName.getCityName());
    }

    /**
     * Deletion of a location.
     */
    @Test
    void delete() {
        // Delete the location by
        genericDao.delete(genericDao.getById(2));
        assertNull(genericDao.getById(2));
    }
}