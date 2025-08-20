package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.OrderBo;
import com.example.billingsystem.bo.custom.CustomerBo;
import com.example.billingsystem.bo.custom.ItemBo;
import com.example.billingsystem.dto.OrderDto;
import com.example.billingsystem.dto.OrderItemDto;
import com.example.billingsystem.dto.CustomerDto;
import com.example.billingsystem.dto.ItemDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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

        // Populate customer names for orders
        for (OrderDto order : orders) {
            try {
                if (order.getCustomerId() != null) {
                    CustomerDto customer = customerBo.getCustomer(order.getCustomerId());
                    if (customer != null) {
                        order.setCustomerName(customer.getName());
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error fetching customer name for order " + order.getId(), e);
            }
        }

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
            // Populate customer name for the order
            try {
                if (order.getCustomerId() != null) {
                    CustomerDto customer = customerBo.getCustomer(order.getCustomerId());
                    if (customer != null) {
                        order.setCustomerName(customer.getName());
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error fetching customer name for order " + order.getId(), e);
            }

            List<OrderItemDto> orderItems = orderBo.getOrderItems(id);

            // Populate item names for order items
            for (OrderItemDto orderItem : orderItems) {
                try {
                    if (orderItem.getItemId() != null) {
                        ItemDto item = itemBo.getItem(orderItem.getItemId());
                        if (item != null) {
                            orderItem.setItemName(item.getItemName());
                        }
                    }
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error fetching item name for order item " + orderItem.getItemId(), e);
                }
            }

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

            // Populate customer names for search results
            for (OrderDto order : orders) {
                try {
                    if (order.getCustomerId() != null) {
                        CustomerDto customer = customerBo.getCustomer(order.getCustomerId());
                        if (customer != null) {
                            order.setCustomerName(customer.getName());
                        }
                    }
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error fetching customer name for order " + order.getId(), e);
                }
            }

            request.setAttribute("orders", orders);
            request.setAttribute("searchTerm", term);
        }
        request.getRequestDispatcher("/WEB-INF/views/order/list.jsp").forward(request, response);
    }

    private void handleCreateOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(request.getParameter("customerId"));

        // Fix order status to match schema enum: ('Pending', 'Paid', 'Cancelled')
        orderDto.setStatus("Pending"); // Schema requires capitalized 'Pending', not 'pending'

        List<OrderItemDto> orderItems = processOrderItems(request);
        if (orderItems.isEmpty()) {
            request.setAttribute("error", "Order must contain at least one item");
            handleNewOrderForm(request, response);
            return;
        }

        BigDecimal subtotal = calculateSubtotal(orderItems);
        BigDecimal discountAmount = calculateDiscount(request, subtotal);
        BigDecimal totalAmount = subtotal.subtract(discountAmount);

        orderDto.setSubtotal(subtotal);
        orderDto.setDiscountAmount(discountAmount);
        orderDto.setTotalAmount(totalAmount);

        String orderNumber = orderBo.createOrder(orderDto, orderItems);
        if (orderNumber != null) {
            response.sendRedirect(request.getContextPath() + "/orders");
        } else {
            request.setAttribute("error", "Failed to create order");
            handleNewOrderForm(request, response);
        }
    }

    private void handleUpdateOrder(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        OrderDto orderDto = orderBo.getOrder(id);
        if (orderDto != null) {
            orderDto.setCustomerId(request.getParameter("customerId"));

            // Validate status parameter against schema enum values
            String status = request.getParameter("status");
            if (status != null) {
                status = status.trim();
                // Ensure status matches schema enum: ('Pending', 'Paid', 'Cancelled')
                if (status.equals("Pending") || status.equals("Paid") || status.equals("Cancelled")) {
                    orderDto.setStatus(status);
                } else {
                    // Convert common variations to proper schema values
                    if (status.equalsIgnoreCase("pending")) {
                        orderDto.setStatus("Pending");
                    } else if (status.equalsIgnoreCase("paid") || status.equalsIgnoreCase("completed")) {
                        orderDto.setStatus("Paid");
                    } else if (status.equalsIgnoreCase("cancelled") || status.equalsIgnoreCase("canceled")) {
                        orderDto.setStatus("Cancelled");
                    } else {
                        orderDto.setStatus("Pending"); // Default fallback
                    }
                }
            }

            List<OrderItemDto> orderItems = processOrderItems(request);
            if (orderItems.isEmpty()) {
                request.setAttribute("error", "Order must contain at least one item");
                handleEditOrderForm(request, response, id);
                return;
            }

            BigDecimal subtotal = calculateSubtotal(orderItems);
            BigDecimal discountAmount = calculateDiscount(request, subtotal);
            BigDecimal totalAmount = subtotal.subtract(discountAmount);

            orderDto.setSubtotal(subtotal);
            orderDto.setDiscountAmount(discountAmount);
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

    private List<OrderItemDto> processOrderItems(HttpServletRequest request) {
        List<OrderItemDto> orderItems = new ArrayList<>();
        String[] itemIds = request.getParameterValues("itemId[]");
        String[] quantities = request.getParameterValues("quantity[]");
        String[] unitPrices = request.getParameterValues("unitPrice[]");

        if (itemIds != null && quantities != null && unitPrices != null) {
            for (int i = 0; i < itemIds.length; i++) {
                if (i < quantities.length && i < unitPrices.length &&
                    !itemIds[i].isEmpty() && !quantities[i].isEmpty() && !unitPrices[i].isEmpty()) {

                    OrderItemDto item = new OrderItemDto();
                    item.setItemId(itemIds[i]);
                    item.setQuantity(Integer.parseInt(quantities[i]));
                    item.setUnitPrice(new BigDecimal(unitPrices[i]));
                    item.setTotalPrice(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));

                    orderItems.add(item);
                }
            }
        }
        return orderItems;
    }

    private BigDecimal calculateSubtotal(List<OrderItemDto> orderItems) {
        return orderItems.stream()
                .map(OrderItemDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDiscount(HttpServletRequest request, BigDecimal subtotal) {
        String discountStr = request.getParameter("discountAmount");
        if (discountStr != null && !discountStr.trim().isEmpty()) {
            try {
                return new BigDecimal(discountStr.trim());
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "Invalid discount amount: " + discountStr);
            }
        }
        return BigDecimal.ZERO;
    }
}
