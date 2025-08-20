package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.UserBo;
import com.example.billingsystem.dto.UserDto;
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
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (email != null) {
            email = email.trim().toLowerCase();
        }

        if (username != null && !username.trim().isEmpty()) {
            username = username.trim().toLowerCase();
        } else {
            // If username is not provided, generate one from email
            if (email != null && !email.isEmpty()) {
                username = email.split("@")[0]; // Use the part before @ as username
            }
        }

        if (password != null) {
            password = password.trim();
        }

        // Validate role according to schema enum: ('admin', 'staff')
        if (role != null) {
            role = role.trim();
            if (!role.equals("admin") && !role.equals("staff")) {
                role = "staff"; // Default to staff if invalid role
            }
        } else {
            role = "staff"; // Default role from schema
        }

        logger.log(Level.INFO, "Registration attempt for email: {0}, username: {1}, role: {2}", new Object[]{email, username, role});

        // Validate required fields
        if (email == null || email.isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (password == null || password.isEmpty()) {
            request.setAttribute("error", "Password is required");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setUsername(username);
            userDto.setPassword(PasswordManager.encryptPassword(password));
            userDto.setRole(role); // Now properly validated according to schema
            userDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            boolean isCreated = userBo.createUser(userDto);
            if (isCreated) {
                logger.log(Level.INFO, "User registered successfully: {0}", email);

                HttpSession session = request.getSession();
                session.setAttribute("user", userDto);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                logger.log(Level.WARNING, "Failed to create user: {0}", email);

                // Check specific reasons for failure
                UserBo userBoCheck = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
                try {
                    // Check if it's an email duplicate
                    if (userBoCheck.authenticateUser(email, "dummy").isPresent()) {
                        request.setAttribute("error", "Email address is already registered. Please use a different email or try logging in.");
                    } else if (username != null && !username.trim().isEmpty() &&
                              userBoCheck.authenticateUser(username, "dummy").isPresent()) {
                        request.setAttribute("error", "Username is already taken. Please choose a different username.");
                    } else {
                        request.setAttribute("error", "Failed to create account. Please try again.");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "Email address is already registered or username is already taken.");
                }

                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating user: " + email, e);
            throw e;
        }
    }
}
