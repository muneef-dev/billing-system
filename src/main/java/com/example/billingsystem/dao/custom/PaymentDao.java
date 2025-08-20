package com.example.billingsystem.dao.custom;

import com.example.billingsystem.dao.CrudDao;
import com.example.billingsystem.entity.Payment;

import java.sql.SQLException;
import java.util.List;

public interface PaymentDao extends CrudDao<Payment, String> {
    List<Payment> findByOrderId(String orderId) throws SQLException, ClassNotFoundException;
    Payment find(String id) throws SQLException, ClassNotFoundException;
}
