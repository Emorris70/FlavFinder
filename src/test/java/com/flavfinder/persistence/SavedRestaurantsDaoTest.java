package com.flavfinder.persistence;

import com.flavfinder.entity.SavedRestaurants;
import com.flavfinder.entity.User;
import com.flavfinder.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test simulating the process of saving a restaurant.
 *
 * @author EmileM
 */
class SavedRestaurantsDaoTest {
    GenericDao<SavedRestaurants> savedDao;

    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");

        savedDao = new GenericDao<>(SavedRestaurants.class);
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
     * Utilizing the lazy loading to load the rest of additional
     * details using the restaurant id.
     */
    @Test
    void insert() {
        // Get the user to save for...
        GenericDao<User> userDao = new GenericDao<>(User.class);
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
     * Test deletes or "removes" a saved restaurant.
     */
    @Test
    void delete() {

    }

    @Test
    void getAll() {
    }
}