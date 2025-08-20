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
                                <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy HH:mm"/>
                            </p>
                        </div>
                        <div>
                            <h3 class="text-sm font-medium text-secondary">Customer</h3>
                            <p class="mt-1 text-lg font-medium text-primary">
                                ${order.customerName != null ? order.customerName : 'Customer ID: '.concat(order.customerId)}
                            </p>
                        </div>
                        <div>
                            <h3 class="text-sm font-medium text-secondary">Status</h3>
                            <p class="mt-1">
                                <span class="badge ${order.status == 'Paid' ? 'badge-success' :
                                                   order.status == 'Pending' ? 'badge-warning' : 'badge-danger'}">
                                    ${order.status}
                                </span>
                            </p>
                        </div>
                    </div>
                </div>

                <!-- Order Items -->
                <div class="px-6 py-4">
                    <h3 class="text-lg font-medium text-primary mb-4">Order Items</h3>
                    <c:choose>
                        <c:when test="${not empty orderItems}">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Item</th>
                                        <th>Quantity</th>
                                        <th>Unit Price</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${orderItems}">
                                        <tr>
                                            <td class="font-medium text-primary">
                                                ${item.itemName != null ? item.itemName : 'Item ID: '.concat(item.itemId)}
                                            </td>
                                            <td class="text-secondary">${item.quantity}</td>
                                            <td class="text-secondary">
                                                <fmt:formatNumber value="${item.unitPrice}" type="currency"/>
                                            </td>
                                            <td class="text-secondary">
                                                <fmt:formatNumber value="${item.totalPrice}" type="currency"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center text-secondary py-8">
                                <i class="fas fa-box-open text-4xl mb-4 text-gray-300"></i>
                                <div>No items found for this order</div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Order Summary -->
                <div class="px-6 py-4 bg-tertiary border-t border-color">
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                        <div class="text-center">
                            <div class="text-sm text-secondary">Subtotal</div>
                            <div class="text-lg font-semibold text-primary">
                                <fmt:formatNumber value="${order.subtotal}" type="currency"/>
                            </div>
                        </div>
                        <div class="text-center">
                            <div class="text-sm text-secondary">Discount</div>
                            <div class="text-lg font-semibold text-primary">
                                <fmt:formatNumber value="${order.discountAmount}" type="currency"/>
                            </div>
                        </div>
                        <div class="text-center">
                            <div class="text-sm text-secondary">Total Amount</div>
                            <div class="text-xl font-bold text-primary">
                                <fmt:formatNumber value="${order.totalAmount}" type="currency"/>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Actions -->
                <div class="px-6 py-4 bg-tertiary border-t border-color">
                    <div class="flex justify-end space-x-3">
                        <a href="${pageContext.request.contextPath}/orders" class="btn btn-secondary">
                            <i class="fas fa-arrow-left mr-2"></i>Back to Orders
                        </a>
                        <c:if test="${order.status != 'Paid' && order.status != 'Cancelled'}">
                            <a href="${pageContext.request.contextPath}/orders/edit/${order.id}" class="btn btn-primary">
                                <i class="fas fa-edit mr-2"></i>Edit Order
                            </a>
                            <button onclick="markPaidOrder('${order.id}', '${order.orderNumber}')" class="btn btn-success">
                                <i class="fas fa-check mr-2"></i>Mark as Paid
                            </button>
                            <button onclick="cancelOrder('${order.id}', '${order.orderNumber}')" class="btn btn-danger">
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
            document.addEventListener('DOMContentLoaded', function() {
                // Check if functions are available
                if (typeof showConfirmDialog === 'undefined' || typeof showSuccessToast === 'undefined') {
                    console.error('Toast and dialog functions not loaded. Please check script.js');
                }
            });

            async function markPaidOrder(id, orderNumber) {
                try {
                    const confirmed = await showConfirmDialog(
                        'Mark Order as Paid',
                        `Are you sure you want to mark order "${orderNumber}" as paid? This action cannot be undone.`,
                        {
                            confirmText: 'Mark as Paid',
                            cancelText: 'Cancel',
                            confirmStyle: 'success',
                            icon: 'fas fa-check-circle'
                        }
                    );

                    if (confirmed) {
                        // Show loading state
                        const btn = event.target.closest('button');
                        const originalText = btn.innerHTML;
                        btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Processing...';
                        btn.disabled = true;

                        try {
                            const response = await fetch('${pageContext.request.contextPath}/orders/edit/' + id, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                },
                                body: 'status=Paid'
                            });

                            if (response.ok) {
                                showSuccessToast('Order marked as paid successfully');
                                setTimeout(() => {
                                    window.location.reload();
                                }, 1000);
                            } else {
                                showErrorToast('Failed to update order status. Please try again.');
                                btn.innerHTML = originalText;
                                btn.disabled = false;
                            }
                        } catch (error) {
                            console.error('Network error:', error);
                            showErrorToast('An error occurred while updating the order');
                            btn.innerHTML = originalText;
                            btn.disabled = false;
                        }
                    }
                } catch (error) {
                    console.error('Dialog error:', error);
                    // Fallback to basic confirm
                    if (confirm('Mark as Paid?\n\nAre you sure you want to mark order "' + orderNumber + '" as paid?')) {
                        window.location.href = '${pageContext.request.contextPath}/orders/edit/' + id + '?status=Paid';
                    }
                }
            }

            async function cancelOrder(id, orderNumber) {
                try {
                    const confirmed = await showConfirmDialog(
                        'Cancel Order',
                        `Are you sure you want to cancel order "${orderNumber}"? This action will set the status to Cancelled.`,
                        {
                            confirmText: 'Cancel Order',
                            cancelText: 'Keep Order',
                            confirmStyle: 'danger',
                            icon: 'fas fa-ban'
                        }
                    );

                    if (confirmed) {
                        // Show loading state
                        const btn = event.target.closest('button');
                        const originalText = btn.innerHTML;
                        btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Cancelling...';
                        btn.disabled = true;

                        try {
                            const response = await fetch('${pageContext.request.contextPath}/orders/edit/' + id, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                },
                                body: 'status=Cancelled'
                            });

                            if (response.ok) {
                                showSuccessToast('Order cancelled successfully');
                                setTimeout(() => {
                                    window.location.reload();
                                }, 1000);
                            } else {
                                showErrorToast('Failed to cancel order. Please try again.');
                                btn.innerHTML = originalText;
                                btn.disabled = false;
                            }
                        } catch (error) {
                            console.error('Network error:', error);
                            showErrorToast('An error occurred while cancelling the order');
                            btn.innerHTML = originalText;
                            btn.disabled = false;
                        }
                    }
                } catch (error) {
                    console.error('Dialog error:', error);
                    // Fallback to basic confirm
                    if (confirm('Cancel Order?\n\nAre you sure you want to cancel order "' + orderNumber + '"?')) {
                        window.location.href = '${pageContext.request.contextPath}/orders/edit/' + id + '?status=Cancelled';
                    }
                }
            }
        </script>
    </jsp:attribute>
</t:layout>
