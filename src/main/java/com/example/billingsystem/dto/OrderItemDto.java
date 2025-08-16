package com.example.billingsystem.dto;

import java.math.BigDecimal;

public class OrderItemDto {
    private String id;
    private String orderId;
    private String itemId;
    private String itemName; // For display purposes
    private String itemCode; // For display purposes
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public OrderItemDto() {
    }

    public OrderItemDto(String id, String orderId, String itemId, String itemName,
                       String itemCode, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    // Getters and Setters
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
