<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details - Pahana Edu Bookshop</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
    <div class="min-h-screen flex">
        <!-- Sidebar -->
        <jsp:include page="../../components/sidebar.jsp" />

        <!-- Main Content -->
        <div class="flex-1">
            <!-- Top Navigation -->
            <jsp:include page="../../components/topnav.jsp" />

            <!-- Content -->
            <div class="p-6">
                <div class="max-w-4xl mx-auto">
                    <div class="flex justify-between items-center mb-6">
                        <h1 class="text-2xl font-bold">Order Details</h1>
                        <div class="space-x-2">
                            <a href="${pageContext.request.contextPath}/orders/edit/${order.id}"
                               class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                                Edit Order
                            </a>
                            <a href="${pageContext.request.contextPath}/orders"
                               class="text-blue-500 hover:text-blue-700">
                                Back to Orders
                            </a>
                        </div>
                    </div>

                    <!-- Order Summary -->
                    <div class="bg-white shadow rounded-lg mb-6">
                        <div class="px-6 py-4 border-b border-gray-200">
                            <h2 class="text-lg font-semibold text-gray-800">Order Summary</h2>
                        </div>
                        <div class="p-6 grid grid-cols-2 gap-4">
                            <div>
                                <p class="text-sm text-gray-600">Order Number</p>
                                <p class="font-semibold">${order.orderNumber}</p>
                            </div>
                            <div>
                                <p class="text-sm text-gray-600">Order Date</p>
                                <p class="font-semibold">
                                    <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy HH:mm"/>
                                </p>
                            </div>
                            <div>
                                <p class="text-sm text-gray-600">Customer</p>
                                <p class="font-semibold">${order.customerName}</p>
                            </div>
                            <div>
                                <p class="text-sm text-gray-600">Status</p>
                                <p>
                                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full
                                        ${order.status eq 'completed' ? 'bg-green-100 text-green-800' :
                                          order.status eq 'pending' ? 'bg-yellow-100 text-yellow-800' :
                                          'bg-red-100 text-red-800'}">
                                        ${order.status}
                                    </span>
                                </p>
                            </div>
                        </div>
                    </div>

                    <!-- Order Items -->
                    <div class="bg-white shadow rounded-lg mb-6">
                        <div class="px-6 py-4 border-b border-gray-200">
                            <h2 class="text-lg font-semibold text-gray-800">Order Items</h2>
                        </div>
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                                            Item
                                        </th>
                                        <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">
                                            Unit Price
                                        </th>
                                        <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">
                                            Quantity
                                        </th>
                                        <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">
                                            Subtotal
                                        </th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <c:forEach var="item" items="${orderItems}">
                                        <tr>
                                            <td class="px-6 py-4">
                                                <div class="text-sm font-medium text-gray-900">${item.itemName}</div>
                                                <div class="text-sm text-gray-500">${item.itemCode}</div>
                                            </td>
                                            <td class="px-6 py-4 text-right text-sm text-gray-500">
                                                <fmt:formatNumber value="${item.unitPrice}" type="currency"/>
                                            </td>
                                            <td class="px-6 py-4 text-right text-sm text-gray-500">
                                                ${item.quantity}
                                            </td>
                                            <td class="px-6 py-4 text-right text-sm text-gray-500">
                                                <fmt:formatNumber value="${item.subtotal}" type="currency"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                                <tfoot class="bg-gray-50">
                                    <tr>
                                        <td colspan="3" class="px-6 py-4 text-right text-sm font-medium text-gray-900">
                                            Total Amount
                                        </td>
                                        <td class="px-6 py-4 text-right text-sm font-medium text-gray-900">
                                            <fmt:formatNumber value="${order.totalAmount}" type="currency"/>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>

                    <!-- Actions -->
                    <div class="flex justify-end space-x-4">
                        <button onclick="window.print()"
                                class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">
                            Print Order
                        </button>
                        <button onclick="deleteOrder('${order.id}')"
                                class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
                            Delete Order
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function deleteOrder(id) {
            if (confirm('Are you sure you want to delete this order?')) {
                fetch('${pageContext.request.contextPath}/orders/delete/' + id, {
                    method: 'POST'
                }).then(response => {
                    if (response.ok) {
                        window.location.href = '${pageContext.request.contextPath}/orders';
                    } else {
                        alert('Failed to delete order');
                    }
                });
            }
        }
    </script>
</body>
</html>
