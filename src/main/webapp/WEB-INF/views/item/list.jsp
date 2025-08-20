<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Items" layout="dashboard">
    <jsp:attribute name="content">
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-2xl font-bold text-primary">Items</h1>
            <a href="${pageContext.request.contextPath}/items/new"
               class="btn btn-primary">
                <i class="fas fa-plus mr-2"></i>Add New Item
            </a>
        </div>

        <!-- Search Bar -->
        <div class="card p-4 mb-6">
            <form action="${pageContext.request.contextPath}/items/search" method="GET" class="flex gap-4">
                <input type="text" name="term" value="${searchTerm}"
                       placeholder="Search items..."
                       class="form-input flex-1">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search mr-2"></i>Search
                </button>
            </form>
        </div>

        <!-- Items Table -->
        <div class="card overflow-hidden">
            <div class="overflow-x-auto">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${items}">
                            <tr>
                                <td class="font-medium text-primary">${item.itemCode}</td>
                                <td class="text-secondary">${item.name}</td>
                                <td class="text-secondary">
                                    <fmt:formatNumber value="${item.price}" type="currency"/>
                                </td>
                                <td class="text-secondary">${item.stockQuantity}</td>
                                <td class="space-x-2">
                                    <a href="${pageContext.request.contextPath}/items/edit/${item.id}"
                                       class="text-primary hover:text-primary-hover transition-colors">
                                        <i class="fas fa-edit mr-1"></i>Edit
                                    </a>
                                    <a href="#" onclick="deleteItem('${item.id}', '${item.name}')"
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
            document.addEventListener('DOMContentLoaded', function() {
                // Check if functions are available
                if (typeof showDeleteConfirm === 'undefined' || typeof showSuccessToast === 'undefined') {
                    console.error('Toast and dialog functions not loaded. Please check script.js');
                }
            });

            async function deleteItem(id, itemName) {
                try {
                    const confirmed = await showDeleteConfirm('Item', `This will permanently remove "${itemName}" and cannot be undone.`);

                    if (confirmed) {
                        try {
                            const response = await fetch('${pageContext.request.contextPath}/items/delete/' + id, {
                                method: 'POST'
                            });

                            if (response.ok) {
                                showSuccessToast('Item deleted successfully');
                                setTimeout(() => {
                                    window.location.reload();
                                }, 1000);
                            } else {
                                showErrorToast('Failed to delete item. Please try again.');
                            }
                        } catch (error) {
                            console.error('Network error:', error);
                            showErrorToast('An error occurred while deleting the item');
                        }
                    }
                } catch (error) {
                    console.error('Dialog error:', error);
                    // Fallback to basic confirm
                    if (confirm('Delete Item?\n\nAre you sure you want to delete "' + itemName + '"? This action cannot be undone.')) {
                        try {
                            const response = await fetch('${pageContext.request.contextPath}/items/delete/' + id, {
                                method: 'POST'
                            });

                            if (response.ok) {
                                alert('Item deleted successfully');
                                window.location.reload();
                            } else {
                                alert('Failed to delete item. Please try again.');
                            }
                        } catch (error) {
                            alert('An error occurred while deleting the item');
                        }
                    }
                }
            }
        </script>
    </jsp:attribute>
</t:layout>
