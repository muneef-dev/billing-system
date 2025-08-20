<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="New Order" layout="dashboard">
    <jsp:attribute name="content">
        <div class="max-w-4xl mx-auto">
            <div class="flex justify-between items-center mb-6">
                <h1 class="text-2xl font-bold text-primary">Create New Order</h1>
                <a href="${pageContext.request.contextPath}/orders"
                   class="text-primary hover:text-primary-hover transition-colors">Back to Orders</a>
            </div>

            <div class="card p-6">
                <form id="orderForm" class="space-y-6">
                    <!-- Customer Selection -->
                    <div>
                        <label for="customerId" class="form-label">Customer</label>
                        <select id="customerId" name="customerId" class="form-input">
                            <option value="">Select a customer</option>
                            <c:forEach var="customer" items="${customers}">
                                <option value="${customer.id}">${customer.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Items Section -->
                    <div>
                        <h3 class="text-lg font-medium text-primary mb-4">Order Items</h3>

                        <!-- Item Selection -->
                        <div class="flex gap-4 mb-4">
                            <select id="itemSelect" class="form-input flex-1">
                                <option value="">Select an item</option>
                                <c:forEach var="item" items="${items}">
                                    <option value="${item.id}" data-price="${item.price}" data-stock="${item.stockQuantity}">
                                        ${item.name} - $${item.price} (${item.stockQuantity} in stock)
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
                                        <th>Price</th>
                                        <th>Total</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody id="orderItems"></tbody>
                            </table>
                        </div>

                        <!-- Order Total -->
                        <div class="text-right text-lg font-semibold text-primary">
                            Total: $<span id="orderTotal">0.00</span>
                        </div>
                    </div>

                    <!-- Submit Buttons -->
                    <div class="flex justify-end space-x-3">
                        <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/orders'"
                                class="btn btn-secondary">
                            <i class="fas fa-times mr-2"></i>Cancel
                        </button>
                        <button type="button" onclick="submitOrder()" class="btn btn-primary">
                            <i class="fas fa-save mr-2"></i>Create Order
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
                itemsData[${item.id}] = {
                    id: ${item.id},
                    name: "${item.name}",
                    price: ${item.price},
                    stock: ${item.stockQuantity}
                };
            </c:forEach>

            function addItem() {
                const itemSelect = document.getElementById('itemSelect');
                const quantity = document.getElementById('quantity');
                const itemId = itemSelect.value;

                if (!itemId) {
                    alert('Please select an item');
                    return;
                }

                const item = itemsData[itemId];
                const qty = parseInt(quantity.value);

                if (qty > item.stock) {
                    alert('Not enough stock available');
                    return;
                }

                orderItems.push({
                    itemId: itemId,
                    name: item.name,
                    quantity: qty,
                    price: item.price
                });

                updateOrderTable();
                itemSelect.value = '';
                quantity.value = 1;
            }

            function removeItem(index) {
                orderItems.splice(index, 1);
                updateOrderTable();
            }

            function updateOrderTable() {
                const tbody = document.getElementById('orderItems');
                const totalSpan = document.getElementById('orderTotal');
                let total = 0;

                tbody.innerHTML = orderItems.map((item, index) => {
                    const itemTotal = item.quantity * item.price;
                    total += itemTotal;
                    return `
                        <tr>
                            <td class="text-primary font-medium">${item.name}</td>
                            <td class="text-secondary">${item.quantity}</td>
                            <td class="text-secondary">$${item.price.toFixed(2)}</td>
                            <td class="text-secondary">$${itemTotal.toFixed(2)}</td>
                            <td>
                                <button onclick="removeItem(${index})" class="text-danger hover:text-danger-hover transition-colors">
                                    <i class="fas fa-trash mr-1"></i>Remove
                                </button>
                            </td>
                        </tr>
                    `;
                }).join('');

                totalSpan.textContent = total.toFixed(2);
            }

            function submitOrder() {
                const customerId = document.getElementById('customerId').value;

                if (!customerId) {
                    alert('Please select a customer');
                    return;
                }

                if (orderItems.length === 0) {
                    alert('Please add at least one item to the order');
                    return;
                }

                const orderData = {
                    customerId: customerId,
                    items: orderItems.map(item => ({
                        itemId: item.itemId,
                        quantity: item.quantity
                    }))
                };

                fetch('${pageContext.request.contextPath}/orders/create', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(orderData)
                })
                .then(response => {
                    if (response.ok) {
                        window.location.href = '${pageContext.request.contextPath}/orders';
                    } else {
                        throw new Error('Failed to create order');
                    }
                })
                .catch(error => {
                    alert(error.message);
                });
            }
        </script>
    </jsp:attribute>
</t:layout>
