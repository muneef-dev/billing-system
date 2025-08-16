package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.OrderBo;
import com.example.billingsystem.bo.custom.CustomerBo;
import com.example.billingsystem.bo.custom.ItemBo;
import com.example.billingsystem.dto.OrderDto;
import com.example.billingsystem.dto.OrderItemDto;
import com.example.billingsystem.dto.CustomerDto;
import com.example.billingsystem.dto.ItemDto;
import com.example.billingsystem.util.KeyGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "OrderServlet", urlPatterns = {"/orders/*"})
public class OrderServlet extends HttpServlet {
    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoFactory.BoType.ORDER);
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoFactory.BoType.CUSTOMER);
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoFactory.BoType.ITEM);
    private static final Logger logger = Logger.getLogger(OrderServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                handleListOrders(request, response);
            } else if (pathInfo.equals("/new")) {
                handleNewOrderForm(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                handleEditOrderForm(request, response, pathInfo.substring(6));
            } else if (pathInfo.startsWith("/view/")) {
                handleViewOrder(request, response, pathInfo.substring(6));
            } else if (pathInfo.startsWith("/search")) {
                handleSearchOrders(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in OrderServlet doGet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                handleCreateOrder(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                handleUpdateOrder(request, response, pathInfo.substring(6));
            } else if (pathInfo.startsWith("/delete/")) {
                handleDeleteOrder(request, response, pathInfo.substring(8));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in OrderServlet doPost", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleListOrders(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<OrderDto> orders = orderBo.getAllOrders();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/order/list.jsp").forward(request, response);
    }

    private void handleNewOrderForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<CustomerDto> customers = customerBo.getAllCustomers();
        List<ItemDto> items = itemBo.getAllItems();
        request.setAttribute("customers", customers);
        request.setAttribute("items", items);
        request.getRequestDispatcher("/WEB-INF/views/order/form.jsp").forward(request, response);
    }

    private void handleEditOrderForm(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        OrderDto order = orderBo.getOrder(id);
        if (order != null) {
            List<CustomerDto> customers = customerBo.getAllCustomers();
            List<ItemDto> items = itemBo.getAllItems();
            List<OrderItemDto> orderItems = orderBo.getOrderItems(id);
            request.setAttribute("order", order);
            request.setAttribute("customers", customers);
            request.setAttribute("items", items);
            request.setAttribute("orderItems", orderItems);
            request.getRequestDispatcher("/WEB-INF/views/order/form.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleViewOrder(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        OrderDto order = orderBo.getOrder(id);
        if (order != null) {
            List<OrderItemDto> orderItems = orderBo.getOrderItems(id);
            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);
            request.getRequestDispatcher("/WEB-INF/views/order/view.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleSearchOrders(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String term = request.getParameter("term");
        if (term != null && !term.trim().isEmpty()) {
            List<OrderDto> orders = orderBo.searchOrders(term.trim());
            request.setAttribute("orders", orders);
            request.setAttribute("searchTerm", term);
        }
        request.getRequestDispatcher("/WEB-INF/views/order/list.jsp").forward(request, response);
    }

    private void handleCreateOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(KeyGenerator.generateId());
        orderDto.setCustomerId(request.getParameter("customerId"));
        logger.log(Level.INFO, "Generated Id", orderDto.getId());
        orderDto.setStatus("pending");
        orderDto.setOrderDate(new Timestamp(System.currentTimeMillis()));

        List<OrderItemDto> orderItems = processOrderItems(request, orderDto.getId());
        if (orderItems.isEmpty()) {
            request.setAttribute("error", "Order must contain at least one item");
            handleNewOrderForm(request, response);
            return;
        }

        BigDecimal totalAmount = calculateTotalAmount(orderItems);
        orderDto.setTotalAmount(totalAmount);

        String orderNumber = orderBo.createOrder(orderDto, orderItems);
        if (orderNumber != null) {
            response.sendRedirect(request.getContextPath() + "/orders/view/" + orderDto.getId());
        } else {
            request.setAttribute("error", "Failed to create order");
            handleNewOrderForm(request, response);
        }
    }

    private void handleUpdateOrder(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        OrderDto orderDto = orderBo.getOrder(id);
        if (orderDto != null) {
            orderDto.setCustomerId(request.getParameter("customerId"));
            orderDto.setStatus(request.getParameter("status"));

            List<OrderItemDto> orderItems = processOrderItems(request, orderDto.getId());
            if (orderItems.isEmpty()) {
                request.setAttribute("error", "Order must contain at least one item");
                handleEditOrderForm(request, response, id);
                return;
            }

            BigDecimal totalAmount = calculateTotalAmount(orderItems);
            orderDto.setTotalAmount(totalAmount);

            if (orderBo.updateOrder(orderDto, orderItems)) {
                response.sendRedirect(request.getContextPath() + "/orders/view/" + id);
            } else {
                request.setAttribute("error", "Failed to update order");
                handleEditOrderForm(request, response, id);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleDeleteOrder(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        if (orderBo.deleteOrder(id)) {
            response.sendRedirect(request.getContextPath() + "/orders");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete order");
        }
    }

    private List<OrderItemDto> processOrderItems(HttpServletRequest request, String orderId) {
        List<OrderItemDto> orderItems = new ArrayList<>();
        String[] itemIds = request.getParameterValues("itemId[]");
        String[] quantities = request.getParameterValues("quantity[]");
        String[] prices = request.getParameterValues("price[]");

        logger.log(Level.INFO, "Processing order items - Order ID: " + orderId);
        logger.log(Level.INFO, "Item IDs from form: " + (itemIds != null ? String.join(", ", itemIds) : "null"));

        if (itemIds != null && quantities != null && prices != null) {
            for (int i = 0; i < itemIds.length; i++) {
                if (!itemIds[i].isEmpty() && !quantities[i].isEmpty() && !prices[i].isEmpty()) {
                    OrderItemDto item = new OrderItemDto();
                    String orderItemId = KeyGenerator.generateId(); // Generate ID only for the order item entry
                    logger.log(Level.INFO, "Creating order item - Order Item ID: " + orderItemId +
                        ", Order ID: " + orderId +
                        ", Item ID (from form): " + itemIds[i]);

                    item.setId(orderItemId);
                    item.setOrderId(orderId);
                    item.setItemId(itemIds[i]); // Use the item ID from the form directly
                    item.setQuantity(Integer.parseInt(quantities[i].trim()));
                    item.setUnitPrice(new BigDecimal(prices[i].trim()));
                    item.setSubtotal(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
                    orderItems.add(item);

                    logger.log(Level.INFO, "Created order item - Order Item ID: " + item.getId() +
                        ", Order ID: " + item.getOrderId() +
                        ", Item ID: " + item.getItemId() +
                        ", Quantity: " + item.getQuantity());
                }
            }
        }
        return orderItems;
    }

    private BigDecimal calculateTotalAmount(List<OrderItemDto> orderItems) {
        return orderItems.stream()
                .map(OrderItemDto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
