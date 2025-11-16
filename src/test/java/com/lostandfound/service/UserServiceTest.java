package com.lostandfound.service;

import com.lostandfound.model.User;
import com.lostandfound.utils.DatabaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserServiceTest {
    private Connection connection;
    private UserService userService;
    private User testUser;

    @Before
    public void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userService = new UserService(connection);
        
        // Create a unique test user with timestamp in username
        long timestamp = System.currentTimeMillis();
        testUser = new User("testuser" + timestamp, "testpass", "test@example.com", "1234567890");
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testRegisterUser() throws SQLException {
        boolean result = userService.registerUser(testUser);
        assertTrue("User should be registered successfully", result);
        assertTrue("User ID should be set after registration", testUser.getId() > 0);
    }

    @Test
    public void testRegisterDuplicateUser() throws SQLException {
        // Register user first time
        userService.registerUser(testUser);
        
        // Try to register the same username again
        User duplicateUser = new User(testUser.getUsername(), "anotherpass", "another@example.com", "0987654321");
        boolean result = userService.registerUser(duplicateUser);
        assertFalse("Duplicate user registration should fail", result);
    }

    @Test
    public void testLoginUser() throws SQLException {
        // Register user first
        userService.registerUser(testUser);
        
        // Test login with correct credentials
        User loggedInUser = userService.loginUser(testUser.getUsername(), "testpass");
        assertNotNull("User should be able to login with correct credentials", loggedInUser);
        assertEquals("Username should match", testUser.getUsername(), loggedInUser.getUsername());
        
        // Test login with incorrect password
        User invalidUser = userService.loginUser(testUser.getUsername(), "wrongpass");
        assertNull("User should not be able to login with wrong password", invalidUser);
        
        // Test login with non-existing user
        User nonExistingUser = userService.loginUser("nonexisting", "anypass");
        assertNull("Non-existing user should not be able to login", nonExistingUser);
    }

    @Test
    public void testGetUserById() throws SQLException {
        // Register user first
        userService.registerUser(testUser);
        
        // Get user by ID
        User foundUser = userService.getUserById(testUser.getId());
        assertNotNull("User should be found by ID", foundUser);
        assertEquals("User ID should match", testUser.getId(), foundUser.getId());
        assertEquals("Username should match", testUser.getUsername(), foundUser.getUsername());
    }
}