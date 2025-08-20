package com.example.billingsystem.dao.custom;

import com.example.billingsystem.dao.CrudDao;
import com.example.billingsystem.entity.Order;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao extends CrudDao<Order, String> {
    List<Order> searchOrders(String searchTerm) throws SQLException, ClassNotFoundException;
    List<Order> findByCustomerId(String customerId) throws SQLException, ClassNotFoundException;
    List<Order> getRecentOrders(int limit) throws SQLException, ClassNotFoundException;
    String generateOrderNumber() throws SQLException, ClassNotFoundException;
    Order find(String id) throws SQLException, ClassNotFoundException;
    // New methods for dashboard
    int getOrderCount() throws SQLException, ClassNotFoundException;
    BigDecimal getTotalRevenue() throws SQLException, ClassNotFoundException;
    List<Object[]> getMonthlySalesData() throws SQLException, ClassNotFoundException;
    List<Object[]> getTopSellingItems(int limit) throws SQLException, ClassNotFoundException;
}
