package com.lostandfound.dao;

import com.lostandfound.model.LostItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LostItemDAO {
    private Connection connection;

    public LostItemDAO(Connection connection) {
        this.connection = connection;
    }

    // Create a new lost item
    public boolean createLostItem(LostItem lostItem) throws SQLException {
        String sql = "INSERT INTO lost_items (user_id, title, description, category, " +
                "lost_location, lost_time, image_url, contact_info, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, lostItem.getUserId());
            statement.setString(2, lostItem.getTitle());
            statement.setString(3, lostItem.getDescription());
            statement.setString(4, lostItem.getCategory());
            statement.setString(5, lostItem.getLostLocation());
            statement.setTimestamp(6, Timestamp.valueOf(lostItem.getLostTime()));
            statement.setString(7, lostItem.getImageUrl());
            statement.setString(8, lostItem.getContactInfo());
            statement.setString(9, lostItem.getStatus());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    lostItem.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        }
    }

    // Get all lost items
    public List<LostItem> getAllLostItems() throws SQLException {
        List<LostItem> lostItems = new ArrayList<>();
        String sql = "SELECT * FROM lost_items ORDER BY created_at DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                LostItem lostItem = new LostItem();
                lostItem.setId(resultSet.getInt("id"));
                lostItem.setUserId(resultSet.getInt("user_id"));
                lostItem.setTitle(resultSet.getString("title"));
                lostItem.setDescription(resultSet.getString("description"));
                lostItem.setCategory(resultSet.getString("category"));
                lostItem.setLostLocation(resultSet.getString("lost_location"));
                lostItem.setLostTime(resultSet.getTimestamp("lost_time").toLocalDateTime());
                lostItem.setImageUrl(resultSet.getString("image_url"));
                lostItem.setContactInfo(resultSet.getString("contact_info"));
                lostItem.setStatus(resultSet.getString("status"));
                lostItem.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                lostItems.add(lostItem);
            }
        }
        return lostItems;
    }

    // Get lost item by ID
    public LostItem getLostItemById(int id) throws SQLException {
        String sql = "SELECT * FROM lost_items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                LostItem lostItem = new LostItem();
                lostItem.setId(resultSet.getInt("id"));
                lostItem.setUserId(resultSet.getInt("user_id"));
                lostItem.setTitle(resultSet.getString("title"));
                lostItem.setDescription(resultSet.getString("description"));
                lostItem.setCategory(resultSet.getString("category"));
                lostItem.setLostLocation(resultSet.getString("lost_location"));
                lostItem.setLostTime(resultSet.getTimestamp("lost_time").toLocalDateTime());
                lostItem.setImageUrl(resultSet.getString("image_url"));
                lostItem.setContactInfo(resultSet.getString("contact_info"));
                lostItem.setStatus(resultSet.getString("status"));
                lostItem.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                return lostItem;
            }
            return null;
        }
    }

    // Get lost items by user ID
    public List<LostItem> getLostItemsByUserId(int userId) throws SQLException {
        List<LostItem> lostItems = new ArrayList<>();
        String sql = "SELECT * FROM lost_items WHERE user_id = ? ORDER BY created_at DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                LostItem lostItem = new LostItem();
                lostItem.setId(resultSet.getInt("id"));
                lostItem.setUserId(resultSet.getInt("user_id"));
                lostItem.setTitle(resultSet.getString("title"));
                lostItem.setDescription(resultSet.getString("description"));
                lostItem.setCategory(resultSet.getString("category"));
                lostItem.setLostLocation(resultSet.getString("lost_location"));
                lostItem.setLostTime(resultSet.getTimestamp("lost_time").toLocalDateTime());
                lostItem.setImageUrl(resultSet.getString("image_url"));
                lostItem.setContactInfo(resultSet.getString("contact_info"));
                lostItem.setStatus(resultSet.getString("status"));
                lostItem.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                lostItems.add(lostItem);
            }
        }
        return lostItems;
    }
    
    // Search lost items with keyword, category and location
    public List<LostItem> searchLostItems(String keyword, String category, String location) throws SQLException {
        return searchLostItems(keyword, category, location, null, null);
    }
    
    // Search lost items with additional date range filters
    public List<LostItem> searchLostItems(String keyword, String category, String location, String dateFrom, String dateTo) throws SQLException {
        List<LostItem> lostItems = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder("SELECT * FROM lost_items WHERE 1=1");
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (title LIKE ? OR description LIKE ?)");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ?");
        }
        if (location != null && !location.isEmpty()) {
            sql.append(" AND lost_location LIKE ?");
        }
        if (dateFrom != null && !dateFrom.isEmpty()) {
            sql.append(" AND DATE(lost_time) >= ?");
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            sql.append(" AND DATE(lost_time) <= ?");
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
            if (dateFrom != null && !dateFrom.isEmpty()) {
                statement.setString(paramIndex++, dateFrom);
            }
            if (dateTo != null && !dateTo.isEmpty()) {
                statement.setString(paramIndex++, dateTo);
            }
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LostItem lostItem = new LostItem();
                lostItem.setId(resultSet.getInt("id"));
                lostItem.setUserId(resultSet.getInt("user_id"));
                lostItem.setTitle(resultSet.getString("title"));
                lostItem.setDescription(resultSet.getString("description"));
                lostItem.setCategory(resultSet.getString("category"));
                lostItem.setLostLocation(resultSet.getString("lost_location"));
                lostItem.setLostTime(resultSet.getTimestamp("lost_time").toLocalDateTime());
                lostItem.setImageUrl(resultSet.getString("image_url"));
                lostItem.setContactInfo(resultSet.getString("contact_info"));
                lostItem.setStatus(resultSet.getString("status"));
                lostItem.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                lostItems.add(lostItem);
            }
        }
        return lostItems;
    }
}