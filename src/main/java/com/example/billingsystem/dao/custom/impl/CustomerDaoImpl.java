package com.example.billingsystem.dao.custom.impl;

import com.example.billingsystem.dao.CrudUtil;
import com.example.billingsystem.dao.custom.CustomerDao;
import com.example.billingsystem.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public boolean create(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO customers (id, account_number, name, address, telephone, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                customer.getId(),
                customer.getAccountNumber(),
                customer.getName(),
                customer.getAddress(),
                customer.getTelephone(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    @Override
    public List<Customer> search(String s) throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE customers SET account_number=?, name=?, address=?, telephone=?, updated_at=? WHERE id=?",
                customer.getAccountNumber(),
                customer.getName(),
                customer.getAddress(),
                customer.getTelephone(),
                customer.getUpdatedAt(),
                customer.getId()
        );
    }

    @Override
    public List<Customer> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customers ORDER BY name");
        return extractCustomersFromResultSet(resultSet);
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM customers WHERE id=?", id);
    }

    @Override
    public Customer find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customers WHERE id=?", id);
        if (resultSet.next()) {
            return extractCustomerFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public Customer findByAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customers WHERE account_number=?", accountNumber);
        if (resultSet.next()) {
            return extractCustomerFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<Customer> searchCustomers(String searchTerm) throws SQLException, ClassNotFoundException {
        searchTerm = "%" + searchTerm + "%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM customers WHERE name LIKE ? OR account_number LIKE ? OR telephone LIKE ?",
                searchTerm, searchTerm, searchTerm
        );
        return extractCustomersFromResultSet(resultSet);
    }

    @Override
    public int getCustomerCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM customers");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    private Customer extractCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        return new Customer(
                resultSet.getString("id"),
                resultSet.getString("account_number"),
                resultSet.getString("name"),
                resultSet.getString("address"),
                resultSet.getString("telephone"),
                resultSet.getTimestamp("created_at"),
                resultSet.getTimestamp("updated_at")
        );
    }

    private List<Customer> extractCustomersFromResultSet(ResultSet resultSet) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        while (resultSet.next()) {
            customers.add(extractCustomerFromResultSet(resultSet));
        }
        return customers;
    }
}
