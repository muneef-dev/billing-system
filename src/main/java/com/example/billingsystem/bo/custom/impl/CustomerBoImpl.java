package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.custom.CustomerBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.CustomerDao;
import com.example.billingsystem.dto.CustomerDto;
import com.example.billingsystem.entity.Customer;
import com.example.billingsystem.util.KeyGenerator;
import com.example.billingsystem.util.GeneratorUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CustomerBoImpl implements CustomerBo {

    private final CustomerDao customerDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.CUSTOMER);

    @Override
    public boolean createCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
        // Generate ID if not provided
        if (customerDto.getId() == null || customerDto.getId().trim().isEmpty()) {
            customerDto.setId(KeyGenerator.generateId());
        }

        // Generate account number if not provided
        if (customerDto.getAccountNumber() == null || customerDto.getAccountNumber().trim().isEmpty()) {
            customerDto.setAccountNumber(GeneratorUtil.generateAccountNumber());
        }

        return customerDao.create(new Customer(
                customerDto.getId(),
                customerDto.getAccountNumber(),
                customerDto.getName(),
                customerDto.getEmail(),
                customerDto.getAddress(),
                customerDto.getTelephone(),
                customerDto.getNotes(),
                customerDto.isActive(),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
        return customerDao.update(new Customer(
                customerDto.getId(),
                customerDto.getAccountNumber(),
                customerDto.getName(),
                customerDto.getEmail(),
                customerDto.getAddress(),
                customerDto.getTelephone(),
                customerDto.getNotes(),
                customerDto.isActive(),
                customerDto.getCreatedAt(),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDao.delete(id);
    }

    @Override
    public CustomerDto getCustomer(String id) throws SQLException, ClassNotFoundException {
        Customer customer = customerDao.find(id);
        return customer != null ? convertToDto(customer) : null;
    }

    @Override
    public CustomerDto getCustomerByAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException {
        Customer customer = customerDao.findByAccountNumber(accountNumber);
        return customer != null ? convertToDto(customer) : null;
    }

    @Override
    public List<CustomerDto> searchCustomers(String searchTerm) throws SQLException, ClassNotFoundException {
        List<Customer> customers = customerDao.searchCustomers(searchTerm);
        return convertToDtoList(customers);
    }

    @Override
    public int getCustomerCount() throws SQLException, ClassNotFoundException {
        return customerDao.loadAll().size();
    }

    @Override
    public List<CustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException {
        List<Customer> customers = customerDao.loadAll();
        return convertToDtoList(customers);
    }

    private CustomerDto convertToDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getAccountNumber(),
                customer.getName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getTelephone(),
                customer.getNotes(),
                customer.isActive(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    private List<CustomerDto> convertToDtoList(List<Customer> customers) {
        List<CustomerDto> customerDtos = new ArrayList<>();
        for (Customer customer : customers) {
            customerDtos.add(convertToDto(customer));
        }
        return customerDtos;
    }
}
