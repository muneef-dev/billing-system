<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.example.billingsystem.dto.UserDto" %>

<%
    UserDto user = (UserDto) session.getAttribute("user");
    if (user != null) {
        response.sendRedirect("dashboard.jsp");
    } else {
        response.sendRedirect("login.jsp");
    }
%>
