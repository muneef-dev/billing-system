<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${customer == null ? 'Add New' : 'Edit'} Customer - Pahana Edu Bookshop</title>
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
                        <h1 class="text-2xl font-bold">${customer == null ? 'Add New' : 'Edit'} Customer</h1>
                        <a href="${pageContext.request.contextPath}/customers"
                           class="text-blue-500 hover:text-blue-700">
                            Back to List
                        </a>
                    </div>

                    <c:if test="${error != null}">
                        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                            ${error}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/customers${customer == null ? '' : '/edit/'.concat(customer.id)}"
                          method="POST"
                          class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">

                        <div class="mb-4">
                            <label for="accountNumber" class="block text-gray-700 text-sm font-bold mb-2">
                                Account Number
                            </label>
                            <input type="text"
                                   id="accountNumber"
                                   name="accountNumber"
                                   value="${customer.accountNumber}"
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
                                   value="${customer.name}"
                                   required
                                   class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-4">
                            <label for="address" class="block text-gray-700 text-sm font-bold mb-2">
                                Address
                            </label>
                            <textarea id="address"
                                      name="address"
                                      rows="3"
                                      class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">${customer.address}</textarea>
                        </div>

                        <div class="mb-6">
                            <label for="telephone" class="block text-gray-700 text-sm font-bold mb-2">
                                Telephone
                            </label>
                            <input type="tel"
                                   id="telephone"
                                   name="telephone"
                                   value="${customer.telephone}"
                                   pattern="[0-9]{10}"
                                   title="Please enter a valid 10-digit phone number"
                                   class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="flex items-center justify-between">
                            <button type="submit"
                                    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                                ${customer == null ? 'Create' : 'Update'} Customer
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
