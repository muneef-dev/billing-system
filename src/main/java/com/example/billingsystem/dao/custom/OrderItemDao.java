package com.example.billingsystem.dao.custom;

import com.example.billingsystem.dao.CrudDao;
import com.example.billingsystem.entity.OrderItem;
import java.sql.SQLException;
import java.util.List;

public interface OrderItemDao extends CrudDao<OrderItem, String> {
    List<OrderItem> findByOrderId(String orderId) throws SQLException, ClassNotFoundException;
    boolean deleteByOrderId(String orderId) throws SQLException, ClassNotFoundException;
    boolean createBatch(List<OrderItem> orderItems) throws SQLException, ClassNotFoundException;
    OrderItem find(String id) throws SQLException, ClassNotFoundException;
}
