<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Items - Pahana Edu Bookshop</title>
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
                    <h1 class="text-2xl font-bold">Items</h1>
                    <a href="${pageContext.request.contextPath}/items/new"
                       class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Add New Item
                    </a>
                </div>

                <!-- Search Bar -->
                <div class="mb-6">
                    <form action="${pageContext.request.contextPath}/items/search" method="GET" class="flex gap-4">
                        <input type="text" name="term" value="${searchTerm}"
                               placeholder="Search items..."
                               class="flex-1 px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <button type="submit"
                                class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                            Search
                        </button>
                    </form>
                </div>

                <!-- Items Table -->
                <div class="bg-white rounded-lg shadow overflow-x-auto">
                    <table class="min-w-full">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Item Code
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Name
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Price
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Stock
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Actions
                                </th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="item" items="${items}">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        ${item.itemCode}
                                    </td>
                                    <td class="px-6 py-4">
                                        <div class="text-sm text-gray-900">${item.name}</div>
                                        <div class="text-sm text-gray-500">${item.description}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <fmt:formatNumber value="${item.price}" type="currency"/>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full
                                            ${item.stockQuantity > 10 ? 'bg-green-100 text-green-800' :
                                              item.stockQuantity > 0 ? 'bg-yellow-100 text-yellow-800' :
                                              'bg-red-100 text-red-800'}">
                                            ${item.stockQuantity}
                                        </span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm">
                                        <a href="${pageContext.request.contextPath}/items/edit/${item.id}"
                                           class="text-blue-600 hover:text-blue-900 mr-4">Edit</a>
                                        <a href="#" onclick="deleteItem('${item.id}')"
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
        function deleteItem(id) {
            if (confirm('Are you sure you want to delete this item?')) {
                fetch('${pageContext.request.contextPath}/items/delete/' + id, {
                    method: 'POST'
                }).then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        alert('Failed to delete item');
                    }
                });
            }
        }
    </script>
</body>
</html>
