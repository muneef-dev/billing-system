<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Dashboard" layout="dashboard">
    <jsp:attribute name="content">
        <div class="mb-8">
            <h1 class="text-3xl font-bold text-primary">Welcome back, ${sessionScope.user.username}!</h1>
            <p class="mt-1 text-sm text-secondary">Here's what's happening in your bookshop today.</p>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <!-- Total Orders Card -->
            <div class="card p-6">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-primary text-white">
                        <i class="fas fa-shopping-bag text-xl"></i>
                    </div>
                    <div class="ml-4">
                        <h2 class="text-sm font-medium text-secondary">Total Orders</h2>
                        <p class="text-2xl font-semibold text-primary">${totalOrders != null ? totalOrders : 0}</p>
                    </div>
                </div>
            </div>

            <!-- Total Items Card -->
            <div class="card p-6">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-success text-white">
                        <i class="fas fa-book text-xl"></i>
                    </div>
                    <div class="ml-4">
                        <h2 class="text-sm font-medium text-secondary">Total Items</h2>
                        <p class="text-2xl font-semibold text-primary">${totalItems != null ? totalItems : 0}</p>
                    </div>
                </div>
            </div>

            <!-- Total Customers Card -->
            <div class="card p-6">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-warning text-white">
                        <i class="fas fa-users text-xl"></i>
                    </div>
                    <div class="ml-4">
                        <h2 class="text-sm font-medium text-secondary">Total Customers</h2>
                        <p class="text-2xl font-semibold text-primary">${totalCustomers != null ? totalCustomers : 0}</p>
                    </div>
                </div>
            </div>

            <!-- Total Revenue Card -->
            <div class="card p-6">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-success text-white">
                        <i class="fas fa-dollar-sign text-xl"></i>
                    </div>
                    <div class="ml-4">
                        <h2 class="text-sm font-medium text-secondary">Total Revenue</h2>
                        <p class="text-2xl font-semibold text-primary">
                            <fmt:formatNumber value="${totalRevenue != null ? totalRevenue : 0}" type="currency"/>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="card mb-8">
            <div class="px-6 py-4 border-b border-color">
                <h3 class="text-lg font-medium text-primary">Quick Actions</h3>
            </div>
            <div class="p-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                <a href="${pageContext.request.contextPath}/customers/new"
                   class="quick-action quick-action-blue">
                    <i class="fas fa-user-plus mr-3"></i>
                    <span class="font-medium">New Customer</span>
                </a>
                <a href="${pageContext.request.contextPath}/items/new"
                   class="quick-action quick-action-green">
                    <i class="fas fa-plus mr-3"></i>
                    <span class="font-medium">New Item</span>
                </a>
                <a href="${pageContext.request.contextPath}/orders/new"
                   class="quick-action quick-action-yellow">
                    <i class="fas fa-shopping-cart mr-3"></i>
                    <span class="font-medium">New Order</span>
                </a>
                <a href="${pageContext.request.contextPath}/reports"
                   class="quick-action quick-action-blue">
                    <i class="fas fa-chart-bar mr-3"></i>
                    <span class="font-medium">View Reports</span>
                </a>
            </div>
        </div>

        <!-- Recent Orders -->
        <div class="card">
            <div class="px-6 py-4 border-b border-color flex justify-between items-center">
                <h3 class="text-lg font-medium text-primary">Recent Orders</h3>
                <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary text-sm">
                    <i class="fas fa-arrow-right mr-2"></i>View All
                </a>
            </div>
            <div class="overflow-x-auto">
                <c:choose>
                    <c:when test="${not empty recentOrders}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Order Number</th>
                                    <th>Customer</th>
                                    <th>Amount</th>
                                    <th>Status</th>
                                    <th>Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${recentOrders}">
                                    <tr class="hover:bg-secondary transition-colors">
                                        <td class="font-medium text-primary">${order.orderNumber}</td>
                                        <td class="text-secondary">${order.customerName != null ? order.customerName : 'Customer ID: '.concat(order.customerId)}</td>
                                        <td class="text-secondary">
                                            <fmt:formatNumber value="${order.totalAmount}" type="currency"/>
                                        </td>
                                        <td>
                                            <span class="badge ${order.status == 'completed' ? 'badge-success' :
                                                               order.status == 'pending' ? 'badge-warning' : 'badge-danger'}">
                                                ${order.status}
                                            </span>
                                        </td>
                                        <td class="text-secondary">
                                            <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center text-secondary py-8">
                            <i class="fas fa-shopping-cart text-4xl mb-4 text-gray-300"></i>
                            <div>No recent orders</div>
                            <div class="text-sm mt-2">
                                <a href="${pageContext.request.contextPath}/orders/new" class="text-primary hover:text-primary-hover">
                                    Create your first order
                                </a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:attribute>
</t:layout>
