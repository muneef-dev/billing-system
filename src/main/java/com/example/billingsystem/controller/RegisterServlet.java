package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.UserBo;
import com.example.billingsystem.dto.UserDto;
import com.example.billingsystem.util.KeyGenerator;
import com.example.billingsystem.util.PasswordManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {
    private final UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            handleRegistrationForm(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in RegisterServlet doGet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            handleRegistration(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in RegisterServlet doPost", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleRegistrationForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (username != null) {
            username = username.trim().toLowerCase();
        }

        if (password != null) {
            password = password.trim();
        }

        if (role != null) {
            role = role.trim();
        }

        logger.log(Level.INFO, "Registration attempt for username: {0}", username);

        try {
            UserDto userDto = new UserDto();
            userDto.setId(KeyGenerator.generateId());
            logger.log(Level.INFO, "Generated Id", userDto.getId());
            userDto.setUsername(username);
            userDto.setPassword(PasswordManager.encryptPassword(password));
            userDto.setRole(role);
            userDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            boolean isCreated = userBo.createUser(userDto);
            if (isCreated) {
                logger.log(Level.INFO, "User registered successfully: {0}", username);

                HttpSession session = request.getSession();
                session.setAttribute("user", userDto);
                response.sendRedirect("/dashboard.jsp");
            } else {
                logger.log(Level.WARNING, "Failed to create user: {0}", username);

                request.setAttribute("error", "Failed to create account. Please try again.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating user: " + username, e);
            throw e;
        }
    }
}
