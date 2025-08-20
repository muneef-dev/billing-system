<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="sidebar">
    <div class="p-6">
        <h1 class="text-xl font-bold text-white mb-2">Pahana Edu</h1>
        <p class="text-sm text-gray-300">Bookshop Management</p>
    </div>

    <nav class="flex-1">
        <ul class="sidebar-nav space-y-2 px-4">
            <li class="rounded-lg hover:bg-sidebar-hover ${requestScope['javax.servlet.forward.servlet_path'].startsWith('/dashboard') ? 'active bg-sidebar-active' : ''}">
                <a href="${pageContext.request.contextPath}/dashboard" class="flex items-center p-3 text-white rounded-lg transition-all">
                    <i class="fas fa-tachometer-alt mr-3 text-lg"></i>
                    <span class="font-medium">Dashboard</span>
                </a>
            </li>
            <li class="rounded-lg hover:bg-sidebar-hover ${requestScope['javax.servlet.forward.servlet_path'].startsWith('/customers') ? 'active bg-sidebar-active' : ''}">
                <a href="${pageContext.request.contextPath}/customers" class="flex items-center p-3 text-white rounded-lg transition-all">
                    <i class="fas fa-users mr-3 text-lg"></i>
                    <span class="font-medium">Customers</span>
                </a>
            </li>
            <li class="rounded-lg hover:bg-sidebar-hover ${requestScope['javax.servlet.forward.servlet_path'].startsWith('/items') ? 'active bg-sidebar-active' : ''}">
                <a href="${pageContext.request.contextPath}/items" class="flex items-center p-3 text-white rounded-lg transition-all">
                    <i class="fas fa-book mr-3 text-lg"></i>
                    <span class="font-medium">Items</span>
                </a>
            </li>
            <li class="rounded-lg hover:bg-sidebar-hover ${requestScope['javax.servlet.forward.servlet_path'].startsWith('/orders') ? 'active bg-sidebar-active' : ''}">
                <a href="${pageContext.request.contextPath}/orders" class="flex items-center p-3 text-white rounded-lg transition-all">
                    <i class="fas fa-shopping-cart mr-3 text-lg"></i>
                    <span class="font-medium">Orders</span>
                </a>
            </li>
        </ul>
    </nav>

    <div class="p-4 border-t border-gray-600">
        <div class="flex items-center text-gray-300 text-sm">
            <i class="fas fa-info-circle mr-2"></i>
            <span>Version 1.0.0</span>
        </div>
    </div>
</div>
