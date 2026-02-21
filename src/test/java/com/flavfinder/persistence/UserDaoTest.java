package com.flavfinder.persistence;

import com.flavfinder.entity.User;
import com.flavfinder.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for in association to the UserDao class versus methods.
 *
 * @author EmileM
 */
class UserDaoTest {
    GenericDao genericDao;

    /**
     * Initializes the application before performing a test.
     */
    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");

        genericDao = new GenericDao(User.class);
    }

    /**
     * Test case to get a user by there id.
     */
    @Test
    void getById() {
        // Get the user
        User user = (User)genericDao.getById(1);
        String retrievedName = user.getFirstName();

        // Verify
        assertEquals(user.getFirstName(), retrievedName);
        assertNotNull(user);
    }

    /**
     * Performs an update of a user.
     */
    @Test
    void update() {
        User userToUpdate = (User)genericDao.getById(1);
        userToUpdate.setFirstName("Test");
        genericDao.update(userToUpdate);
        User user = (User)genericDao.getById(1);
        assertEquals("Test", user.getFirstName());
    }

    /**
     * Performs an insert of a new user.
     */
    @Test
    void insert() {
        User newUser = new User("John", "John@gmail.com", "test123");
        // Inserts the new user
        int insertUserId = genericDao.insert(newUser);
        // If returned value > 1 that mean the user was inserted
        assertNotEquals(0, insertUserId);
        // Gets the user inserted by the id
        User insertedUser = (User)genericDao.getById(insertUserId);
        String expectedUser = insertedUser.getFirstName();
        // Verify that the user was inserted
        assertEquals(expectedUser, insertedUser.getFirstName());
    }

    /**
     * Performs a deletion of a user.
     *
     * Confirmed - if a user is removed, associated fields
     * within the saved_location table is also removed.
     */
    @Test
    void delete() {
        genericDao.delete(genericDao.getById(1));
        assertNull(genericDao.getById(1));
    }

    /**
     * Gets all the users.
     */
    @Test
    void getAll() {
        List<User> userList = genericDao.getAll();
        assertEquals(1, userList.size());
    }
}