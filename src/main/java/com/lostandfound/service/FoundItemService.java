package com.lostandfound.service;

import com.lostandfound.dao.FoundItemDAO;
import com.lostandfound.model.FoundItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FoundItemService {
    private FoundItemDAO foundItemDAO;

    public FoundItemService(Connection connection) {
        this.foundItemDAO = new FoundItemDAO(connection);
    }

    // Create a new found item
    public boolean createFoundItem(FoundItem foundItem) throws SQLException {
        return foundItemDAO.createFoundItem(foundItem);
    }

    // Get all found items
    public List<FoundItem> getAllFoundItems() throws SQLException {
        return foundItemDAO.getAllFoundItems();
    }

    // Get found item by ID
    public FoundItem getFoundItemById(int id) throws SQLException {
        return foundItemDAO.getFoundItemById(id);
    }

    // Get found items by user ID
    public List<FoundItem> getFoundItemsByUserId(int userId) throws SQLException {
        return foundItemDAO.getFoundItemsByUserId(userId);
    }
    
    // Search found items with keyword, category and location
    public List<FoundItem> searchFoundItems(String keyword, String category, String location) throws SQLException {
        return foundItemDAO.searchFoundItems(keyword, category, location);
    }
    
    // Search found items with additional date range filters
    public List<FoundItem> searchFoundItems(String keyword, String category, String location, String dateFrom, String dateTo) throws SQLException {
        return foundItemDAO.searchFoundItems(keyword, category, location, dateFrom, dateTo);
    }
    
    // Search found items with pagination
    public List<FoundItem> searchFoundItems(String keyword, String category, String location, String dateFrom, String dateTo, int page, int pageSize) throws SQLException {
        int offset = (page - 1) * pageSize;
        return foundItemDAO.searchFoundItems(keyword, category, location, dateFrom, dateTo, offset, pageSize);
    }
    
    // Get total count of found items with filters
    public int getTotalFoundItemsCount(String keyword, String category, String location, String dateFrom, String dateTo) throws SQLException {
        return foundItemDAO.getTotalFoundItemsCount(keyword, category, location, dateFrom, dateTo);
    }
    
    // Delete found item by ID and user ID (for user to delete their own items)
    public boolean deleteFoundItemByIdAndUserId(int id, int userId) throws SQLException {
        return foundItemDAO.deleteFoundItemByIdAndUserId(id, userId);
    }
    
    // Delete found item by ID only (for admin use)
    public boolean deleteFoundItemById(int id) throws SQLException {
        return foundItemDAO.deleteFoundItemById(id);
    }
    
    // Batch delete found items by IDs (for admin use)
    public int batchDeleteFoundItemsByIds(int[] ids) throws SQLException {
        return foundItemDAO.batchDeleteFoundItemsByIds(ids);
    }
    
    // Update found item
    public boolean updateFoundItem(FoundItem foundItem) throws SQLException {
        return foundItemDAO.updateFoundItem(foundItem);
    }
    
    // Update found item status
    public boolean updateFoundItemStatus(int id, String status) throws SQLException {
        return foundItemDAO.updateFoundItemStatus(id, status);
    }
}