package com.example.billingsystem.dto;

import java.sql.Timestamp;

public class StockMovementDto {
    private String id;
    private String itemId;
    private int changeQuantity;
    private String reason; // Sale, Return, Adjustment, Purchase
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public StockMovementDto() {
    }

    public StockMovementDto(String id, String itemId, int changeQuantity, String reason,
                           Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.itemId = itemId;
        this.changeQuantity = changeQuantity;
        this.reason = reason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getChangeQuantity() {
        return changeQuantity;
    }

    public void setChangeQuantity(int changeQuantity) {
        this.changeQuantity = changeQuantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
