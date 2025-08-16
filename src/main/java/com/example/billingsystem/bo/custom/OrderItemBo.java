package com.example.billingsystem.bo.custom;

import com.example.billingsystem.bo.SuperBo;
import com.example.billingsystem.dto.OrderItemDto;

import java.sql.SQLException;
import java.util.List;

public interface OrderItemBo extends SuperBo {
    boolean createOrderItem(OrderItemDto orderItemDto) throws SQLException, ClassNotFoundException;
    boolean updateOrderItem(OrderItemDto orderItemDto) throws SQLException, ClassNotFoundException;
    boolean deleteOrderItem(String id) throws SQLException, ClassNotFoundException;
    OrderItemDto getOrderItem(String id) throws SQLException, ClassNotFoundException;
    List<OrderItemDto> getAllOrderItems() throws SQLException, ClassNotFoundException;
    List<OrderItemDto> getOrderItemsByOrderId(String orderId) throws SQLException, ClassNotFoundException;
    boolean deleteOrderItemsByOrderId(String orderId) throws SQLException, ClassNotFoundException;
    boolean createOrderItems(List<OrderItemDto> orderItems) throws SQLException, ClassNotFoundException;
}
