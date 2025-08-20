package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.CustomerBo;
import com.example.billingsystem.bo.custom.ItemBo;
import com.example.billingsystem.bo.custom.OrderBo;
import com.example.billingsystem.dto.OrderDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DashboardServlet.class.getName());

    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoFactory.BoType.CUSTOMER);
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoFactory.BoType.ITEM);
    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoFactory.BoType.ORDER);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get counts from database
            int totalCustomers = customerBo.getCustomerCount();
            int totalItems = itemBo.getItemCount();
            int totalOrders = orderBo.getOrderCount();
            BigDecimal totalRevenue = orderBo.getTotalRevenue();

            // Get recent orders (last 5)
            List<OrderDto> recentOrders = orderBo.getRecentOrders(5);

            // Get monthly sales data for chart
            List<Object[]> monthlySalesData = orderBo.getMonthlySalesData();

            // Get top selling items
            List<Object[]> topSellingItems = orderBo.getTopSellingItems(5);

            // Add date information for the charts
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);

            // Calculate date ranges for the data
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

            // Default to 6 months ago
            Calendar sixMonthsAgo = Calendar.getInstance();
            sixMonthsAgo.add(Calendar.MONTH, -6);
            Date startDate = sixMonthsAgo.getTime();

            // End date is current date
            Date endDate = new Date();

            String dataStartDate = dateFormat.format(startDate);
            String dataEndDate = dateFormat.format(endDate);

            // Add attributes to the request
            request.setAttribute("totalCustomers", totalCustomers);
            request.setAttribute("totalItems", totalItems);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("recentOrders", recentOrders);
            request.setAttribute("monthlySalesData", monthlySalesData);
            request.setAttribute("topSellingItems", topSellingItems);
            request.setAttribute("currentYear", currentYear);
            request.setAttribute("dataStartDate", dataStartDate);
            request.setAttribute("dataEndDate", dataEndDate);

            // Forward to dashboard JSP
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading dashboard data", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading dashboard data");
        }
    }
}
