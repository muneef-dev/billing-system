package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.OrderBo;
import com.example.billingsystem.bo.custom.CustomerBo;
import com.example.billingsystem.bo.custom.ItemBo;
import com.example.billingsystem.dto.OrderDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {
    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoFactory.BoType.ORDER);
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoFactory.BoType.CUSTOMER);
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoFactory.BoType.ITEM);
    private static final Logger logger = Logger.getLogger(DashboardServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            handleDashboard(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in DashboardServlet doGet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // Get recent orders for display
            List<OrderDto> recentOrders = orderBo.getRecentOrders(5);
            request.setAttribute("recentOrders", recentOrders);

            // Get total counts and statistics
            request.setAttribute("totalOrders", orderBo.getAllOrders().size());
            request.setAttribute("totalCustomers", customerBo.getAllCustomers().size());
            request.setAttribute("totalItems", itemBo.getAllItems().size());

            // Get total revenue (sum of all completed orders)
            List<OrderDto> allOrders = orderBo.getAllOrders();
            double totalRevenue = allOrders.stream()
                .filter(order -> "completed".equals(order.getStatus()))
                .mapToDouble(order -> order.getTotalAmount().doubleValue())
                .sum();
            request.setAttribute("totalRevenue", totalRevenue);

            logger.log(Level.INFO, "Dashboard data loaded successfully");
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading dashboard data", e);
            throw e;
        }
    }
}
