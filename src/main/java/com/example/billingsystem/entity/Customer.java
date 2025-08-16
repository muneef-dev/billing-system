package com.example.billingsystem.entity;

import java.sql.Timestamp;

public class Customer {
    private String id;
    private String accountNumber;
    private String name;
    private String address;
    private String telephone;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Customer() {
    }

    public Customer(String id, String accountNumber, String name, String address, String telephone, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
