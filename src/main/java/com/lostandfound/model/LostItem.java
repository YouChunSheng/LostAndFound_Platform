package com.lostandfound.model;

import java.time.LocalDateTime;

public class LostItem {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String category;
    private String lostLocation;
    private LocalDateTime lostTime;
    private String imageUrl;
    private String contactInfo;
    private String status; // "unclaimed", "claimed"
    private LocalDateTime createdAt;

    // Constructors
    public LostItem() {
        this.status = "unclaimed";
        this.createdAt = LocalDateTime.now();
    }

    public LostItem(int userId, String title, String description, String category,
                    String lostLocation, LocalDateTime lostTime, String imageUrl,
                    String contactInfo) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.lostLocation = lostLocation;
        this.lostTime = lostTime;
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

    public String getLostLocation() {
        return lostLocation;
    }

    public void setLostLocation(String lostLocation) {
        this.lostLocation = lostLocation;
    }

    public LocalDateTime getLostTime() {
        return lostTime;
    }

    public void setLostTime(LocalDateTime lostTime) {
        this.lostTime = lostTime;
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