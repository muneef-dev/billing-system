package com.example.billingsystem.controller;

import com.example.billingsystem.dto.UserDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LogoutServlet", urlPatterns = "/auth/logout")
public class LogoutServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LogoutServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            handleLogout(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in LogoutServlet doGet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = "";
            if (session.getAttribute("user") != null) {
                username = ((UserDto) session.getAttribute("user")).getUsername();
            }

            session.invalidate();
            logger.log(Level.INFO, "User logged out successfully: {0}", username);
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
