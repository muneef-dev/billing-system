package com.example.billingsystem.dto;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class ItemDto {
    private String id;
    private String itemCode;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ItemDto() {
    }

    public ItemDto(String id, String itemCode, String name, String description, BigDecimal price, int stockQuantity,
                  Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.itemCode = itemCode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public ItemDto(String id, String s, String name, String description, BigDecimal price, int quantity) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
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
