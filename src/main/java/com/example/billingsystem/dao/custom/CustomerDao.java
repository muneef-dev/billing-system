package com.example.billingsystem.dao.custom;

import com.example.billingsystem.dao.CrudDao;
import com.example.billingsystem.entity.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDao extends CrudDao<Customer, String> {
    Customer findByAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException;
    List<Customer> searchCustomers(String searchTerm) throws SQLException, ClassNotFoundException;
    Customer find(String id) throws SQLException, ClassNotFoundException;
}
