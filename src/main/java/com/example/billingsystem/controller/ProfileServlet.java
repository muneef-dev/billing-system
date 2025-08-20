package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.UserBo;
import com.example.billingsystem.dto.UserDto;
import com.example.billingsystem.util.PasswordManager;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile", "/profile/update", "/profile/change-password"})
public class ProfileServlet extends HttpServlet {
    private final UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    private static final Logger logger = Logger.getLogger(ProfileServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String path = request.getServletPath();

            if ("/profile".equals(path)) {
                handleProfileView(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in ProfileServlet doGet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String path = request.getServletPath();

            switch (path) {
                case "/profile/update":
                    handleProfileUpdate(request, response);
                    break;
                case "/profile/change-password":
                    handlePasswordChange(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in ProfileServlet doPost", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleProfileView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        UserDto currentUser = (UserDto) session.getAttribute("user");
        String newEmail = request.getParameter("email");
        String newUsername = request.getParameter("username");

        if (newEmail != null) {
            newEmail = newEmail.trim().toLowerCase();
        }

        if (newUsername != null && !newUsername.trim().isEmpty()) {
            newUsername = newUsername.trim().toLowerCase();
        }

        logger.log(Level.INFO, "Profile update attempt for user ID: {0}", currentUser.getId());

        // Validate required fields
        if (newEmail == null || newEmail.isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }

        try {
            // Check if the new email is different and already exists
            if (!newEmail.equals(currentUser.getEmail())) {
                // Check if email already exists for other users
                if (userBo.authenticateUser(newEmail, "dummy").isPresent()) {
                    request.setAttribute("error", "Email address is already registered by another user.");
                    request.getRequestDispatcher("/profile.jsp").forward(request, response);
                    return;
                }
            }

            // Check if the new username is different and already exists
            if (newUsername != null && !newUsername.equals(currentUser.getUsername())) {
                if (userBo.authenticateUser(newUsername, "dummy").isPresent()) {
                    request.setAttribute("error", "Username is already taken by another user.");
                    request.getRequestDispatcher("/profile.jsp").forward(request, response);
                    return;
                }
            }

            // Update user information
            currentUser.setEmail(newEmail);
            currentUser.setUsername(newUsername);

            boolean isUpdated = userBo.updateUser(currentUser);

            if (isUpdated) {
                logger.log(Level.INFO, "Profile updated successfully for user: {0}", currentUser.getId());

                // Update session with new user data
                session.setAttribute("user", currentUser);

                request.setAttribute("success", "Profile updated successfully!");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
            } else {
                logger.log(Level.WARNING, "Failed to update profile for user: {0}", currentUser.getId());
                request.setAttribute("error", "Failed to update profile. Please try again.");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating profile for user: " + currentUser.getId(), e);
            request.setAttribute("error", "An error occurred while updating your profile.");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }
    }

    private void handlePasswordChange(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        UserDto currentUser = (UserDto) session.getAttribute("user");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (currentPassword != null) {
            currentPassword = currentPassword.trim();
        }

        if (newPassword != null) {
            newPassword = newPassword.trim();
        }

        if (confirmPassword != null) {
            confirmPassword = confirmPassword.trim();
        }

        logger.log(Level.INFO, "Password change attempt for user ID: {0}", currentUser.getId());

        // Validate inputs
        if (currentPassword == null || currentPassword.isEmpty()) {
            request.setAttribute("error", "Current password is required");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            request.setAttribute("error", "New password is required");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New passwords do not match");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 4) {
            request.setAttribute("error", "Password must be at least 4 characters long");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }

        try {
            // Verify current password
            if (!PasswordManager.checkPassword(currentPassword, currentUser.getPassword())) {
                request.setAttribute("error", "Current password is incorrect");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }

            // Update password
            String encryptedNewPassword = PasswordManager.encryptPassword(newPassword);
            currentUser.setPassword(encryptedNewPassword);

            boolean isUpdated = userBo.updateUser(currentUser);

            if (isUpdated) {
                logger.log(Level.INFO, "Password changed successfully for user: {0}", currentUser.getId());

                // Update session with new user data
                session.setAttribute("user", currentUser);

                request.setAttribute("success", "Password changed successfully!");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
            } else {
                logger.log(Level.WARNING, "Failed to change password for user: {0}", currentUser.getId());
                request.setAttribute("error", "Failed to change password. Please try again.");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error changing password for user: " + currentUser.getId(), e);
            request.setAttribute("error", "An error occurred while changing your password.");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }
    }
}
