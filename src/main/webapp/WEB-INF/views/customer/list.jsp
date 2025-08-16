<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customers - Pahana Edu Bookshop</title>
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
                    <h1 class="text-2xl font-bold">Customers</h1>
                    <a href="${pageContext.request.contextPath}/customers/new"
                       class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Add New Customer
                    </a>
                </div>

                <!-- Search Bar -->
                <div class="mb-6">
                    <form action="${pageContext.request.contextPath}/customers/search" method="GET" class="flex gap-4">
                        <input type="text" name="term" value="${searchTerm}"
                               placeholder="Search customers..."
                               class="flex-1 px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <button type="submit"
                                class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                            Search
                        </button>
                    </form>
                </div>

                <!-- Customers Table -->
                <div class="bg-white rounded-lg shadow overflow-x-auto">
                    <table class="min-w-full">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Account Number
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Name
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Telephone
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Actions
                                </th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="customer" items="${customers}">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        ${customer.accountNumber}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        ${customer.name}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        ${customer.telephone}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm">
                                        <a href="${pageContext.request.contextPath}/customers/edit/${customer.id}"
                                           class="text-blue-600 hover:text-blue-900 mr-4">Edit</a>
                                        <a href="#" onclick="deleteCustomer('${customer.id}')"
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
        function deleteCustomer(id) {
            if (confirm('Are you sure you want to delete this customer?')) {
                fetch('${pageContext.request.contextPath}/customers/delete/' + id, {
                    method: 'POST'
                }).then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        alert('Failed to delete customer');
                    }
                });
            }
        }
    </script>
</body>
</html>
