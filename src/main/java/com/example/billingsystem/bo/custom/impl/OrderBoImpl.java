package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.OrderBo;
import com.example.billingsystem.bo.custom.OrderItemBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.OrderDao;
import com.example.billingsystem.dao.custom.ItemDao;
import com.example.billingsystem.dao.custom.OrderItemDao;
import com.example.billingsystem.dto.OrderDto;
import com.example.billingsystem.dto.OrderItemDto;
import com.example.billingsystem.entity.Order;
import com.example.billingsystem.entity.OrderItem;
import com.example.billingsystem.util.KeyGenerator;
import com.example.billingsystem.util.GeneratorUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderBoImpl implements OrderBo {
    private final OrderDao orderDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.ORDER);
    private final ItemDao itemDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.ITEM);
    private final OrderItemBo orderItemBo = BoFactory.getInstance().getBo(BoFactory.BoType.ORDER_ITEM);
    private final OrderItemDao orderItemDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.ORDER_ITEM);

    @Override
    public String createOrder(OrderDto orderDto, List<OrderItemDto> items) throws SQLException, ClassNotFoundException {
        // Generate ID if not provided
        if (orderDto.getId() == null || orderDto.getId().trim().isEmpty()) {
            orderDto.setId(KeyGenerator.generateId());
        }

        // Generate order number if not provided
        if (orderDto.getOrderNumber() == null || orderDto.getOrderNumber().trim().isEmpty()) {
            orderDto.setOrderNumber(GeneratorUtil.generateOrderNumber());
        }

        // Validate order status enum values according to schema: ('Pending', 'Paid', 'Cancelled')
        if (orderDto.getStatus() != null) {
            String status = orderDto.getStatus();
            if (!status.equals("Pending") && !status.equals("Paid") && !status.equals("Cancelled")) {
                orderDto.setStatus("Pending"); // Default to Pending if invalid status
            }
        } else {
            orderDto.setStatus("Pending"); // Default status from schema
        }

        Logger.getLogger(OrderBoImpl.class.getName()).log(Level.INFO, "OrderBoImpl creating order with ID: " + orderDto.getId());

        // Create order
        Order order = new Order(
                orderDto.getId(),
                orderDto.getOrderNumber(),
                orderDto.getCustomerId(),
                orderDto.getSubtotal(),
                orderDto.getDiscountAmount(),
                orderDto.getTotalAmount(),
                orderDto.getStatus(),
                new java.sql.Timestamp(System.currentTimeMillis()),
                new java.sql.Timestamp(System.currentTimeMillis())
        );

        boolean orderCreated = orderDao.create(order);
        if (!orderCreated) {
            throw new SQLException("Failed to create order");
        }

        // Update stock and verify order items
        for (OrderItemDto item : items) {
            Logger.getLogger(OrderBoImpl.class.getName()).log(Level.INFO,
                "Processing order item in OrderBoImpl - ID: " + item.getId() +
                ", Order ID: " + item.getOrderId() +
                ", Item ID: " + item.getItemId());

            // Ensure the order ID is set correctly
            item.setOrderId(order.getId());

            // Update stock
            itemDao.updateStock(item.getItemId(), -item.getQuantity());
        }

        // Create order items using OrderItemBo
        boolean itemsCreated = orderItemBo.createOrderItems(items);
        if (!itemsCreated) {
            // Rollback stock updates if order items creation fails
            for (OrderItemDto item : items) {
                itemDao.updateStock(item.getItemId(), item.getQuantity());
            }
            throw new SQLException("Failed to create order items");
        }

        return orderDto.getOrderNumber();
    }

    @Override
    public boolean updateOrder(OrderDto orderDto, List<OrderItemDto> items) throws SQLException, ClassNotFoundException {
        // First, get existing order items to restore stock
        List<OrderItemDto> existingItems = getOrderItems(orderDto.getId());
        for (OrderItemDto item : existingItems) {
            itemDao.updateStock(item.getItemId(), item.getQuantity());
        }

        // Update order
        Order order = new Order(
                orderDto.getId(),
                orderDto.getOrderNumber(),
                orderDto.getCustomerId(),
                orderDto.getSubtotal(),
                orderDto.getDiscountAmount(),
                orderDto.getTotalAmount(),
                orderDto.getStatus(),
                new java.sql.Timestamp(System.currentTimeMillis()),
                new java.sql.Timestamp(System.currentTimeMillis())
        );

        boolean orderUpdated = orderDao.update(order);
        if (!orderUpdated) {
            throw new SQLException("Failed to update order");
        }

        // Delete existing items
        orderItemDao.deleteByOrderId(order.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDto item : items) {
            OrderItem orderItem = new OrderItem(
                    item.getId(),
                    order.getId(),
                    item.getItemId(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getTotalPrice()
            );
            orderItems.add(orderItem);
        }

        // Create new order items and update stock
        for (OrderItemDto item : items) {
            // Update stock
            itemDao.updateStock(item.getItemId(), -item.getQuantity());
        }

        return orderItemDao.createBatch(orderItems);
    }

    @Override
    public boolean deleteOrder(String id) throws SQLException, ClassNotFoundException {
        // First, get order items to restore stock
        List<OrderItemDto> items = getOrderItems(id);
        for (OrderItemDto item : items) {
            itemDao.updateStock(item.getItemId(), item.getQuantity());
        }

        // Delete order items
        orderItemDao.deleteByOrderId(id);

        // Delete order
        return orderDao.delete(id);
    }

    @Override
    public OrderDto getOrder(String id) throws SQLException, ClassNotFoundException {
        Order order = orderDao.find(id);
        return order != null ? convertToDto(order) : null;
    }

    @Override
    public List<OrderDto> getAllOrders() throws SQLException, ClassNotFoundException {
        List<Order> orders = orderDao.loadAll();
        return convertToDtoList(orders);
    }

    @Override
    public List<OrderDto> searchOrders(String searchTerm) throws SQLException, ClassNotFoundException {
        List<Order> orders = orderDao.searchOrders(searchTerm);
        return convertToDtoList(orders);
    }

    @Override
    public List<OrderDto> getOrdersByCustomer(String customerId) throws SQLException, ClassNotFoundException {
        List<Order> orders = orderDao.findByCustomerId(customerId);
        return convertToDtoList(orders);
    }

    @Override
    public int getOrderCount() throws SQLException, ClassNotFoundException {
        return orderDao.getOrderCount();
    }

    @Override
    public BigDecimal getTotalRevenue() throws SQLException, ClassNotFoundException {
        return orderDao.getTotalRevenue();
    }

    @Override
    public List<OrderDto> getRecentOrders(int limit) throws SQLException, ClassNotFoundException {
        List<Order> orders = orderDao.getRecentOrders(limit);
        return convertToDtoList(orders);
    }

    @Override
    public List<Object[]> getMonthlySalesData() throws SQLException, ClassNotFoundException {
        return orderDao.getMonthlySalesData();
    }

    @Override
    public List<Object[]> getTopSellingItems(int limit) throws SQLException, ClassNotFoundException {
        return orderDao.getTopSellingItems(limit);
    }

    public List<OrderItemDto> getOrderItems(String orderId) throws SQLException, ClassNotFoundException {
        List<OrderItem> items = orderItemDao.findByOrderId(orderId);
        return convertToItemDtoList(items);
    }

    @Override
    public String generateOrderNumber() throws SQLException, ClassNotFoundException {
        return orderDao.generateOrderNumber();
    }

    private OrderDto convertToDto(Order order) {
        return new OrderDto(
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

    private List<OrderDto> convertToDtoList(List<Order> orders) {
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(convertToDto(order));
        }
        return orderDtos;
    }

    private OrderItemDto convertToItemDto(OrderItem item) {
        return new OrderItemDto(
                item.getId(),
                item.getOrderId(),
                item.getItemId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }

    private List<OrderItemDto> convertToItemDtoList(List<OrderItem> items) {
        List<OrderItemDto> itemDtos = new ArrayList<>();
        for (OrderItem item : items) {
            itemDtos.add(convertToItemDto(item));
        }
        return itemDtos;
    }
}
