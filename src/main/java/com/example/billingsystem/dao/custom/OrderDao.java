package com.example.billingsystem.dao.custom;

import com.example.billingsystem.dao.CrudDao;
import com.example.billingsystem.entity.Order;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao extends CrudDao<Order, String> {
    String generateOrderNumber() throws SQLException, ClassNotFoundException;
    List<Order> findByCustomerId(String customerId) throws SQLException, ClassNotFoundException;
    List<Order> searchOrders(String searchTerm) throws SQLException, ClassNotFoundException;
    List<Order> getRecentOrders(int limit) throws SQLException, ClassNotFoundException;
    Order find(String id) throws SQLException, ClassNotFoundException;
}
