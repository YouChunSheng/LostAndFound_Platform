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
        return searchLostItems(keyword, category, location, dateFrom, dateTo, 0, 20); // 默认查询前20条记录
    }
    
    // Search lost items with additional date range filters and pagination
    public List<LostItem> searchLostItems(String keyword, String category, String location, String dateFrom, String dateTo, int offset, int limit) throws SQLException {
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
    
    // Get total count of lost items with filters
    public int getTotalLostItemsCount(String keyword, String category, String location, String dateFrom, String dateTo) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) as total FROM lost_items WHERE 1=1");
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
    
    // Delete lost item by ID and user ID (for user to delete their own items)
    public boolean deleteLostItemByIdAndUserId(int id, int userId) throws SQLException {
        String sql = "DELETE FROM lost_items WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        }
    }
    
    // Delete lost item by ID only (for admin to delete any items)
    public boolean deleteLostItemById(int id) throws SQLException {
        String sql = "DELETE FROM lost_items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }
    
    // Batch delete lost items by IDs (for admin to delete multiple items)
    public int batchDeleteLostItemsByIds(int[] ids) throws SQLException {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        
        // 构建SQL语句
        StringBuilder sql = new StringBuilder("DELETE FROM lost_items WHERE id IN (");
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
    
    // Update lost item
    public boolean updateLostItem(LostItem lostItem) throws SQLException {
        String sql = "UPDATE lost_items SET title = ?, description = ?, category = ?, " +
                "lost_location = ?, lost_time = ?, image_url = ?, contact_info = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lostItem.getTitle());
            statement.setString(2, lostItem.getDescription());
            statement.setString(3, lostItem.getCategory());
            statement.setString(4, lostItem.getLostLocation());
            statement.setTimestamp(5, Timestamp.valueOf(lostItem.getLostTime()));
            statement.setString(6, lostItem.getImageUrl());
            statement.setString(7, lostItem.getContactInfo());
            statement.setInt(8, lostItem.getId());
            statement.setInt(9, lostItem.getUserId());
            
            return statement.executeUpdate() > 0;
        }
    }
    
    // Update lost item status
    public boolean updateLostItemStatus(int id, String status) throws SQLException {
        String sql = "UPDATE lost_items SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        }
    }
}