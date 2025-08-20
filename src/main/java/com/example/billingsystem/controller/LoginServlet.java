package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.UserBo;
import com.example.billingsystem.dto.UserDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", urlPatterns = "/auth/login")
public class LoginServlet extends HttpServlet {
    private final UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            handleLoginForm(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in LoginServlet doGet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            handleLogin(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in LoginServlet doPost", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleLoginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username != null) {
            username = username.trim().toLowerCase();
        }

        if (password != null) {
            password = password.trim();
        }

        logger.log(Level.INFO, "Login attempt for username: {0}", username);

        Optional<UserDto> userOptional = userBo.authenticateUser(username, password);
        if (userOptional.isPresent()) {
            logger.log(Level.INFO, "Login successful for user: {0}", username);

            HttpSession session = request.getSession();
            session.setAttribute("user", userOptional.get());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            logger.log(Level.WARNING, "Login failed for username: {0}", username);

            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
