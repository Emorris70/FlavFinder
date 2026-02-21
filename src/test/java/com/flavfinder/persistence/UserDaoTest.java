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
    UserDao userDao;
    GenericDao genericDao;

    /**
     * Initializes the application before performing a test.
     */
    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");
        userDao = new UserDao();
        genericDao = new GenericDao(User.class);
    }

    /**
     * Test case to get a user by there id
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
     * Performs an update of a user
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
        int insertUserId = userDao.insert(newUser);
        // If returned value > 1 that mean the user was inserted
        assertNotEquals(0, insertUserId);
        // Gets the user inserted by the id
        User insertedUser = userDao.getById(insertUserId);
        String expectedUser = insertedUser.getFirstName();
        // Verify that the user was inserted
        assertEquals(expectedUser, insertedUser.getFirstName());
    }

    /**
     * Performs a deletion of a user
     */
    @Test
    void delete() {
        userDao.delete(userDao.getById(1));
        assertNull(userDao.getById(1));
    }

    /**
     * Gets all the users.
     */
    @Test
    void getAll() {
        List<User> userList = userDao.getAll();
        assertEquals(1, userList.size());
    }
}