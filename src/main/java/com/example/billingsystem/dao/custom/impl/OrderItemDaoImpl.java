package com.example.billingsystem.dao.custom.impl;

import com.example.billingsystem.bo.custom.impl.UserBoImpl;
import com.example.billingsystem.dao.CrudUtil;
import com.example.billingsystem.dao.custom.OrderItemDao;
import com.example.billingsystem.entity.OrderItem;
import com.example.billingsystem.util.KeyGenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderItemDaoImpl implements OrderItemDao {

    @Override
    public boolean create(OrderItem orderItem) throws SQLException, ClassNotFoundException {
        Logger.getLogger(OrderItemDaoImpl.class.getName()).log(Level.INFO,
            "OrderItemDaoImpl Creating order item - ID: " + orderItem.getId() +
            ", Order ID: " + orderItem.getOrderId() +
            ", Item ID: " + orderItem.getItemId());

        return CrudUtil.execute("INSERT INTO order_items (id, order_id, item_id, quantity, unit_price, subtotal) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                orderItem.getId(),
                orderItem.getOrderId(),
                orderItem.getItemId(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getSubtotal()
        );
    }

    @Override
    public List<OrderItem> search(String s) throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public boolean update(OrderItem orderItem) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE order_items SET quantity=?, unit_price=?, subtotal=? WHERE id=?",
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getSubtotal(),
                orderItem.getId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM order_items WHERE id=?", id);
    }

    @Override
    public OrderItem find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
            "SELECT oi.*, i.name as item_name, i.item_code " +
            "FROM order_items oi " +
            "LEFT JOIN items i ON oi.item_id = i.id " +
            "WHERE oi.id=?", id);
        if (resultSet.next()) {
            return extractOrderItemFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<OrderItem> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
            "SELECT oi.*, i.name as item_name, i.item_code " +
            "FROM order_items oi " +
            "LEFT JOIN items i ON oi.item_id = i.id");
        return extractOrderItemsFromResultSet(resultSet);
    }

    @Override
    public List<OrderItem> findByOrderId(String orderId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
            "SELECT oi.*, i.name as item_name, i.item_code " +
            "FROM order_items oi " +
            "LEFT JOIN items i ON oi.item_id = i.id " +
            "WHERE oi.order_id = ?", orderId);
        return extractOrderItemsFromResultSet(resultSet);
    }

    @Override
    public boolean deleteByOrderId(String orderId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM order_items WHERE order_id=?", orderId);
    }

    @Override
    public boolean createBatch(List<OrderItem> orderItems) throws SQLException, ClassNotFoundException {
        Logger.getLogger(OrderItemDaoImpl.class.getName()).log(Level.INFO,
            "OrderItemDaoImpl Creating batch of " + orderItems.size() + " items");

        boolean success = true;
        for (OrderItem item : orderItems) {
            // Log each item's details before creation
            Logger.getLogger(OrderItemDaoImpl.class.getName()).log(Level.INFO,
                "Processing item - ID: " + item.getId() +
                ", Order ID: " + item.getOrderId() +
                ", Item ID: " + item.getItemId());

            // Validate all required fields are present
            if (item.getId() == null || item.getId().trim().isEmpty() ||
                item.getOrderId() == null || item.getOrderId().trim().isEmpty() ||
                item.getItemId() == null || item.getItemId().trim().isEmpty()) {
                Logger.getLogger(OrderItemDaoImpl.class.getName()).log(Level.SEVERE,
                    "Invalid item data - ID: " + item.getId() +
                    ", Order ID: " + item.getOrderId() +
                    ", Item ID: " + item.getItemId());
                return false;
            }

            success &= create(item);
            if (!success) {
                Logger.getLogger(OrderItemDaoImpl.class.getName()).log(Level.SEVERE,
                    "Failed to create item - ID: " + item.getId());
                break;
            }
        }
        return success;
    }

    private OrderItem extractOrderItemFromResultSet(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem(
                rs.getString("id"),
                rs.getString("order_id"),
                rs.getString("item_id"),
                rs.getInt("quantity"),
                rs.getBigDecimal("unit_price"),
                rs.getBigDecimal("subtotal")
        );
        orderItem.setItemName(rs.getString("item_name"));
        orderItem.setItemCode(rs.getString("item_code"));
        return orderItem;
    }

    private List<OrderItem> extractOrderItemsFromResultSet(ResultSet rs) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        while (rs.next()) {
            orderItems.add(extractOrderItemFromResultSet(rs));
        }
        return orderItems;
    }
}
