package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.custom.OrderItemBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.OrderItemDao;
import com.example.billingsystem.dao.custom.impl.OrderItemDaoImpl;
import com.example.billingsystem.dto.OrderItemDto;
import com.example.billingsystem.entity.OrderItem;
import com.example.billingsystem.util.KeyGenerator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderItemBoImpl implements OrderItemBo {

    private final OrderItemDao orderItemDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.ORDER_ITEM);

    @Override
    public boolean createOrderItem(OrderItemDto orderItemDto) throws SQLException, ClassNotFoundException {
        Logger.getLogger(OrderItemBoImpl.class.getName()).log(Level.INFO, "OrderItemBoImpl Executing SQL with ID: " + orderItemDto.getId());
        return orderItemDao.create(new OrderItem(
                orderItemDto.getId(),
                orderItemDto.getOrderId(),
                orderItemDto.getItemId(),
                orderItemDto.getQuantity(),
                orderItemDto.getUnitPrice(),
                orderItemDto.getSubtotal()
        ));
    }

    @Override
    public boolean updateOrderItem(OrderItemDto orderItemDto) throws SQLException, ClassNotFoundException {
        return orderItemDao.update(new OrderItem(
                orderItemDto.getId(),
                orderItemDto.getOrderId(),
                orderItemDto.getItemId(),
                orderItemDto.getQuantity(),
                orderItemDto.getUnitPrice(),
                orderItemDto.getSubtotal()
        ));
    }

    @Override
    public boolean deleteOrderItem(String id) throws SQLException, ClassNotFoundException {
        return orderItemDao.delete(id);
    }

    @Override
    public OrderItemDto getOrderItem(String id) throws SQLException, ClassNotFoundException {
        OrderItem orderItem = orderItemDao.find(id);
        return orderItem != null ? convertToDto(orderItem) : null;
    }

    @Override
    public List<OrderItemDto> getAllOrderItems() throws SQLException, ClassNotFoundException {
        List<OrderItem> orderItems = orderItemDao.loadAll();
        return convertToDtoList(orderItems);
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(String orderId) throws SQLException, ClassNotFoundException {
        List<OrderItem> orderItems = orderItemDao.findByOrderId(orderId);
        return convertToDtoList(orderItems);
    }

    @Override
    public boolean deleteOrderItemsByOrderId(String orderId) throws SQLException, ClassNotFoundException {
        return orderItemDao.deleteByOrderId(orderId);
    }

    @Override
    public boolean createOrderItems(List<OrderItemDto> orderItems) throws SQLException, ClassNotFoundException {
        List<OrderItem> entities = new ArrayList<>();

        for (OrderItemDto dto : orderItems) {
            // Validate DTO
            if (dto == null || dto.getId() == null || dto.getOrderId() == null || dto.getItemId() == null) {
                Logger.getLogger(OrderItemBoImpl.class.getName()).log(Level.SEVERE,
                    "Invalid DTO data received - " + (dto == null ? "DTO is null" :
                    "ID: " + dto.getId() + ", Order ID: " + dto.getOrderId() + ", Item ID: " + dto.getItemId()));
                return false;
            }

            Logger.getLogger(OrderItemBoImpl.class.getName()).log(Level.INFO,
                "OrderItemBoImpl processing DTO - ID: " + dto.getId() +
                ", Order ID: " + dto.getOrderId() +
                ", Item ID: " + dto.getItemId() +
                ", Quantity: " + dto.getQuantity());

            // Create entity with validated data
            OrderItem entity = new OrderItem(
                dto.getId(),
                dto.getOrderId(),
                dto.getItemId(),
                dto.getQuantity(),
                dto.getUnitPrice(),
                dto.getSubtotal()
            );

            Logger.getLogger(OrderItemBoImpl.class.getName()).log(Level.INFO,
                "Created entity - ID: " + entity.getId() +
                ", Order ID: " + entity.getOrderId() +
                ", Item ID: " + entity.getItemId() +
                ", Quantity: " + entity.getQuantity());

            entities.add(entity);
        }

        if (entities.isEmpty()) {
            Logger.getLogger(OrderItemBoImpl.class.getName()).log(Level.SEVERE, "No valid entities created from DTOs");
            return false;
        }

        return orderItemDao.createBatch(entities);
    }

    private OrderItemDto convertToDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getOrderId(),
                orderItem.getItemId(),
                orderItem.getItemName(),
                orderItem.getItemCode(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getSubtotal()
        );
    }

    private List<OrderItemDto> convertToDtoList(List<OrderItem> orderItems) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDtos.add(convertToDto(orderItem));
        }
        return orderItemDtos;
    }

    private OrderItem convertToEntity(OrderItemDto dto) {
        if (dto == null) return null;

        return new OrderItem(
            dto.getId(),
            dto.getOrderId(),
            dto.getItemId(),
            dto.getQuantity(),
            dto.getUnitPrice(),
            dto.getSubtotal()
        );
    }
}
