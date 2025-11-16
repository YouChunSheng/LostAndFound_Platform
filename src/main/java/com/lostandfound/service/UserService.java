package com.lostandfound.service;

import com.lostandfound.dao.UserDAO;
import com.lostandfound.model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private UserDAO userDAO;

    public UserService(Connection connection) {
        this.userDAO = new UserDAO(connection);
    }

    // Register a new user
    public boolean registerUser(User user) throws SQLException {
        // Check if username already exists
        if (userDAO.usernameExists(user.getUsername())) {
            return false;
        }
        
        // In practice, hash the password before saving
        // For now, we'll save it as-is for simplicity
        return userDAO.createUser(user);
    }

    // Authenticate user login
    public User loginUser(String username, String password) throws SQLException {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // In practice, you would compare hashed passwords
            return user;
        }
        return null;
    }

    // Get user by ID
    public User getUserById(int id) throws SQLException {
        return userDAO.findById(id);
    }
}