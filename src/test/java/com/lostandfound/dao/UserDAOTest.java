package com.lostandfound.dao;

import com.lostandfound.model.User;
import com.lostandfound.utils.DatabaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserDAOTest {
    private Connection connection;
    private UserDAO userDAO;
    private User testUser;

    @Before
    public void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userDAO = new UserDAO(connection);
        
        // Create a test user
        testUser = new User("testuser", "testpass", "test@example.com", "1234567890");
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testCreateUser() throws SQLException {
        boolean result = userDAO.createUser(testUser);
        assertTrue("User should be created successfully", result);
        assertTrue("User ID should be set after creation", testUser.getId() > 0);
    }

    @Test
    public void testFindByUsername() throws SQLException {
        // First create a user
        userDAO.createUser(testUser);
        
        // Then find the user
        User foundUser = userDAO.findByUsername("testuser");
        assertNotNull("User should be found", foundUser);
        assertEquals("Username should match", "testuser", foundUser.getUsername());
        assertEquals("Email should match", "test@example.com", foundUser.getEmail());
    }

    @Test
    public void testFindById() throws SQLException {
        // First create a user
        userDAO.createUser(testUser);
        
        // Then find the user by ID
        User foundUser = userDAO.findById(testUser.getId());
        assertNotNull("User should be found", foundUser);
        assertEquals("User ID should match", testUser.getId(), foundUser.getId());
        assertEquals("Username should match", "testuser", foundUser.getUsername());
    }

    @Test
    public void testUsernameExists() throws SQLException {
        // Test with non-existing username
        boolean exists = userDAO.usernameExists("nonexistinguser");
        assertFalse("Username should not exist", exists);
        
        // Create user and test again
        userDAO.createUser(testUser);
        exists = userDAO.usernameExists("testuser");
        assertTrue("Username should exist after creation", exists);
    }
}