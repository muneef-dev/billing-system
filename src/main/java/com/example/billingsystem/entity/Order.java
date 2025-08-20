package com.example.billingsystem.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
public class Order {
    // Getters and Setters
    private String id;
    private String orderNumber;
    private String customerId;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String status; // Pending, Paid, Cancelled
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Order() {
    }

    public Order(String id, String orderNumber, String customerId, BigDecimal subtotal,
                    BigDecimal discountAmount, BigDecimal totalAmount, String status,
                    Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
