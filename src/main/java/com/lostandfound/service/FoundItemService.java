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
}