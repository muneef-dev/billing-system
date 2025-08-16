package com.example.billingsystem.dao.custom.impl;

import com.example.billingsystem.bo.custom.impl.UserBoImpl;
import com.example.billingsystem.dao.CrudUtil;
import com.example.billingsystem.dao.custom.OrderDao;
import com.example.billingsystem.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDaoImpl implements OrderDao {

    @Override
    public boolean create(Order order) throws SQLException, ClassNotFoundException {
        Logger.getLogger(UserDaoImpl.class.getName()).log(Level.INFO, "OrderBo Executing SQL with ID: " + order.getId());
        return CrudUtil.execute("INSERT INTO orders (id, order_number, customer_id, order_date, total_amount, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }

    @Override
    public List<Order> search(String s) throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public boolean update(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE orders SET customer_id=?, total_amount=?, status=? WHERE id=?",
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM orders WHERE id=?", id);
    }

    @Override
    public Order find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT o.*, c.name as customer_name " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.id " +
                "WHERE o.id=?", id);
        if (resultSet.next()) {
            return extractOrderFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<Order> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT o.*, c.name as customer_name " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.id " +
                "ORDER BY o.order_date DESC");
        return extractOrdersFromResultSet(resultSet);
    }

    @Override
    public String generateOrderNumber() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(CAST(SUBSTRING(order_number, 4) AS UNSIGNED)) as last_num FROM orders WHERE order_number LIKE 'ORD%'");
        if (resultSet.next()) {
            int lastNum = resultSet.getInt("last_num");
            return String.format("ORD%06d", lastNum + 1);
        }
        return "ORD000001";
    }

    @Override
    public List<Order> findByCustomerId(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT o.*, c.name as customer_name " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.id " +
                "WHERE o.customer_id = ? " +
                "ORDER BY o.order_date DESC", customerId);
        return extractOrdersFromResultSet(resultSet);
    }

    @Override
    public List<Order> searchOrders(String searchTerm) throws SQLException, ClassNotFoundException {
        searchTerm = "%" + searchTerm + "%";
        ResultSet resultSet = CrudUtil.execute("SELECT o.*, c.name as customer_name " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.id " +
                "WHERE o.order_number LIKE ? OR c.name LIKE ? " +
                "ORDER BY o.order_date DESC", searchTerm, searchTerm);
        return extractOrdersFromResultSet(resultSet);
    }

    @Override
    public List<Order> getRecentOrders(int limit) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT o.*, c.name as customer_name " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.id " +
                "ORDER BY o.order_date DESC LIMIT ?", limit);
        return extractOrdersFromResultSet(resultSet);
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order(
                rs.getString("id"),
                rs.getString("order_number"),
                rs.getString("customer_id"),
                rs.getTimestamp("order_date"),
                rs.getBigDecimal("total_amount"),
                rs.getString("status")
        );
        order.setCustomerName(rs.getString("customer_name")); // Set the customer name from join
        return order;
    }

    private List<Order> extractOrdersFromResultSet(ResultSet rs) throws SQLException {
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            orders.add(extractOrderFromResultSet(rs));
        }
        return orders;
    }
}
