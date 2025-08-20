package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.UserBo;
import com.example.billingsystem.dto.UserDto;
import com.example.billingsystem.util.SendMailManager;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ForgotPasswordServlet.class.getName());
    private final UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    private final Random random = new Random();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String step = request.getParameter("step");

        try {
            switch (step) {
                case "email":
                    handleEmailStep(request, response);
                    break;
                case "verify":
                    handleVerifyStep(request, response);
                    break;
                case "reset":
                    handleResetStep(request, response);
                    break;
                case "resend":
                    handleResendStep(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid step");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in ForgotPasswordServlet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleEmailStep(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }

        // Check if user exists
        UserDto user = userBo.getUserByEmail(email.trim());
        if (user == null) {
            request.setAttribute("error", "No account found with this email address");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }

        // Generate OTP
        String otp = generateOTP();

        // Store OTP in session with timestamp
        HttpSession session = request.getSession();
        session.setAttribute("forgot_password_otp", otp);
        session.setAttribute("forgot_password_email", email.trim());
        session.setAttribute("forgot_password_timestamp", System.currentTimeMillis());

        // Send OTP via email
        boolean emailSent = sendOTPEmail(email.trim(), otp, user.getUsername());

        if (emailSent) {
            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp?step=verify&email=" + email.trim());
        } else {
            request.setAttribute("error", "Failed to send verification code. Please check your internet connection.");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
        }
    }

    private void handleVerifyStep(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String enteredOTP = request.getParameter("otp");

        HttpSession session = request.getSession();
        String sessionOTP = (String) session.getAttribute("forgot_password_otp");
        String sessionEmail = (String) session.getAttribute("forgot_password_email");
        Long timestamp = (Long) session.getAttribute("forgot_password_timestamp");

        // Validate session data
        if (sessionOTP == null || sessionEmail == null || timestamp == null) {
            request.setAttribute("error", "Session expired. Please start the process again.");
            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp");
            return;
        }

        // Check if OTP is expired (10 minutes)
        if (System.currentTimeMillis() - timestamp > 600000) {
            session.removeAttribute("forgot_password_otp");
            session.removeAttribute("forgot_password_email");
            session.removeAttribute("forgot_password_timestamp");
            request.setAttribute("error", "Verification code has expired. Please request a new one.");
            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp");
            return;
        }

        // Validate email and OTP
        if (!sessionEmail.equals(email) || !sessionOTP.equals(enteredOTP)) {
            request.setAttribute("error", "Invalid verification code. Please try again.");
            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp?step=verify&email=" + email);
            return;
        }

        // OTP verified successfully
        session.setAttribute("forgot_password_verified", true);
        response.sendRedirect(request.getContextPath() + "/forgot-password.jsp?step=reset&email=" + email);
    }

    private void handleResetStep(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        HttpSession session = request.getSession();
        String sessionEmail = (String) session.getAttribute("forgot_password_email");
        Boolean verified = (Boolean) session.getAttribute("forgot_password_verified");

        // Validate session
        if (sessionEmail == null || verified == null || !verified || !sessionEmail.equals(email)) {
            request.setAttribute("error", "Unauthorized access. Please start the process again.");
            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp");
            return;
        }

        // Validate passwords
        if (newPassword == null || newPassword.length() < 4) {
            request.setAttribute("error", "Password must be at least 4 characters long");
            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp?step=reset&email=" + email);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp?step=reset&email=" + email);
            return;
        }

        // Update password
        boolean updated = userBo.updatePassword(email, newPassword);

        if (updated) {
            // Clear session data
            session.removeAttribute("forgot_password_otp");
            session.removeAttribute("forgot_password_email");
            session.removeAttribute("forgot_password_timestamp");
            session.removeAttribute("forgot_password_verified");

            // Send confirmation email
            UserDto user = userBo.getUserByEmail(email);
            if (user != null) {
                sendPasswordResetConfirmation(email, user.getUsername());
            }

            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp?success=true");
        } else {
            request.setAttribute("error", "Failed to reset password. Please try again.");
            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp?step=reset&email=" + email);
        }
    }

    private void handleResendStep(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        String email = request.getParameter("email");
        HttpSession session = request.getSession();
        String sessionEmail = (String) session.getAttribute("forgot_password_email");

        if (sessionEmail == null || !sessionEmail.equals(email)) {
            result.put("success", false);
            result.put("message", "Invalid session");
            out.print(gson.toJson(result));
            return;
        }

        // Generate new OTP
        String otp = generateOTP();

        // Update session
        session.setAttribute("forgot_password_otp", otp);
        session.setAttribute("forgot_password_timestamp", System.currentTimeMillis());

        // Send new OTP
        UserDto user = userBo.getUserByEmail(email);
        boolean emailSent = sendOTPEmail(email, otp, user != null ? user.getUsername() : "User");

        result.put("success", emailSent);
        result.put("message", emailSent ? "Verification code resent successfully" : "Failed to resend verification code");

        out.print(gson.toJson(result));
    }

    private String generateOTP() {
        return String.format("%06d", random.nextInt(1000000));
    }

    private boolean sendOTPEmail(String email, String otp, String username) {
        try {
            String subject = "Password Reset Verification Code - Pahana Bookshop";
            String body = String.format(
                "Hello %s,\n\n" +
                "You have requested to reset your password for Pahana Bookshop.\n\n" +
                "Your verification code is: %s\n\n" +
                "This code will expire in 10 minutes.\n\n" +
                "If you did not request this password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Pahana Bookshop Team",
                username, otp
            );

            String result = SendMailManager.sendMail(Arrays.asList(subject, body, email, "Email sent successfully"));
            return "Email sent successfully".equals(result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to send OTP email", e);
            return false;
        }
    }

    private boolean sendPasswordResetConfirmation(String email, String username) {
        try {
            String subject = "Password Reset Successful - Pahana Bookshop";
            String body = String.format(
                "Hello %s,\n\n" +
                "Your password has been successfully reset for Pahana Bookshop.\n\n" +
                "If you did not make this change, please contact our support team immediately.\n\n" +
                "Best regards,\n" +
                "Pahana Bookshop Team",
                username
            );

            String result = SendMailManager.sendMail(Arrays.asList(subject, body, email, "Email sent successfully"));
            return "Email sent successfully".equals(result);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to send password reset confirmation email", e);
            return false;
        }
    }
}
