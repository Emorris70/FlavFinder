package com.flavfinder.persistence;

import com.flavfinder.entity.Restaurant;
import com.flavfinder.entity.SavedRestaurant;
import com.flavfinder.entity.User;
import com.flavfinder.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CRUD tests for the saved_restaurants join the table.
 */
class SavedRestaurantDaoTest {

    GenericDao<SavedRestaurant> savedRestaurantDao;
    GenericDao<Restaurant> restaurantDao;
    GenericDao<User> userDao;

    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");

        savedRestaurantDao = new GenericDao<>(SavedRestaurant.class);
        restaurantDao = new GenericDao<>(Restaurant.class);
        userDao = new GenericDao<>(User.class);
    }

    /**
     * Retrieve a saved restaurant row by its id.
     */
    @Test
    void getById() {
        SavedRestaurant saved = savedRestaurantDao.getById(1);

        assertNotNull(saved);
        assertEquals(1, saved.getUser().getId());
        assertEquals("api_abc123", saved.getRestaurant().getApiRestaurantId());
    }

    /**
     * Save a new restaurant for a user.
     */
    @Test
    void insert() {
        User user = userDao.getById(1);
        Restaurant restaurant = restaurantDao.getById(2);

        SavedRestaurant entry = SavedRestaurant.builder()
                .user(user)
                .restaurant(restaurant)
                .build();

        int insertedId = savedRestaurantDao.insert(entry);
        SavedRestaurant result = savedRestaurantDao.getById(insertedId);

        assertNotNull(result);
        assertNotNull(result.getSavedAt());
        assertEquals(restaurant.getApiRestaurantId(), result.getRestaurant().getApiRestaurantId());
    }

    /**
     * Delete a saved restaurant - user and restaurant rows should remain.
     */
    @Test
    void delete() {
        SavedRestaurant saved = savedRestaurantDao.getById(1);
        savedRestaurantDao.delete(saved);

        assertNull(savedRestaurantDao.getById(1));
        assertNotNull(restaurantDao.getById(1));
        assertNotNull(userDao.getById(1));
    }

    /**
     * Cascade delete - removing the user should remove their saved restaurants.
     */
    @Test
    void deleteUserCascade() {
        User user = userDao.getById(1);
        userDao.delete(user);

        assertNull(savedRestaurantDao.getById(1));
    }

    /**
     * Fetch all saved restaurants for a user via join.
     * Mirrors: SELECT sr.* FROM saved_restaurants sr JOIN user u ON sr.user_id = u.id WHERE u.id = 1
     */
    @Test
    void joinByUser() {
        List<SavedRestaurant> results = savedRestaurantDao.joinBy("user", "id", 1);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("api_abc123", results.get(0).getRestaurant().getApiRestaurantId());
    }
}
