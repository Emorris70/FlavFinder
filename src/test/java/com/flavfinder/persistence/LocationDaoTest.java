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

//    /**
//     * Get the users current saved location.
//     */
//    @Test
//    void getById() {
//        // Gets the row
//        SavedLocation location = locationDao.getById(3);
//
//        // Check their current location(aka city)
//        String ExpectedCity = location.getCityName();
//
//        //verify
//        assertNotNull(location);
//        assertEquals(ExpectedCity, location.getCityName());
//    }
//
//    /**
//     * Update the current location (city name).
//     */
//    @Test
//    void update() {
//        // Gets the COLUMN id field NOT the user_id
//        SavedLocation cityToUpdate = locationDao.getById(3);
//
//        // change the city name
//        cityToUpdate.setCityName("Milwaukee");
//        locationDao.update(cityToUpdate);
//
//        // Verify
//        SavedLocation user = locationDao.getById(3);
//        // Compares the updated object values match.
//        assertEquals(cityToUpdate, user);
//
//    }
//
//    /**
//     * Insert a new location; this should simulate
//     * going from current location to a custom location.
//     */
//    @Test
//    void insert() {
//        // Get the user(id)
//        User user = userDao.getById(1);
//
//        // Add a new location
//        SavedLocation location = new SavedLocation("New York", "1234" ,
//                40.71, -74.01, false, user);
//
//        // Insert the location
//        // Retrieve the returned id
//
//        int insertedLocation = locationDao.insert(location);
//
//        // Retrieve the new city name
//        SavedLocation expectedLocation = locationDao.getById(insertedLocation);
//
//        // verify
//        assertNotNull(expectedLocation);
//        // Compares both object values
//        assertEquals(expectedLocation, location);
//    }
//
//    /**
//     * Deletion of a location.
//     *
//     * The deletion of a location SHOULD not affect the
//     * associated user simply removes the location.
//     */
//    @Test
//    void deleteLocation() {
//        // Delete the location by
//        locationDao.delete(locationDao.getById(3));
//        assertNull(locationDao.getById(3));
//    }
//
//    /**
//     * Test user constraint - If a user is deleted associated fields
//     * within the location table should be too.
//     */
//    @Test
//    void deleteUserConstraintTest() {
//        User user = userDao.getById(1);
//        userDao.delete(user);
//
//        // Verify
//        SavedLocation location = locationDao.getById(3);
//        assertNull(location);
//    }

    /**
     * Test the Lombok @Getter annotation.
     */
    @Test
    void lombokGetTest() {
        SavedLocation location = locationDao.getById(3);

        assertNotNull(location);
        assertEquals("Milwaukee",location.getCityName());
    }

    /**
     * Test the toString() method.
     */
    @Test
    void lomToString() {
        SavedLocation location = new SavedLocation();
        location.setCityName("Madison");

        System.out.println(location.toString());
    }

    /**
     * Test hash and equals methods.
     */
    @Test
    void testEquality() {
        User user = userDao.getById(1);
        SavedLocation loc1 = new SavedLocation("Madison", "53704", 43.0, -89.4, true, user);
        SavedLocation loc2 = new SavedLocation("Madison", "53704", 43.0, -89.4, true, user);

        assertTrue(loc1.equals(loc2));
        assertEquals(loc1.hashCode(), loc2.hashCode());
    }

//    @Test
//    void testBuilder() {
//        // This is much easier to read than a long constructor!
//        SavedLocation loc = SavedLocation.builder()
//                .cityName("Madison")
//                .zipCode("53704")
//                .latitude(43.07)
//                .longitude(-89.40)
//                .isDefault(true)
//                .build();
//
//        assertEquals("Madison", loc.getCityName());
//    }
}