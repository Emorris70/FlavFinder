package com.flavfinder.persistence;

import com.flavfinder.entity.User;
import com.flavfinder.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    UserDao userDao;

    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleanDB.sql");
        userDao = new UserDao();
    }

    @Test
    void getById() {
    }

    @Test
    void update() {
    }

    @Test
    void insert() {
        User newUser = new User("test", "test@gmail.com", "test123");
        // Inserts the new user
        int insertUserId = userDao.insert(newUser);
        // If returned value > 1 that mean the user was inserted
        assertNotEquals(0, insertUserId);
        // Gets the user inserted by the id
        User insertedUser = userDao.getById(insertUserId);
        // Gets the ensuring the user "test" was inserted
        assertEquals("test", insertedUser.getFirstName());
    }

    @Test
    void delete() {
    }

    @Test
    void getAll() {
    }
}