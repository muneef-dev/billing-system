package com.example.billingsystem.dto;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class PaymentDto {
    private String id;
    private String orderId;
    private BigDecimal amount;
    private String method; // Cash, BankTransfer
    private String referenceNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public PaymentDto() {
    }

    public PaymentDto(String id, String orderId, BigDecimal amount, String method,
                     String referenceNumber, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.referenceNumber = referenceNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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
