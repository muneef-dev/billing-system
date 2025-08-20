package com.example.billingsystem.dao.custom.impl;

import com.example.billingsystem.dao.CrudUtil;
import com.example.billingsystem.dao.custom.PaymentDao;
import com.example.billingsystem.entity.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDaoImpl implements PaymentDao {

    @Override
    public boolean create(Payment payment) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO payments (id, order_id, amount, method, reference_number, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getReferenceNumber(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }

    @Override
    public List<Payment> search(String s) throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public boolean update(Payment payment) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE payments SET order_id=?, amount=?, method=?, reference_number=?, updated_at=? WHERE id=?",
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getReferenceNumber(),
                payment.getUpdatedAt(),
                payment.getId()
        );
    }

    @Override
    public List<Payment> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM payments ORDER BY created_at DESC");
        return extractPaymentsFromResultSet(resultSet);
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM payments WHERE id=?", id);
    }

    @Override
    public Payment find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM payments WHERE id=?", id);
        if (resultSet.next()) {
            return extractPaymentFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<Payment> findByOrderId(String orderId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM payments WHERE order_id=?", orderId);
        return extractPaymentsFromResultSet(resultSet);
    }

    private Payment extractPaymentFromResultSet(ResultSet resultSet) throws SQLException {
        return new Payment(
                resultSet.getString("id"),
                resultSet.getString("order_id"),
                resultSet.getBigDecimal("amount"),
                resultSet.getString("method"),
                resultSet.getString("reference_number"),
                resultSet.getTimestamp("created_at"),
                resultSet.getTimestamp("updated_at")
        );
    }

    private List<Payment> extractPaymentsFromResultSet(ResultSet resultSet) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        while (resultSet.next()) {
            payments.add(extractPaymentFromResultSet(resultSet));
        }
        return payments;
    }
}
