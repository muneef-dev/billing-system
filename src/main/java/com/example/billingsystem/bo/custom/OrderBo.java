package com.example.billingsystem.bo.custom;

import com.example.billingsystem.bo.SuperBo;
import com.example.billingsystem.dto.OrderDto;
import com.example.billingsystem.dto.OrderItemDto;

import java.sql.SQLException;
import java.util.List;

public interface OrderBo extends SuperBo {
    String createOrder(OrderDto orderDto, List<OrderItemDto> items) throws SQLException, ClassNotFoundException;
    boolean updateOrder(OrderDto orderDto, List<OrderItemDto> items) throws SQLException, ClassNotFoundException;
    boolean deleteOrder(String id) throws SQLException, ClassNotFoundException;
    OrderDto getOrder(String id) throws SQLException, ClassNotFoundException;
    List<OrderDto> getAllOrders() throws SQLException, ClassNotFoundException;
    List<OrderDto> searchOrders(String searchTerm) throws SQLException, ClassNotFoundException;
    List<OrderDto> getOrdersByCustomer(String customerId) throws SQLException, ClassNotFoundException;
    List<OrderDto> getRecentOrders(int limit) throws SQLException, ClassNotFoundException;
    List<OrderItemDto> getOrderItems(String orderId) throws SQLException, ClassNotFoundException;
    String generateOrderNumber() throws SQLException, ClassNotFoundException;
}
