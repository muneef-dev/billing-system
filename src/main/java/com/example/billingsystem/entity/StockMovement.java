package com.example.billingsystem.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class StockMovement {
    private String id;
    private String itemId;
    private int changeQuantity;
    private String reason; // Sale, Return, Adjustment, Purchase
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public StockMovement() {
    }

    public StockMovement(String id, String itemId, int changeQuantity, String reason,
                        Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.itemId = itemId;
        this.changeQuantity = changeQuantity;
        this.reason = reason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
