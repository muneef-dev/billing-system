<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Orders" layout="dashboard">
    <jsp:attribute name="content">
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-2xl font-bold text-primary">Orders</h1>
            <a href="${pageContext.request.contextPath}/orders/new" class="btn btn-primary">
                <i class="fas fa-plus mr-2"></i>Create New Order
            </a>
        </div>

        <!-- Search Bar -->
        <div class="card p-4 mb-6">
            <form action="${pageContext.request.contextPath}/orders/search" method="GET" class="flex gap-4">
                <input type="text" name="term" value="${searchTerm}"
                       placeholder="Search orders by order number, customer name..."
                       class="form-input flex-1">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search mr-2"></i>Search
                </button>
            </form>
        </div>

        <!-- Orders Table -->
        <div class="card overflow-hidden">
            <div class="overflow-x-auto">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Order Number</th>
                            <th>Customer</th>
                            <th>Date</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty orders}">
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td class="font-medium text-primary">${order.orderNumber}</td>
                                        <td class="text-secondary">
                                            <!-- Display customer name if available, otherwise show ID -->
                                            ${order.customerName != null ? order.customerName : 'Customer ID: '.concat(order.customerId)}
                                        </td>
                                        <td class="text-secondary">
                                            <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy"/>
                                        </td>
                                        <td class="text-secondary">
                                            <fmt:formatNumber value="${order.totalAmount}" type="currency"/>
                                        </td>
                                        <td>
                                            <span class="badge ${order.status == 'Paid' ? 'badge-success' :
                                                               order.status == 'Pending' ? 'badge-warning' : 'badge-danger'}">
                                                ${order.status}
                                            </span>
                                        </td>
                                        <td class="space-x-2">
                                            <a href="${pageContext.request.contextPath}/orders/view/${order.id}"
                                               class="text-primary hover:text-primary-hover transition-colors">
                                                <i class="fas fa-eye mr-1"></i>View
                                            </a>
                                            <a href="${pageContext.request.contextPath}/orders/edit/${order.id}"
                                               class="text-secondary hover:text-secondary-hover transition-colors">
                                                <i class="fas fa-edit mr-1"></i>Edit
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="text-center text-secondary py-8">
                                        <i class="fas fa-shopping-cart text-4xl mb-4 text-gray-300"></i>
                                        <div>No orders found</div>
                                        <div class="text-sm mt-2">
                                            <a href="${pageContext.request.contextPath}/orders/new" class="text-primary hover:text-primary-hover">
                                                Create your first order
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </jsp:attribute>
</t:layout>
