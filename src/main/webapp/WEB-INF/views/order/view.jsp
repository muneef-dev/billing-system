<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="View Order" layout="dashboard">
    <jsp:attribute name="content">
        <div class="max-w-4xl mx-auto">
            <div class="flex justify-between items-center mb-6">
                <h1 class="text-2xl font-bold text-primary">Order Details</h1>
                <a href="${pageContext.request.contextPath}/orders"
                   class="text-primary hover:text-primary-hover transition-colors">Back to Orders</a>
            </div>

            <div class="card overflow-hidden">
                <!-- Order Header -->
                <div class="p-6 border-b border-color">
                    <div class="grid grid-cols-2 gap-6">
                        <div>
                            <h3 class="text-sm font-medium text-secondary">Order Number</h3>
                            <p class="mt-1 text-lg font-medium text-primary">${order.orderNumber}</p>
                        </div>
                        <div>
                            <h3 class="text-sm font-medium text-secondary">Order Date</h3>
                            <p class="mt-1 text-lg font-medium text-primary">
                                <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy"/>
                            </p>
                        </div>
                        <div>
                            <h3 class="text-sm font-medium text-secondary">Customer</h3>
                            <p class="mt-1 text-lg font-medium text-primary">${order.customerName}</p>
                        </div>
                        <div>
                            <h3 class="text-sm font-medium text-secondary">Status</h3>
                            <p class="mt-1">
                                <span class="badge ${order.status == 'completed' ? 'badge-success' :
                                                   order.status == 'pending' ? 'badge-warning' : 'badge-danger'}">
                                    ${order.status}
                                </span>
                            </p>
                        </div>
                    </div>
                </div>

                <!-- Order Items -->
                <div class="px-6 py-4">
                    <h3 class="text-lg font-medium text-primary mb-4">Order Items</h3>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Item</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td class="font-medium text-primary">${item.name}</td>
                                    <td class="text-secondary">${item.quantity}</td>
                                    <td class="text-secondary">
                                        <fmt:formatNumber value="${item.price}" type="currency"/>
                                    </td>
                                    <td class="text-secondary">
                                        <fmt:formatNumber value="${item.quantity * item.price}" type="currency"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot class="bg-tertiary">
                            <tr>
                                <td colspan="3" class="text-right font-medium text-primary">Total:</td>
                                <td class="font-medium text-primary">
                                    <fmt:formatNumber value="${order.totalAmount}" type="currency"/>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>

                <!-- Actions -->
                <div class="px-6 py-4 bg-tertiary border-t border-color">
                    <div class="flex justify-end space-x-3">
                        <a href="${pageContext.request.contextPath}/orders" class="btn btn-secondary">
                            <i class="fas fa-arrow-left mr-2"></i>Back
                        </a>
                        <c:if test="${order.status != 'completed' && order.status != 'cancelled'}">
                            <button onclick="completeOrder('${order.id}')" class="btn btn-success">
                                <i class="fas fa-check mr-2"></i>Complete Order
                            </button>
                            <button onclick="cancelOrder('${order.id}')" class="btn btn-danger">
                                <i class="fas fa-times mr-2"></i>Cancel Order
                            </button>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="scripts">
        <script>
            function completeOrder(id) {
                if (confirm('Are you sure you want to mark this order as completed?')) {
                    fetch('${pageContext.request.contextPath}/orders/complete/' + id, {
                        method: 'POST'
                    }).then(response => {
                        if (response.ok) {
                            window.location.reload();
                        } else {
                            alert('Failed to complete order');
                        }
                    });
                }
            }

            function cancelOrder(id) {
                if (confirm('Are you sure you want to cancel this order?')) {
                    fetch('${pageContext.request.contextPath}/orders/cancel/' + id, {
                        method: 'POST'
                    }).then(response => {
                        if (response.ok) {
                            window.location.reload();
                        } else {
                            alert('Failed to cancel order');
                        }
                    });
                }
            }
        </script>
    </jsp:attribute>
</t:layout>
