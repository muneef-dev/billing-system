package com.example.billingsystem.entity;

import java.sql.Timestamp;

public class Category {
    private String id;
    private String categoryName;
    private String description;
    private String categoryId; // self-referencing for parent category
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Category() {
    }

    public Category(String id, String categoryName, String description, String categoryId,
                   boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.categoryId = categoryId;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
