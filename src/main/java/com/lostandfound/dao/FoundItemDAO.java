package com.lostandfound.dao;

import com.lostandfound.model.FoundItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoundItemDAO {
    private Connection connection;

    public FoundItemDAO(Connection connection) {
        this.connection = connection;
    }

    // Create a new found item
    public boolean createFoundItem(FoundItem foundItem) throws SQLException {
        String sql = "INSERT INTO found_items (user_id, title, description, category, " +
                "found_location, found_time, image_url, contact_info, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, foundItem.getUserId());
            statement.setString(2, foundItem.getTitle());
            statement.setString(3, foundItem.getDescription());
            statement.setString(4, foundItem.getCategory());
            statement.setString(5, foundItem.getFoundLocation());
            statement.setTimestamp(6, Timestamp.valueOf(foundItem.getFoundTime()));
            statement.setString(7, foundItem.getImageUrl());
            statement.setString(8, foundItem.getContactInfo());
            statement.setString(9, foundItem.getStatus());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    foundItem.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        }
    }

    // Get all found items
    public List<FoundItem> getAllFoundItems() throws SQLException {
        List<FoundItem> foundItems = new ArrayList<>();
        String sql = "SELECT * FROM found_items ORDER BY created_at DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                FoundItem foundItem = new FoundItem();
                foundItem.setId(resultSet.getInt("id"));
                foundItem.setUserId(resultSet.getInt("user_id"));
                foundItem.setTitle(resultSet.getString("title"));
                foundItem.setDescription(resultSet.getString("description"));
                foundItem.setCategory(resultSet.getString("category"));
                foundItem.setFoundLocation(resultSet.getString("found_location"));
                foundItem.setFoundTime(resultSet.getTimestamp("found_time").toLocalDateTime());
                foundItem.setImageUrl(resultSet.getString("image_url"));
                foundItem.setContactInfo(resultSet.getString("contact_info"));
                foundItem.setStatus(resultSet.getString("status"));
                foundItem.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                foundItems.add(foundItem);
            }
        }
        return foundItems;
    }

    // Get found item by ID
    public FoundItem getFoundItemById(int id) throws SQLException {
        String sql = "SELECT * FROM found_items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                FoundItem foundItem = new FoundItem();
                foundItem.setId(resultSet.getInt("id"));
                foundItem.setUserId(resultSet.getInt("user_id"));
                foundItem.setTitle(resultSet.getString("title"));
                foundItem.setDescription(resultSet.getString("description"));
                foundItem.setCategory(resultSet.getString("category"));
                foundItem.setFoundLocation(resultSet.getString("found_location"));
                foundItem.setFoundTime(resultSet.getTimestamp("found_time").toLocalDateTime());
                foundItem.setImageUrl(resultSet.getString("image_url"));
                foundItem.setContactInfo(resultSet.getString("contact_info"));
                foundItem.setStatus(resultSet.getString("status"));
                foundItem.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                return foundItem;
            }
            return null;
        }
    }

    // Get found items by user ID
    public List<FoundItem> getFoundItemsByUserId(int userId) throws SQLException {
        List<FoundItem> foundItems = new ArrayList<>();
        String sql = "SELECT * FROM found_items WHERE user_id = ? ORDER BY created_at DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                FoundItem foundItem = new FoundItem();
                foundItem.setId(resultSet.getInt("id"));
                foundItem.setUserId(resultSet.getInt("user_id"));
                foundItem.setTitle(resultSet.getString("title"));
                foundItem.setDescription(resultSet.getString("description"));
                foundItem.setCategory(resultSet.getString("category"));
                foundItem.setFoundLocation(resultSet.getString("found_location"));
                foundItem.setFoundTime(resultSet.getTimestamp("found_time").toLocalDateTime());
                foundItem.setImageUrl(resultSet.getString("image_url"));
                foundItem.setContactInfo(resultSet.getString("contact_info"));
                foundItem.setStatus(resultSet.getString("status"));
                foundItem.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                foundItems.add(foundItem);
            }
        }
        return foundItems;
    }
    
    // Search found items with keyword, category and location
    public List<FoundItem> searchFoundItems(String keyword, String category, String location) throws SQLException {
        List<FoundItem> foundItems = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder("SELECT * FROM found_items WHERE 1=1");
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (title LIKE ? OR description LIKE ?)");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ?");
        }
        if (location != null && !location.isEmpty()) {
            sql.append(" AND found_location LIKE ?");
        }
        sql.append(" ORDER BY created_at DESC");
        
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (keyword != null && !keyword.isEmpty()) {
                statement.setString(paramIndex++, "%" + keyword + "%");
                statement.setString(paramIndex++, "%" + keyword + "%");
            }
            if (category != null && !category.isEmpty()) {
                statement.setString(paramIndex++, category);
            }
            if (location != null && !location.isEmpty()) {
                statement.setString(paramIndex++, "%" + location + "%");
            }
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FoundItem foundItem = new FoundItem();
                foundItem.setId(resultSet.getInt("id"));
                foundItem.setUserId(resultSet.getInt("user_id"));
                foundItem.setTitle(resultSet.getString("title"));
                foundItem.setDescription(resultSet.getString("description"));
                foundItem.setCategory(resultSet.getString("category"));
                foundItem.setFoundLocation(resultSet.getString("found_location"));
                foundItem.setFoundTime(resultSet.getTimestamp("found_time").toLocalDateTime());
                foundItem.setImageUrl(resultSet.getString("image_url"));
                foundItem.setContactInfo(resultSet.getString("contact_info"));
                foundItem.setStatus(resultSet.getString("status"));
                foundItem.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                foundItems.add(foundItem);
            }
        }
        return foundItems;
    }
}