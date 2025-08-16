<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${order == null ? 'Create' : 'Edit'} Order - Pahana Edu Bookshop</title>
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
                <div class="max-w-5xl mx-auto">
                    <div class="flex justify-between items-center mb-6">
                        <h1 class="text-2xl font-bold">${order == null ? 'Create' : 'Edit'} Order</h1>
                        <a href="${pageContext.request.contextPath}/orders"
                           class="text-blue-500 hover:text-blue-700">
                            Back to Orders
                        </a>
                    </div>

                    <c:if test="${error != null}">
                        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                            ${error}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/orders${order == null ? '' : '/edit/'.concat(order.id)}"
                          method="POST"
                          class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">

                        <!-- Customer Selection -->
                        <div class="mb-6">
                            <label for="customerId" class="block text-gray-700 text-sm font-bold mb-2">
                                Customer
                            </label>
                            <select id="customerId"
                                    name="customerId"
                                    required
                                    class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
                                <option value="">Select Customer</option>
                                <c:forEach var="customer" items="${customers}">
                                    <option value="${customer.id}" ${order != null && order.customerId eq customer.id ? 'selected' : ''}>
                                        ${customer.name} (${customer.accountNumber})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Order Items -->
                        <div class="mb-6">
                            <label class="block text-gray-700 text-sm font-bold mb-2">
                                Order Items
                            </label>
                            <div id="orderItems" class="space-y-4">
                                <!-- Dynamic item rows will be added here -->
                                <c:if test="${empty orderItems}">
                                    <!-- Initial empty row -->
                                    <div class="flex gap-4 items-start order-item-row">
                                        <div class="flex-1">
                                            <select name="itemId[]" required class="w-full px-3 py-2 border rounded item-select">
                                                <option value="">Select Item</option>
                                                <c:forEach var="item" items="${items}">
                                                    <option value="${item.id}" data-price="${item.price}" data-stock="${item.stockQuantity}">
                                                        ${item.name} (${item.itemCode})
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="w-32">
                                            <input type="number" name="quantity[]" min="1" required
                                                   class="w-full px-3 py-2 border rounded quantity-input"
                                                   placeholder="Quantity">
                                        </div>
                                        <div class="w-32">
                                            <input type="number" name="price[]" step="0.01" required readonly
                                                   class="w-full px-3 py-2 border rounded bg-gray-50 price-input"
                                                   placeholder="Price">
                                        </div>
                                        <div class="w-32">
                                            <input type="number" step="0.01" readonly
                                                   class="w-full px-3 py-2 border rounded bg-gray-50 subtotal-input"
                                                   placeholder="Subtotal">
                                        </div>
                                        <button type="button" class="remove-item px-2 py-1 text-red-600 hover:text-red-800">
                                            Remove
                                        </button>
                                    </div>
                                </c:if>
                                <c:forEach var="item" items="${orderItems}">
                                    <div class="flex gap-4 items-start order-item-row">
                                        <div class="flex-1">
                                            <select name="itemId[]" required class="w-full px-3 py-2 border rounded item-select">
                                                <option value="">Select Item</option>
                                                <c:forEach var="availableItem" items="${items}">
                                                    <option value="${availableItem.id}"
                                                            data-price="${availableItem.price}"
                                                            data-stock="${availableItem.stockQuantity}"
                                                            ${item.itemId eq availableItem.id ? 'selected' : ''}>
                                                        ${availableItem.name} (${availableItem.itemCode})
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="w-32">
                                            <input type="number" name="quantity[]" min="1" required
                                                   value="${item.quantity}"
                                                   class="w-full px-3 py-2 border rounded quantity-input"
                                                   placeholder="Quantity">
                                        </div>
                                        <div class="w-32">
                                            <input type="number" name="price[]" step="0.01" required readonly
                                                   value="${item.unitPrice}"
                                                   class="w-full px-3 py-2 border rounded bg-gray-50 price-input"
                                                   placeholder="Price">
                                        </div>
                                        <div class="w-32">
                                            <input type="number" step="0.01" readonly
                                                   value="${item.subtotal}"
                                                   class="w-full px-3 py-2 border rounded bg-gray-50 subtotal-input"
                                                   placeholder="Subtotal">
                                        </div>
                                        <button type="button" class="remove-item px-2 py-1 text-red-600 hover:text-red-800">
                                            Remove
                                        </button>
                                    </div>
                                </c:forEach>
                            </div>
                            <button type="button" id="addItem"
                                    class="mt-4 bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">
                                Add Item
                            </button>
                        </div>

                        <!-- Order Total -->
                        <div class="mb-6 flex justify-end">
                            <div class="w-64">
                                <label class="block text-gray-700 text-sm font-bold mb-2">
                                    Total Amount
                                </label>
                                <input type="number" id="totalAmount" readonly
                                       class="w-full px-3 py-2 border rounded bg-gray-50 text-xl font-bold"
                                       value="${order.totalAmount}">
                            </div>
                        </div>

                        <!-- Status (for edit only) -->
                        <c:if test="${order != null}">
                            <div class="mb-6">
                                <label for="status" class="block text-gray-700 text-sm font-bold mb-2">
                                    Status
                                </label>
                                <select id="status"
                                        name="status"
                                        required
                                        class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
                                    <option value="pending" ${order.status eq 'pending' ? 'selected' : ''}>Pending</option>
                                    <option value="completed" ${order.status eq 'completed' ? 'selected' : ''}>Completed</option>
                                    <option value="cancelled" ${order.status eq 'cancelled' ? 'selected' : ''}>Cancelled</option>
                                </select>
                            </div>
                        </c:if>

                        <!-- Submit Button -->
                        <div class="flex items-center justify-between">
                            <button type="submit"
                                    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                                ${order == null ? 'Create' : 'Update'} Order
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Item Row Template -->
    <template id="itemRowTemplate">
        <div class="flex gap-4 items-start order-item-row">
            <div class="flex-1">
                <select name="itemId[]" required class="w-full px-3 py-2 border rounded item-select">
                    <option value="">Select Item</option>
                    <c:forEach var="item" items="${items}">
                        <option value="${item.id}" data-price="${item.price}" data-stock="${item.stockQuantity}">
                            ${item.name} (${item.itemCode})
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="w-32">
                <input type="number" name="quantity[]" min="1" required
                       class="w-full px-3 py-2 border rounded quantity-input"
                       placeholder="Quantity">
            </div>
            <div class="w-32">
                <input type="number" name="price[]" step="0.01" required readonly
                       class="w-full px-3 py-2 border rounded bg-gray-50 price-input"
                       placeholder="Price">
            </div>
            <div class="w-32">
                <input type="number" step="0.01" readonly
                       class="w-full px-3 py-2 border rounded bg-gray-50 subtotal-input"
                       placeholder="Subtotal">
            </div>
            <button type="button" class="remove-item px-2 py-1 text-red-600 hover:text-red-800">
                Remove
            </button>
        </div>
    </template>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const orderItems = document.getElementById('orderItems');
            const addItemButton = document.getElementById('addItem');
            const itemRowTemplate = document.getElementById('itemRowTemplate');

            // Add new item row
            addItemButton.addEventListener('click', function() {
                const newRow = itemRowTemplate.content.cloneNode(true);
                orderItems.appendChild(newRow);
                setupEventListeners(orderItems.lastElementChild);
            });

            // Setup event listeners for existing rows
            document.querySelectorAll('.order-item-row').forEach(setupEventListeners);

            // Setup event listeners for a row
            function setupEventListeners(row) {
                const itemSelect = row.querySelector('.item-select');
                const quantityInput = row.querySelector('.quantity-input');
                const priceInput = row.querySelector('.price-input');
                const subtotalInput = row.querySelector('.subtotal-input');
                const removeButton = row.querySelector('.remove-item');

                // Update price when item is selected
                itemSelect.addEventListener('change', function() {
                    const selectedOption = this.options[this.selectedIndex];
                    const price = selectedOption.dataset.price || '';
                    priceInput.value = price;
                    updateSubtotal(row);
                });

                // Update subtotal when quantity changes
                quantityInput.addEventListener('input', function() {
                    updateSubtotal(row);
                });

                // Remove item row
                removeButton.addEventListener('click', function() {
                    row.remove();
                    updateTotal();
                });
            }

            // Calculate subtotal for a row
            function updateSubtotal(row) {
                const quantity = parseFloat(row.querySelector('.quantity-input').value) || 0;
                const price = parseFloat(row.querySelector('.price-input').value) || 0;
                const subtotal = quantity * price;
                row.querySelector('.subtotal-input').value = subtotal.toFixed(2);
                updateTotal();
            }

            // Calculate total amount
            function updateTotal() {
                const subtotals = [...document.querySelectorAll('.subtotal-input')]
                    .map(input => parseFloat(input.value) || 0);
                const total = subtotals.reduce((sum, subtotal) => sum + subtotal, 0);
                document.getElementById('totalAmount').value = total.toFixed(2);
            }
        });
    </script>
</body>
</html>
