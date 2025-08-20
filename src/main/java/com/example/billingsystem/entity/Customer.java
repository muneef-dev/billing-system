package com.example.billingsystem.entity;

import java.sql.Timestamp;

public class Customer {
    private String id;
    private String accountNumber;
    private String name;
    private String email;
    private String address;
    private String telephone;
    private String notes;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Customer() {
    }

    public Customer(String id, String accountNumber, String name, String email, String address,
                   String telephone, String notes, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.address = address;
        this.telephone = telephone;
        this.notes = notes;
        this.isActive = isActive;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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
