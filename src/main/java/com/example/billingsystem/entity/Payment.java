package com.example.billingsystem.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.math.BigDecimal;

@Setter
@Getter
public class Payment {
    private String id;
    private String orderId;
    private BigDecimal amount;
    private String method; // Cash, BankTransfer
    private String referenceNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Payment() {
    }

    public Payment(String id, String orderId, BigDecimal amount, String method,
                  String referenceNumber, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.referenceNumber = referenceNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
