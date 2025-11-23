package com.lostandfound.service;

import com.lostandfound.dao.LostItemDAO;
import com.lostandfound.model.LostItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LostItemService {
    private LostItemDAO lostItemDAO;

    public LostItemService(Connection connection) {
        this.lostItemDAO = new LostItemDAO(connection);
    }

    // Create a new lost item
    public boolean createLostItem(LostItem lostItem) throws SQLException {
        return lostItemDAO.createLostItem(lostItem);
    }

    // Get all lost items
    public List<LostItem> getAllLostItems() throws SQLException {
        return lostItemDAO.getAllLostItems();
    }

    // Get lost item by ID
    public LostItem getLostItemById(int id) throws SQLException {
        return lostItemDAO.getLostItemById(id);
    }

    // Get lost items by user ID
    public List<LostItem> getLostItemsByUserId(int userId) throws SQLException {
        return lostItemDAO.getLostItemsByUserId(userId);
    }
    
    // Search lost items with keyword, category and location
    public List<LostItem> searchLostItems(String keyword, String category, String location) throws SQLException {
        return lostItemDAO.searchLostItems(keyword, category, location);
    }
    
    // Search lost items with additional date range filters
    public List<LostItem> searchLostItems(String keyword, String category, String location, String dateFrom, String dateTo) throws SQLException {
        return lostItemDAO.searchLostItems(keyword, category, location, dateFrom, dateTo);
    }
    
    // Search lost items with pagination
    public List<LostItem> searchLostItems(String keyword, String category, String location, String dateFrom, String dateTo, int page, int pageSize) throws SQLException {
        int offset = (page - 1) * pageSize;
        return lostItemDAO.searchLostItems(keyword, category, location, dateFrom, dateTo, offset, pageSize);
    }
    
    // Get total count of lost items with filters
    public int getTotalLostItemsCount(String keyword, String category, String location, String dateFrom, String dateTo) throws SQLException {
        return lostItemDAO.getTotalLostItemsCount(keyword, category, location, dateFrom, dateTo);
    }
    
    // Delete lost item by ID and user ID (for user to delete their own items)
    public boolean deleteLostItemByIdAndUserId(int id, int userId) throws SQLException {
        return lostItemDAO.deleteLostItemByIdAndUserId(id, userId);
    }
    
    // Delete lost item by ID only (for admin use)
    public boolean deleteLostItemById(int id) throws SQLException {
        return lostItemDAO.deleteLostItemById(id);
    }
    
    // Batch delete lost items by IDs (for admin use)
    public int batchDeleteLostItemsByIds(int[] ids) throws SQLException {
        return lostItemDAO.batchDeleteLostItemsByIds(ids);
    }
    
    // Update lost item
    public boolean updateLostItem(LostItem lostItem) throws SQLException {
        return lostItemDAO.updateLostItem(lostItem);
    }
    
    // Update lost item status
    public boolean updateLostItemStatus(int id, String status) throws SQLException {
        return lostItemDAO.updateLostItemStatus(id, status);
    }
}