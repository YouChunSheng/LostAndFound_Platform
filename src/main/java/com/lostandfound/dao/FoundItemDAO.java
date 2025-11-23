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
        return searchFoundItems(keyword, category, location, null, null);
    }
    
    // Search found items with additional date range filters
    public List<FoundItem> searchFoundItems(String keyword, String category, String location, String dateFrom, String dateTo) throws SQLException {
        return searchFoundItems(keyword, category, location, dateFrom, dateTo, 0, 20); // 默认查询前20条记录
    }
    
    // Search found items with additional date range filters and pagination
    public List<FoundItem> searchFoundItems(String keyword, String category, String location, String dateFrom, String dateTo, int offset, int limit) throws SQLException {
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
        if (dateFrom != null && !dateFrom.isEmpty()) {
            sql.append(" AND DATE(found_time) >= ?");
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            sql.append(" AND DATE(found_time) <= ?");
        }
        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        
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
            statement.setInt(paramIndex++, limit);
            statement.setInt(paramIndex++, offset);
            
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
    
    // Get total count of found items with filters
    public int getTotalFoundItemsCount(String keyword, String category, String location, String dateFrom, String dateTo) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) as total FROM found_items WHERE 1=1");
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (title LIKE ? OR description LIKE ?)");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ?");
        }
        if (location != null && !location.isEmpty()) {
            sql.append(" AND found_location LIKE ?");
        }
        if (dateFrom != null && !dateFrom.isEmpty()) {
            sql.append(" AND DATE(found_time) >= ?");
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            sql.append(" AND DATE(found_time) <= ?");
        }
        
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
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            return 0;
        }
    }
    
    // Delete found item by ID and user ID (for user to delete their own items)
    public boolean deleteFoundItemByIdAndUserId(int id, int userId) throws SQLException {
        String sql = "DELETE FROM found_items WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        }
    }
    
    // Delete found item by ID only (for admin to delete any items)
    public boolean deleteFoundItemById(int id) throws SQLException {
        String sql = "DELETE FROM found_items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }
    
    // Batch delete found items by IDs (for admin to delete multiple items)
    public int batchDeleteFoundItemsByIds(int[] ids) throws SQLException {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        
        // 构建SQL语句
        StringBuilder sql = new StringBuilder("DELETE FROM found_items WHERE id IN (");
        for (int i = 0; i < ids.length; i++) {
            sql.append("?");
            if (i < ids.length - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.length; i++) {
                statement.setInt(i + 1, ids[i]);
            }
            return statement.executeUpdate();
        }
    }
    
    // Update found item
    public boolean updateFoundItem(FoundItem foundItem) throws SQLException {
        String sql = "UPDATE found_items SET title = ?, description = ?, category = ?, " +
                "found_location = ?, found_time = ?, image_url = ?, contact_info = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, foundItem.getTitle());
            statement.setString(2, foundItem.getDescription());
            statement.setString(3, foundItem.getCategory());
            statement.setString(4, foundItem.getFoundLocation());
            statement.setTimestamp(5, Timestamp.valueOf(foundItem.getFoundTime()));
            statement.setString(6, foundItem.getImageUrl());
            statement.setString(7, foundItem.getContactInfo());
            statement.setInt(8, foundItem.getId());
            statement.setInt(9, foundItem.getUserId());
            
            return statement.executeUpdate() > 0;
        }
    }
    
    // Update found item status
    public boolean updateFoundItemStatus(int id, String status) throws SQLException {
        String sql = "UPDATE found_items SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        }
    }
}