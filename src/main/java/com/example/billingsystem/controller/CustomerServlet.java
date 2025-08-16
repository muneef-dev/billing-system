package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.CustomerBo;
import com.example.billingsystem.dto.CustomerDto;
import com.example.billingsystem.util.KeyGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CustomerServlet", urlPatterns = {"/customers/*"})
public class CustomerServlet extends HttpServlet {
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoFactory.BoType.CUSTOMER);
    private static final Logger logger = Logger.getLogger(CustomerServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                handleListCustomers(request, response);
            } else if (pathInfo.equals("/new")) {
                handleNewCustomerForm(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                handleEditCustomerForm(request, response, pathInfo.substring(6));
            } else if (pathInfo.startsWith("/search")) {
                handleSearchCustomers(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in CustomerServlet doGet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                handleCreateCustomer(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                handleUpdateCustomer(request, response, pathInfo.substring(6));
            } else if (pathInfo.startsWith("/delete/")) {
                handleDeleteCustomer(request, response, pathInfo.substring(8));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in CustomerServlet doPost", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleListCustomers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<CustomerDto> customers = customerBo.getAllCustomers();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);
    }

    private void handleNewCustomerForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/WEB-INF/views/customer/form.jsp").forward(request, response);
    }

    private void handleEditCustomerForm(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        CustomerDto customer = customerBo.getCustomer(id);
        if (customer != null) {
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/views/customer/form.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleSearchCustomers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String term = request.getParameter("term");
        if (term != null && !term.trim().isEmpty()) {
            List<CustomerDto> customers = customerBo.searchCustomers(term.trim());
            request.setAttribute("customers", customers);
            request.setAttribute("searchTerm", term);
        }
        request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);
    }

    private void handleCreateCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(KeyGenerator.generateId());
        customerDto.setAccountNumber(request.getParameter("accountNumber").trim());
        customerDto.setName(request.getParameter("name").trim());
        customerDto.setAddress(request.getParameter("address").trim());
        customerDto.setTelephone(request.getParameter("telephone").trim());

        if (customerBo.createCustomer(customerDto)) {
            response.sendRedirect(request.getContextPath() + "/customers");
        } else {
            request.setAttribute("error", "Failed to create customer");
            request.setAttribute("customer", customerDto);
            handleNewCustomerForm(request, response);
        }
    }

    private void handleUpdateCustomer(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        CustomerDto customerDto = customerBo.getCustomer(id);
        if (customerDto != null) {
            customerDto.setAccountNumber(request.getParameter("accountNumber").trim());
            customerDto.setName(request.getParameter("name").trim());
            customerDto.setAddress(request.getParameter("address").trim());
            customerDto.setTelephone(request.getParameter("telephone").trim());

            if (customerBo.updateCustomer(customerDto)) {
                response.sendRedirect(request.getContextPath() + "/customers");
            } else {
                request.setAttribute("error", "Failed to update customer");
                request.setAttribute("customer", customerDto);
                handleNewCustomerForm(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleDeleteCustomer(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        if (customerBo.deleteCustomer(id)) {
            response.sendRedirect(request.getContextPath() + "/customers");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete customer");
        }
    }
}
