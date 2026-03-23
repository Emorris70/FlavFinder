package com.flavfinder.persistence;

import com.flavfinder.entity.SavedRestaurants;
import com.flavfinder.entity.User;
import com.flavfinder.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.hibernate.exception.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
// TODO test new join table logic

/**
 * Unit test simulating the process of saving a restaurant.
 *
 * @author EmileM
 */
class SavedRestaurantsDaoTest {
    GenericDao<SavedRestaurants> savedDao;
    GenericDao<User> userDao;

    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");

        savedDao = new GenericDao<>(SavedRestaurants.class);
        userDao = new GenericDao<>(User.class);
    }

    /**
     * Test to simply get the saved restaurants name.
     */
    @Test
    void getById() {
        // Get the id
        SavedRestaurants restaurantId = savedDao.getById(1);
        // Get the name
        String name = restaurantId.getRestaurantName();
        // Verify
        assertNotNull(restaurantId);
        assertEquals(name, restaurantId.getRestaurantName());
    }

    /**
     * Test should simulate the process of saving
     * a restaurant.
     *
     * Even though it's an insert - this process creates a
     * "snapshot" of the desired restaurant of choice. Simply the
     * general information.
     * --
     * Utilizing the method "lazy loading" to load the rest of additional
     * details using the restaurant id.
     */
    @Test
    void insert() {
        // Get the user to save for...
        // user(id)
        User user = userDao.getById(1);

        // save a restaurant of choice
        SavedRestaurants savedRestaurant = new SavedRestaurants("EX-123",
                "greg's tacos", "tacos",
                "https://demo", 35.5, 65.0, user);

        int savedId = savedDao.insert(savedRestaurant);

        // Check
        SavedRestaurants expected = savedDao.getById(savedId);
        assertEquals(expected, savedRestaurant);

    }

    /**
     * Test ensures users cannot save the same restaurant
     */
    @Test
    void noDuplicatedSavedValues() {
        User user = userDao.getById(1);

        SavedRestaurants savedRestaurants = new SavedRestaurants("AZP-123", "Joe's Pizza", "Pizza",
                "https://images.unsplash.com/photo", 40.71, 74.01, user);

        assertThrows(ConstraintViolationException.class, () -> {
            savedDao.insert(savedRestaurants);
        });
    }

    /**
     * Test should verify if a user is deleted
     * saved restaurants field should also be deleted.
     */
    @Test
    void deleteUser() {
        // Get the user
        User user = userDao.getById(1);
        // Delete the user
        userDao.delete(user);

        // Verify...

        // saved_restaurants(id)
        SavedRestaurants restaurants = savedDao.getById(1);
        // If the user is deleted associated field should be null
        assertNull(restaurants);
    }

    /**
     * Test represent "un-hearting" a saved restaurant of choice.
     */
    @Test
    void deleteRestaurant() {
        SavedRestaurants restaurants = savedDao.getById(1);
        savedDao.delete(restaurants);
        // verify
        SavedRestaurants expected = savedDao.getById(1);
        assertNull(expected);
    }

    /**
     * Get a list of all saved restaurants
     */
    @Test
    void getAll() {
        List<SavedRestaurants> restaurants = savedDao.getAll();
        assertEquals(1, restaurants.size());
    }
}