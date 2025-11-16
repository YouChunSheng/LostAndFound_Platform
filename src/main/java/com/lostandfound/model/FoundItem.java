package com.lostandfound.model;

import java.time.LocalDateTime;

public class FoundItem {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String category;
    private String foundLocation;
    private LocalDateTime foundTime;
    private String imageUrl;
    private String contactInfo;
    private String status; // "unclaimed", "claimed"
    private LocalDateTime createdAt;

    // Constructors
    public FoundItem() {
        this.status = "unclaimed";
        this.createdAt = LocalDateTime.now();
    }

    public FoundItem(int userId, String title, String description, String category,
                     String foundLocation, LocalDateTime foundTime, String imageUrl,
                     String contactInfo) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.foundLocation = foundLocation;
        this.foundTime = foundTime;
        this.imageUrl = imageUrl;
        this.contactInfo = contactInfo;
        this.status = "unclaimed";
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFoundLocation() {
        return foundLocation;
    }

    public void setFoundLocation(String foundLocation) {
        this.foundLocation = foundLocation;
    }

    public LocalDateTime getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(LocalDateTime foundTime) {
        this.foundTime = foundTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}