package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.UserBo;
import com.example.billingsystem.dto.UserDto;
import com.example.billingsystem.util.PasswordManager;
import com.example.billingsystem.util.SendMailManager;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile", "/profile/update", "/profile/change-password", "/profile/send-otp", "/profile/verify-otp"})
public class ProfileServlet extends HttpServlet {
    private final UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    private static final Logger logger = Logger.getLogger(ProfileServlet.class.getName());
    private final Random random = new Random();

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
                case "/profile/send-otp":
                    handleSendOTP(request, response);
                    break;
                case "/profile/verify-otp":
                    handleVerifyOTP(request, response);
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
            // Update user information
            currentUser.setEmail(newEmail);
            currentUser.setUsername(newUsername);

            boolean success = userBo.updateUser(currentUser);

            if (success) {
                session.setAttribute("user", currentUser);
                request.setAttribute("success", "Profile updated successfully");
                logger.log(Level.INFO, "Profile updated successfully for user ID: {0}", currentUser.getId());
            } else {
                request.setAttribute("error", "Failed to update profile. Please try again.");
                logger.log(Level.WARNING, "Failed to update profile for user ID: {0}", currentUser.getId());
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating profile for user ID: " + currentUser.getId(), e);
            request.setAttribute("error", "An error occurred while updating your profile");
        }

        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    private void handleSendOTP(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            result.put("success", false);
            result.put("message", "Session expired");
            out.print(gson.toJson(result));
            return;
        }

        UserDto currentUser = (UserDto) session.getAttribute("user");

        // Generate OTP
        String otp = generateOTP();

        // Store OTP in session with timestamp
        session.setAttribute("profile_password_otp", otp);
        session.setAttribute("profile_password_timestamp", System.currentTimeMillis());

        // Send OTP via email
        boolean emailSent = sendPasswordChangeOTP(currentUser.getEmail(), otp, currentUser.getUsername());

        result.put("success", emailSent);
        result.put("message", emailSent ? "Verification code sent to your email" : "Failed to send verification code");

        out.print(gson.toJson(result));
    }

    private void handleVerifyOTP(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            result.put("success", false);
            result.put("message", "Session expired");
            out.print(gson.toJson(result));
            return;
        }

        String enteredOTP = request.getParameter("otp");
        String sessionOTP = (String) session.getAttribute("profile_password_otp");
        Long timestamp = (Long) session.getAttribute("profile_password_timestamp");

        // Validate session data
        if (sessionOTP == null || timestamp == null) {
            result.put("success", false);
            result.put("message", "No verification code found. Please request a new one.");
            out.print(gson.toJson(result));
            return;
        }

        // Check if OTP is expired (5 minutes for profile changes)
        if (System.currentTimeMillis() - timestamp > 300000) {
            session.removeAttribute("profile_password_otp");
            session.removeAttribute("profile_password_timestamp");
            result.put("success", false);
            result.put("message", "Verification code has expired. Please request a new one.");
            out.print(gson.toJson(result));
            return;
        }

        // Validate OTP
        if (!sessionOTP.equals(enteredOTP)) {
            result.put("success", false);
            result.put("message", "Invalid verification code. Please try again.");
            out.print(gson.toJson(result));
            return;
        }

        // OTP verified successfully
        session.setAttribute("profile_password_verified", true);
        result.put("success", true);
        result.put("message", "Verification successful");

        out.print(gson.toJson(result));
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

        // Check if OTP verification was completed
        Boolean otpVerified = (Boolean) session.getAttribute("profile_password_verified");
        if (otpVerified == null || !otpVerified) {
            request.setAttribute("error", "Email verification required. Please verify your email first.");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }

        // Validate passwords
        if (newPassword == null || newPassword.length() < 4) {
            request.setAttribute("error", "New password must be at least 4 characters long");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New passwords do not match");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }

        try {
            boolean success = userBo.updatePassword(currentUser.getId(), currentPassword, newPassword);

            if (success) {
                // Clear OTP verification session data
                session.removeAttribute("profile_password_otp");
                session.removeAttribute("profile_password_timestamp");
                session.removeAttribute("profile_password_verified");

                request.setAttribute("success", "Password changed successfully");
                logger.log(Level.INFO, "Password changed successfully for user ID: {0}", currentUser.getId());
            } else {
                request.setAttribute("error", "Current password is incorrect or failed to update password");
                logger.log(Level.WARNING, "Failed to change password for user ID: {0}", currentUser.getId());
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error changing password for user ID: " + currentUser.getId(), e);
            request.setAttribute("error", "An error occurred while changing your password");
        }

        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    private String generateOTP() {
        return String.format("%06d", random.nextInt(1000000));
    }

    private boolean sendPasswordChangeOTP(String email, String otp, String username) {
        try {
            String subject = "Password Change Verification - Pahana Bookshop";
            String body = String.format(
                "Hello %s,\n\n" +
                "You have requested to change your password for Pahana Bookshop.\n\n" +
                "Your verification code is: %s\n\n" +
                "This code will expire in 5 minutes.\n\n" +
                "If you did not request this change, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Pahana Bookshop Team",
                username != null ? username : "User", otp
            );

            String result = SendMailManager.sendMail(Arrays.asList(subject, body, email, "Email sent successfully"));
            return "Email sent successfully".equals(result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to send password change OTP email", e);
            return false;
        }
    }
}
