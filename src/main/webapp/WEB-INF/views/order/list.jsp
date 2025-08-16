<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orders - Pahana Edu Bookshop</title>
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
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-2xl font-bold">Orders</h1>
                    <a href="${pageContext.request.contextPath}/orders/new"
                       class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Create New Order
                    </a>
                </div>

                <!-- Search Bar -->
                <div class="mb-6">
                    <form action="${pageContext.request.contextPath}/orders/search" method="GET" class="flex gap-4">
                        <input type="text" name="term" value="${searchTerm}"
                               placeholder="Search orders..."
                               class="flex-1 px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <button type="submit"
                                class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                            Search
                        </button>
                    </form>
                </div>

                <!-- Orders Table -->
                <div class="bg-white rounded-lg shadow overflow-x-auto">
                    <table class="min-w-full">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Order Number
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Customer
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Date
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Total Amount
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Status
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Actions
                                </th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                        ${order.orderNumber}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        ${order.customerName}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy HH:mm"/>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        <fmt:formatNumber value="${order.totalAmount}" type="currency"/>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full
                                            ${order.status eq 'completed' ? 'bg-green-100 text-green-800' :
                                              order.status eq 'pending' ? 'bg-yellow-100 text-yellow-800' :
                                              'bg-red-100 text-red-800'}">
                                            ${order.status}
                                        </span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm">
                                        <a href="${pageContext.request.contextPath}/orders/edit/${order.id}"
                                           class="text-blue-600 hover:text-blue-900 mr-4">Edit</a>
                                        <a href="#" onclick="deleteOrder('${order.id}')"
                                           class="text-red-600 hover:text-red-900">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
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
                        window.location.reload();
                    } else {
                        alert('Failed to delete order');
                    }
                }).catch(error => {
                    console.error('Error:', error);
                    alert('Failed to delete order');
                });
            }
        }
    </script>
</body>
</html>
