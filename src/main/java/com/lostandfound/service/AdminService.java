package com.lostandfound.service;

import com.lostandfound.dao.UserDAO;
import com.lostandfound.dao.LostItemDAO;
import com.lostandfound.dao.FoundItemDAO;
import com.lostandfound.model.User;
import com.lostandfound.model.LostItem;
import com.lostandfound.model.FoundItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdminService {
    private UserDAO userDAO;
    private LostItemDAO lostItemDAO;
    private FoundItemDAO foundItemDAO;
    private Connection connection;

    public AdminService(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO(connection);
        this.lostItemDAO = new LostItemDAO(connection);
        this.foundItemDAO = new FoundItemDAO(connection);
    }

    // Get all users
    public List<User> getAllUsers() throws SQLException {
        // In a real implementation, you would have a method in UserDAO to get all users
        // This is a simplified version
        return null;
    }

    // Get all lost items
    public List<LostItem> getAllLostItems() throws SQLException {
        return lostItemDAO.getAllLostItems();
    }

    // Get all found items
    public List<FoundItem> getAllFoundItems() throws SQLException {
        return foundItemDAO.getAllFoundItems();
    }

    // Delete user by ID
    public boolean deleteUser(int userId) throws SQLException {
        // In a real implementation, you would have a method in UserDAO to delete user
        // This is a simplified version
        return false;
    }

    // Delete lost item by ID
    public boolean deleteLostItem(int itemId) throws SQLException {
        String sql = "DELETE FROM lost_items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            return statement.executeUpdate() > 0;
        }
    }

    // Delete found item by ID
    public boolean deleteFoundItem(int itemId) throws SQLException {
        String sql = "DELETE FROM found_items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            return statement.executeUpdate() > 0;
        }
    }
}