<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${item == null ? 'Add New' : 'Edit'} Item - Pahana Edu Bookshop</title>
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
                <div class="max-w-3xl mx-auto">
                    <div class="flex justify-between items-center mb-6">
                        <h1 class="text-2xl font-bold">${item == null ? 'Add New' : 'Edit'} Item</h1>
                        <a href="${pageContext.request.contextPath}/items"
                           class="text-blue-500 hover:text-blue-700">
                            Back to List
                        </a>
                    </div>

                    <c:if test="${error != null}">
                        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                            ${error}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/items${item == null ? '' : '/edit/'.concat(item.id)}"
                          method="POST"
                          class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">

                        <div class="mb-4">
                            <label for="itemCode" class="block text-gray-700 text-sm font-bold mb-2">
                                Item Code
                            </label>
                            <input type="text"
                                   id="itemCode"
                                   name="itemCode"
                                   value="${item.itemCode}"
                                   required
                                   class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-4">
                            <label for="name" class="block text-gray-700 text-sm font-bold mb-2">
                                Name
                            </label>
                            <input type="text"
                                   id="name"
                                   name="name"
                                   value="${item.name}"
                                   required
                                   class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-4">
                            <label for="description" class="block text-gray-700 text-sm font-bold mb-2">
                                Description
                            </label>
                            <textarea id="description"
                                    name="description"
                                    rows="3"
                                    class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">${item.description}</textarea>
                        </div>

                        <div class="mb-4">
                            <label for="price" class="block text-gray-700 text-sm font-bold mb-2">
                                Price
                            </label>
                            <input type="number"
                                   id="price"
                                   name="price"
                                   value="${item.price}"
                                   step="0.01"
                                   min="0"
                                   required
                                   class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-6">
                            <label for="stockQuantity" class="block text-gray-700 text-sm font-bold mb-2">
                                Stock Quantity
                            </label>
                            <input type="number"
                                   id="stockQuantity"
                                   name="stockQuantity"
                                   value="${item.stockQuantity}"
                                   min="0"
                                   required
                                   class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="flex items-center justify-between">
                            <button type="submit"
                                    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                                ${item == null ? 'Create' : 'Update'} Item
                            </button>
                            <button type="reset"
                                    class="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                                Reset
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
