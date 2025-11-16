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
}