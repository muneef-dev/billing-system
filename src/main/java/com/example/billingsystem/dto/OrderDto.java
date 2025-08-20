package com.example.billingsystem.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderDto {
    private String id;
    private String orderNumber;
    private String customerId;
    private String customerName; // Add customerName field
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String status; // Pending, Paid, Cancelled
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public OrderDto() {
    }

    public OrderDto(String id, String orderNumber, String customerId, BigDecimal subtotal,
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

    // Add constructor with customerName
    public OrderDto(String id, String orderNumber, String customerId, String customerName,
                   BigDecimal subtotal, BigDecimal discountAmount, BigDecimal totalAmount,
                   String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.status = status;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
