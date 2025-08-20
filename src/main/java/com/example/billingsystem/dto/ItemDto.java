package com.example.billingsystem.dto;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class ItemDto {
    private String id;
    private String itemCode;
    private String itemName;
    private String category;
    private String author;
    private String publisher;
    private String description;
    private String coverImageUrl;
    private BigDecimal unitPrice;
    private BigDecimal costPrice;
    private int stockQuantity;
    private int minimumStockLevel;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ItemDto() {
    }

    public ItemDto(String id, String itemCode, String itemName, String category, String author, String publisher,
                  String description, String coverImageUrl, BigDecimal unitPrice, BigDecimal costPrice,
                  int stockQuantity, int minimumStockLevel, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.unitPrice = unitPrice;
        this.costPrice = costPrice;
        this.stockQuantity = stockQuantity;
        this.minimumStockLevel = minimumStockLevel;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getMinimumStockLevel() {
        return minimumStockLevel;
    }

    public void setMinimumStockLevel(int minimumStockLevel) {
        this.minimumStockLevel = minimumStockLevel;
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
