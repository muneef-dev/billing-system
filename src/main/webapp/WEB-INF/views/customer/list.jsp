<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Customers" layout="dashboard">
    <jsp:attribute name="content">
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-2xl font-bold text-primary">Customers</h1>
            <a href="${pageContext.request.contextPath}/customers/new"
               class="btn btn-primary">
                <i class="fas fa-plus mr-2"></i>Add New Customer
            </a>
        </div>

        <!-- Search Bar -->
        <div class="card p-4 mb-6">
            <form action="${pageContext.request.contextPath}/customers/search" method="GET" class="flex gap-4">
                <input type="text" name="term" value="${searchTerm}"
                       placeholder="Search customers..."
                       class="form-input flex-1">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search mr-2"></i>Search
                </button>
            </form>
        </div>

        <!-- Customers Table -->
        <div class="card overflow-hidden">
            <div class="overflow-x-auto">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Account Number</th>
                            <th>Name</th>
                            <th>Telephone</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="customer" items="${customers}">
                            <tr>
                                <td class="font-medium text-primary">${customer.accountNumber}</td>
                                <td class="text-secondary">${customer.name}</td>
                                <td class="text-secondary">${customer.telephone}</td>
                                <td class="space-x-2">
                                    <a href="${pageContext.request.contextPath}/customers/edit/${customer.id}"
                                       class="text-primary hover:text-primary-hover transition-colors">
                                        <i class="fas fa-edit mr-1"></i>Edit
                                    </a>
                                    <a href="#" onclick="deleteCustomer('${customer.id}')"
                                       class="text-danger hover:text-danger-hover transition-colors">
                                        <i class="fas fa-trash mr-1"></i>Delete
                                    </a>
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
    </jsp:attribute>
</t:layout>
