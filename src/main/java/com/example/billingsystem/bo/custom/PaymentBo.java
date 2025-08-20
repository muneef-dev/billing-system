package com.example.billingsystem.bo.custom;

import com.example.billingsystem.bo.SuperBo;
import com.example.billingsystem.dto.PaymentDto;

import java.sql.SQLException;
import java.util.List;

public interface PaymentBo extends SuperBo {
    boolean createPayment(PaymentDto paymentDto) throws SQLException, ClassNotFoundException;
    boolean updatePayment(PaymentDto paymentDto) throws SQLException, ClassNotFoundException;
    boolean deletePayment(String id) throws SQLException, ClassNotFoundException;
    PaymentDto getPayment(String id) throws SQLException, ClassNotFoundException;
    List<PaymentDto> getAllPayments() throws SQLException, ClassNotFoundException;
    List<PaymentDto> getPaymentsByOrder(String orderId) throws SQLException, ClassNotFoundException;
}
