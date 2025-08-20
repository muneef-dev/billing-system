package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.custom.PaymentBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.PaymentDao;
import com.example.billingsystem.dto.PaymentDto;
import com.example.billingsystem.entity.Payment;
import com.example.billingsystem.util.KeyGenerator;
import com.example.billingsystem.util.GeneratorUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentBoImpl implements PaymentBo {

    private final PaymentDao paymentDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.PAYMENT);

    @Override
    public boolean createPayment(PaymentDto paymentDto) throws SQLException, ClassNotFoundException {
        // Generate ID if not provided
        if (paymentDto.getId() == null || paymentDto.getId().trim().isEmpty()) {
            paymentDto.setId(KeyGenerator.generateId());
        }

        // Generate reference number if not provided
        if (paymentDto.getReferenceNumber() == null || paymentDto.getReferenceNumber().trim().isEmpty()) {
            paymentDto.setReferenceNumber(GeneratorUtil.generateReferenceNumber());
        }

        // Validate payment method enum values according to schema: ('Cash', 'BankTransfer')
        if (paymentDto.getMethod() != null) {
            String method = paymentDto.getMethod();
            if (!method.equals("Cash") && !method.equals("BankTransfer")) {
                throw new IllegalArgumentException("Invalid payment method. Must be 'Cash' or 'BankTransfer'");
            }
        } else {
            throw new IllegalArgumentException("Payment method is required");
        }

        return paymentDao.create(new Payment(
                paymentDto.getId(),
                paymentDto.getOrderId(),
                paymentDto.getAmount(),
                paymentDto.getMethod(),
                paymentDto.getReferenceNumber(),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean updatePayment(PaymentDto paymentDto) throws SQLException, ClassNotFoundException {
        return paymentDao.update(new Payment(
                paymentDto.getId(),
                paymentDto.getOrderId(),
                paymentDto.getAmount(),
                paymentDto.getMethod(),
                paymentDto.getReferenceNumber(),
                paymentDto.getCreatedAt(),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean deletePayment(String id) throws SQLException, ClassNotFoundException {
        return paymentDao.delete(id);
    }

    @Override
    public PaymentDto getPayment(String id) throws SQLException, ClassNotFoundException {
        Payment payment = paymentDao.find(id);
        return payment != null ? convertToDto(payment) : null;
    }

    @Override
    public List<PaymentDto> getAllPayments() throws SQLException, ClassNotFoundException {
        List<Payment> payments = paymentDao.loadAll();
        return convertToDtoList(payments);
    }

    @Override
    public List<PaymentDto> getPaymentsByOrder(String orderId) throws SQLException, ClassNotFoundException {
        List<Payment> payments = paymentDao.findByOrderId(orderId);
        return convertToDtoList(payments);
    }

    private PaymentDto convertToDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getReferenceNumber(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }

    private List<PaymentDto> convertToDtoList(List<Payment> payments) {
        List<PaymentDto> paymentDtos = new ArrayList<>();
        for (Payment payment : payments) {
            paymentDtos.add(convertToDto(payment));
        }
        return paymentDtos;
    }
}
