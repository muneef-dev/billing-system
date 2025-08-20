package com.example.billingsystem.bo.custom;

import com.example.billingsystem.bo.SuperBo;
import com.example.billingsystem.dto.CustomerDto;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBo extends SuperBo {
    boolean createCustomer(CustomerDto customerDTO) throws SQLException, ClassNotFoundException;
    boolean updateCustomer(CustomerDto customerDTO) throws SQLException, ClassNotFoundException;
    boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
    CustomerDto getCustomer(String id) throws SQLException, ClassNotFoundException;
    List<CustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException;
    List<CustomerDto> searchCustomers(String searchTerm) throws SQLException, ClassNotFoundException;
    int getCustomerCount() throws SQLException, ClassNotFoundException;
    CustomerDto getCustomerByAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException;
}
