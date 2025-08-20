package com.example.billingsystem.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.math.BigDecimal;

@Setter
@Getter
public class OrderItem {
    private String id;
    private String orderId;
    private String itemId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public OrderItem() {
    }

    public OrderItem(String id, String orderId, String itemId, int quantity,
                    BigDecimal unitPrice, BigDecimal totalPrice,
                    Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrderItem(String id, String orderId, String itemId, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
    }

}
