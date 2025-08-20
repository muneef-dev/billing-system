<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="${order == null ? 'Create New' : 'Edit'} Order" layout="dashboard">
    <jsp:attribute name="content">
        <div class="max-w-4xl mx-auto">
            <div class="flex justify-between items-center mb-6">
                <h1 class="text-2xl font-bold text-primary">${order == null ? 'Create New' : 'Edit'} Order</h1>
                <a href="${pageContext.request.contextPath}/orders"
                   class="text-primary hover:text-primary-hover transition-colors">Back to Orders</a>
            </div>

            <c:if test="${error != null}">
                <div class="card p-4 mb-4 bg-danger-light border-danger text-danger">
                    <i class="fas fa-exclamation-triangle mr-2"></i>${error}
                </div>
            </c:if>

            <div class="card p-6">
                <form id="orderForm" action="${pageContext.request.contextPath}/orders${order == null ? '' : '/edit/'.concat(order.id)}" method="POST" class="space-y-6">
                    <!-- Customer Selection -->
                    <div>
                        <label for="customerId" class="form-label">Customer <span class="text-red-500">*</span></label>
                        <select id="customerId" name="customerId" class="form-input" required>
                            <option value="">Select a customer</option>
                            <c:forEach var="customer" items="${customers}">
                                <option value="${customer.id}" ${order != null && order.customerId == customer.id ? 'selected' : ''}>
                                    ${customer.name} (${customer.accountNumber})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Order Status (only for edit mode) -->
                    <c:if test="${order != null}">
                        <div>
                            <label for="status" class="form-label">Order Status</label>
                            <select id="status" name="status" class="form-input">
                                <option value="Pending" ${order.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                <option value="Paid" ${order.status == 'Paid' ? 'selected' : ''}>Paid</option>
                                <option value="Cancelled" ${order.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                            </select>
                        </div>
                    </c:if>

                    <!-- Items Section -->
                    <div>
                        <h3 class="text-lg font-medium text-primary mb-4">Order Items</h3>

                        <!-- Item Selection -->
                        <div class="flex gap-4 mb-4">
                            <select id="itemSelect" class="form-input flex-1">
                                <option value="">Select an item</option>
                                <c:forEach var="item" items="${items}">
                                    <option value="${item.id}" data-name="${item.itemName}" data-price="${item.unitPrice}" data-stock="${item.stockQuantity}">
                                        ${item.itemName} - $${item.unitPrice} (${item.stockQuantity} in stock)
                                    </option>
                                </c:forEach>
                            </select>
                            <input type="number" id="quantity" min="1" value="1" placeholder="Qty" class="w-24 form-input">
                            <button type="button" onclick="addItem()" class="btn btn-primary">
                                <i class="fas fa-plus mr-2"></i>Add Item
                            </button>
                        </div>

                        <!-- Items Table -->
                        <div class="card overflow-hidden mb-4">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Item</th>
                                        <th>Quantity</th>
                                        <th>Unit Price</th>
                                        <th>Total</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody id="orderItems"></tbody>
                            </table>
                        </div>

                        <!-- Order Summary -->
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 p-4 bg-tertiary rounded-lg">
                            <div class="text-center">
                                <div class="text-sm text-secondary">Subtotal</div>
                                <div class="text-lg font-semibold text-primary">$<span id="orderSubtotal">0.00</span></div>
                            </div>
                            <div class="text-center">
                                <div class="text-sm text-secondary">Discount</div>
                                <input type="number" id="discountAmount" name="discountAmount" step="0.01" min="0" value="${order != null ? order.discountAmount : 0}"
                                       class="form-input text-center" onchange="updateOrderTotal()">
                            </div>
                            <div class="text-center">
                                <div class="text-sm text-secondary">Total</div>
                                <div class="text-xl font-bold text-primary">$<span id="orderTotal">0.00</span></div>
                            </div>
                        </div>

                        <!-- Hidden inputs for order items -->
                        <div id="orderItemsData"></div>
                    </div>

                    <!-- Submit Buttons -->
                    <div class="flex justify-end space-x-3">
                        <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/orders'"
                                class="btn btn-secondary">
                            <i class="fas fa-times mr-2"></i>Cancel
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save mr-2"></i>${order == null ? 'Create' : 'Update'} Order
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="scripts">
        <script>
            let orderItems = [];
            let itemsData = {};

            // Initialize items data
            <c:forEach var="item" items="${items}">
                itemsData['${item.id}'] = {
                    id: '${item.id}',
                    name: '${item.itemName}',
                    price: ${item.unitPrice},
                    stock: ${item.stockQuantity}
                };
            </c:forEach>

            // Initialize existing order items for edit mode
            <c:if test="${orderItems != null}">
                <c:forEach var="orderItem" items="${orderItems}">
                    orderItems.push({
                        itemId: '${orderItem.itemId}',
                        name: itemsData['${orderItem.itemId}'] ? itemsData['${orderItem.itemId}'].name : 'Unknown Item',
                        quantity: ${orderItem.quantity},
                        unitPrice: ${orderItem.unitPrice}
                    });
                </c:forEach>
            </c:if>

            document.addEventListener('DOMContentLoaded', function() {
                updateOrderTable();

                // Add enter key support for quantity input
                document.getElementById('quantity').addEventListener('keypress', function(e) {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        addItem();
                    }
                });
            });

            function addItem() {
                const itemSelect = document.getElementById('itemSelect');
                const quantity = document.getElementById('quantity');
                const itemId = itemSelect.value;

                if (!itemId) {
                    alert('Please select an item first');
                    return;
                }

                const option = itemSelect.options[itemSelect.selectedIndex];
                const itemName = option.getAttribute('data-name');
                const itemPrice = parseFloat(option.getAttribute('data-price'));
                const itemStock = parseInt(option.getAttribute('data-stock'));
                const qty = parseInt(quantity.value);

                if (qty <= 0) {
                    alert('Please enter a valid quantity');
                    return;
                }

                if (qty > itemStock) {
                    alert(`Not enough stock available. Only ${itemStock} items in stock.`);
                    return;
                }

                // Check if item already exists in order
                const existingItemIndex = orderItems.findIndex(orderItem => orderItem.itemId === itemId);
                if (existingItemIndex !== -1) {
                    const totalQuantity = orderItems[existingItemIndex].quantity + qty;
                    if (totalQuantity > itemStock) {
                        alert(`Cannot add ${qty} more items. Only ${itemStock - orderItems[existingItemIndex].quantity} items available.`);
                        return;
                    }
                    orderItems[existingItemIndex].quantity = totalQuantity;
                } else {
                    orderItems.push({
                        itemId: itemId,
                        name: itemName,
                        quantity: qty,
                        unitPrice: itemPrice
                    });
                }

                updateOrderTable();
                itemSelect.value = '';
                quantity.value = 1;
            }

            function removeItem(index) {
                const item = orderItems[index];
                if (confirm(`Remove Item?\n\nAre you sure you want to remove "${item.name}" from this order?`)) {
                    orderItems.splice(index, 1);
                    updateOrderTable();
                }
            }

            function updateOrderTable() {
                const tbody = document.getElementById('orderItems');
                const subtotalSpan = document.getElementById('orderSubtotal');
                const dataDiv = document.getElementById('orderItemsData');
                let subtotal = 0;

                if (orderItems.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center text-secondary py-8">No items added yet</td></tr>';
                    subtotalSpan.textContent = '0.00';
                    dataDiv.innerHTML = '';
                    updateOrderTotal();
                    return;
                }

                // Update table display
                tbody.innerHTML = orderItems.map((item, index) => {
                    const itemTotal = item.quantity * item.unitPrice;
                    subtotal += itemTotal;
                    return `
                        <tr>
                            <td class="text-primary font-medium">${item.name}</td>
                            <td class="text-secondary">${item.quantity}</td>
                            <td class="text-secondary">$${item.unitPrice.toFixed(2)}</td>
                            <td class="text-secondary">$${itemTotal.toFixed(2)}</td>
                            <td>
                                <button type="button" onclick="removeItem(${index})" class="text-danger hover:text-danger-hover transition-colors">
                                    <i class="fas fa-trash mr-1"></i>Remove
                                </button>
                            </td>
                        </tr>
                    `;
                }).join('');

                // Update hidden form inputs
                dataDiv.innerHTML = orderItems.map((item, index) => `
                    <input type="hidden" name="itemId[]" value="${item.itemId}">
                    <input type="hidden" name="quantity[]" value="${item.quantity}">
                    <input type="hidden" name="unitPrice[]" value="${item.unitPrice}">
                `).join('');

                subtotalSpan.textContent = subtotal.toFixed(2);
                updateOrderTotal();
            }

            function updateOrderTotal() {
                const subtotal = parseFloat(document.getElementById('orderSubtotal').textContent);
                const discount = parseFloat(document.getElementById('discountAmount').value) || 0;
                const total = Math.max(0, subtotal - discount);
                document.getElementById('orderTotal').textContent = total.toFixed(2);
            }

            // Form validation before submit
            document.getElementById('orderForm').addEventListener('submit', function(e) {
                const customerId = document.getElementById('customerId').value;

                if (!customerId) {
                    e.preventDefault();
                    alert('Please select a customer first');
                    return false;
                }

                if (orderItems.length === 0) {
                    e.preventDefault();
                    alert('Please add at least one item to the order');
                    return false;
                }

                return true;
            });
        </script>
    </jsp:attribute>
</t:layout>
