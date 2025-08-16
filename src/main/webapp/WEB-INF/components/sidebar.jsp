<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="w-64 bg-blue-500 text-white flex flex-col min-h-screen">
    <div class="p-4 text-lg font-bold">Pahana Edu Bookshop</div>
    <nav class="flex-1">
        <ul>
            <li class="p-4 hover:bg-blue-600 ${requestScope['javax.servlet.forward.servlet_path'].startsWith('/dashboard') ? 'bg-blue-600' : ''}">
                <a href="${pageContext.request.contextPath}/dashboard" class="flex items-center">
                    <span>Dashboard</span>
                </a>
            </li>
            <li class="p-4 hover:bg-blue-600 ${requestScope['javax.servlet.forward.servlet_path'].startsWith('/customers') ? 'bg-blue-600' : ''}">
                <a href="${pageContext.request.contextPath}/customers" class="flex items-center">
                    <span>Customers</span>
                </a>
            </li>
            <li class="p-4 hover:bg-blue-600 ${requestScope['javax.servlet.forward.servlet_path'].startsWith('/items') ? 'bg-blue-600' : ''}">
                <a href="${pageContext.request.contextPath}/items" class="flex items-center">
                    <span>Items</span>
                </a>
            </li>
            <li class="p-4 hover:bg-blue-600 ${requestScope['javax.servlet.forward.servlet_path'].startsWith('/orders') ? 'bg-blue-600' : ''}">
                <a href="${pageContext.request.contextPath}/orders" class="flex items-center">
                    <span>Orders</span>
                </a>
            </li>
        </ul>
    </nav>
</div>
