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
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td class="font-medium text-primary">${order.orderNumber}</td>
                                <td class="text-secondary">${order.customerName}</td>
                                <td class="text-secondary">
                                    <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy"/>
                                </td>
                                <td class="text-secondary">
                                    <fmt:formatNumber value="${order.totalAmount}" type="currency"/>
                                </td>
                                <td>
                                    <span class="badge ${order.status == 'completed' ? 'badge-success' :
                                                       order.status == 'pending' ? 'badge-warning' : 'badge-danger'}">
                                        ${order.status}
                                    </span>
                                </td>
                                <td class="space-x-2">
                                    <a href="${pageContext.request.contextPath}/orders/view/${order.id}"
                                       class="text-primary hover:text-primary-hover transition-colors">
                                        <i class="fas fa-eye mr-1"></i>View
                                    </a>
                                    <c:if test="${order.status != 'completed'}">
                                        <a href="#" onclick="cancelOrder('${order.id}')"
                                           class="text-danger hover:text-danger-hover transition-colors">
                                            <i class="fas fa-times mr-1"></i>Cancel
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="scripts">
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                // Check if functions are available
                if (typeof showConfirmDialog === 'undefined' || typeof showSuccessToast === 'undefined') {
                    console.error('Toast and dialog functions not loaded. Please check script.js');
                }
            });

            async function cancelOrder(id) {
                try {
                    const confirmed = await showConfirmDialog(
                        'Cancel Order',
                        'Are you sure you want to cancel this order? This action cannot be undone.',
                        {
                            confirmText: 'Cancel Order',
                            cancelText: 'Keep Order',
                            confirmStyle: 'danger',
                            icon: 'fas fa-ban'
                        }
                    );

                    if (confirmed) {
                        try {
                            const response = await fetch('${pageContext.request.contextPath}/orders/cancel/' + id, {
                                method: 'POST'
                            });

                            if (response.ok) {
                                showSuccessToast('Order cancelled successfully');
                                setTimeout(() => {
                                    window.location.reload();
                                }, 1000);
                            } else {
                                showErrorToast('Failed to cancel order. Please try again.');
                            }
                        } catch (error) {
                            console.error('Network error:', error);
                            showErrorToast('An error occurred while cancelling the order');
                        }
                    }
                } catch (error) {
                    console.error('Dialog error:', error);
                    // Fallback to basic confirm
                    if (confirm('Cancel Order?\n\nAre you sure you want to cancel this order? This action cannot be undone.')) {
                        try {
                            const response = await fetch('${pageContext.request.contextPath}/orders/cancel/' + id, {
                                method: 'POST'
                            });

                            if (response.ok) {
                                alert('Order cancelled successfully');
                                window.location.reload();
                            } else {
                                alert('Failed to cancel order. Please try again.');
                            }
                        } catch (error) {
                            alert('An error occurred while cancelling the order');
                        }
                    }
                }
            }
        </script>
    </jsp:attribute>
</t:layout>
