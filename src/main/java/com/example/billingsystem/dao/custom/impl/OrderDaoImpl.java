package com.example.billingsystem.dao.custom.impl;

import com.example.billingsystem.bo.custom.impl.UserBoImpl;
import com.example.billingsystem.dao.CrudUtil;
import com.example.billingsystem.dao.custom.OrderDao;
import com.example.billingsystem.entity.Order;

import java.math.BigDecimal;
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
        return CrudUtil.execute("INSERT INTO orders (id, order_number, customer_id, subtotal, discount_amount, total_amount, status, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerId(),
                order.getSubtotal(),
                order.getDiscountAmount(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    @Override
    public List<Order> search(String s) throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public boolean update(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE orders SET order_number=?, customer_id=?, subtotal=?, discount_amount=?, total_amount=?, status=?, updated_at=? WHERE id=?",
                order.getOrderNumber(),
                order.getCustomerId(),
                order.getSubtotal(),
                order.getDiscountAmount(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getUpdatedAt(),
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
                "ORDER BY o.created_at DESC");
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
                "ORDER BY o.created_at DESC", customerId);
        return extractOrdersFromResultSet(resultSet);
    }

    @Override
    public List<Order> searchOrders(String searchTerm) throws SQLException, ClassNotFoundException {
        searchTerm = "%" + searchTerm + "%";
        ResultSet resultSet = CrudUtil.execute("SELECT o.*, c.name as customer_name " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.id " +
                "WHERE o.order_number LIKE ? OR c.name LIKE ? " +
                "ORDER BY o.created_at DESC", searchTerm, searchTerm);
        return extractOrdersFromResultSet(resultSet);
    }

    @Override
    public List<Order> getRecentOrders(int limit) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT o.*, c.name as customer_name " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.id " +
                "ORDER BY o.created_at DESC LIMIT ?", limit);
        return extractOrdersFromResultSet(resultSet);
    }

    @Override
    public int getOrderCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM orders");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public BigDecimal getTotalRevenue() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COALESCE(SUM(total_amount), 0) FROM orders");
        if (resultSet.next()) {
            return resultSet.getBigDecimal(1);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<Object[]> getMonthlySalesData() throws SQLException, ClassNotFoundException {
        List<Object[]> monthlySales = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(
            "SELECT MONTH(created_at) as month, YEAR(created_at) as year, " +
            "SUM(total_amount) as total_sales " +
            "FROM orders " +
            "WHERE created_at >= DATE_SUB(NOW(), INTERVAL 12 MONTH) " +
            "GROUP BY YEAR(created_at), MONTH(created_at) " +
            "ORDER BY year, month"
        );

        while (resultSet.next()) {
            monthlySales.add(new Object[] {
                resultSet.getInt("month"),
                resultSet.getInt("year"),
                resultSet.getBigDecimal("total_sales")
            });
        }

        return monthlySales;
    }

    @Override
    public List<Object[]> getTopSellingItems(int limit) throws SQLException, ClassNotFoundException {
        List<Object[]> topItems = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute(
            "SELECT i.id, i.item_name, i.item_code, SUM(oi.quantity) as total_sold, " +
            "SUM(oi.total_price) as total_revenue " +
            "FROM order_items oi " +
            "JOIN items i ON oi.item_id = i.id " +
            "GROUP BY i.id, i.item_name, i.item_code " +
            "ORDER BY total_sold DESC " +
            "LIMIT ?", limit
        );

        while (resultSet.next()) {
            topItems.add(new Object[] {
                resultSet.getString("id"),
                resultSet.getString("item_name"),
                resultSet.getString("item_code"),
                resultSet.getInt("total_sold"),
                resultSet.getBigDecimal("total_revenue")
            });
        }

        return topItems;
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        return new Order(
                rs.getString("id"),
                rs.getString("order_number"),
                rs.getString("customer_id"),
                rs.getBigDecimal("subtotal"),
                rs.getBigDecimal("discount_amount"),
                rs.getBigDecimal("total_amount"),
                rs.getString("status"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }

    private List<Order> extractOrdersFromResultSet(ResultSet rs) throws SQLException {
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            orders.add(extractOrderFromResultSet(rs));
        }
        return orders;
    }
}
