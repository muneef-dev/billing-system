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

            // Check if functions are available
            document.addEventListener('DOMContentLoaded', function() {
                if (typeof showConfirmDialog === 'undefined' || typeof showSuccessToast === 'undefined') {
                    console.error('Toast and dialog functions not loaded. Please check script.js');
                }

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
                    try {
                        showWarningToast('Please select an item first');
                    } catch (error) {
                        alert('Please select an item first');
                    }
                    return;
                }

                const item = itemsData[itemId];
                const qty = parseInt(quantity.value);

                if (qty <= 0) {
                    try {
                        showWarningToast('Please enter a valid quantity');
                    } catch (error) {
                        alert('Please enter a valid quantity');
                    }
                    return;
                }

                if (qty > item.stock) {
                    try {
                        showErrorToast(`Not enough stock available. Only ${item.stock} items in stock.`);
                    } catch (error) {
                        alert(`Not enough stock available. Only ${item.stock} items in stock.`);
                    }
                    return;
                }

                // Check if item already exists in order
                const existingItemIndex = orderItems.findIndex(orderItem => orderItem.itemId === itemId);
                if (existingItemIndex !== -1) {
                    const totalQuantity = orderItems[existingItemIndex].quantity + qty;
                    if (totalQuantity > item.stock) {
                        try {
                            showErrorToast(`Cannot add ${qty} more items. Only ${item.stock - orderItems[existingItemIndex].quantity} items available.`);
                        } catch (error) {
                            alert(`Cannot add ${qty} more items. Only ${item.stock - orderItems[existingItemIndex].quantity} items available.`);
                        }
                        return;
                    }
                    orderItems[existingItemIndex].quantity = totalQuantity;
                    try {
                        showInfoToast(`Updated quantity for ${item.name}`);
                    } catch (error) {
                        // Silent fallback for info toast
                    }
                } else {
                    orderItems.push({
                        itemId: itemId,
                        name: item.name,
                        quantity: qty,
                        price: item.price
                    });
                    try {
                        showSuccessToast(`${item.name} added to order`);
                    } catch (error) {
                        // Silent fallback for success toast
                    }
                }

                updateOrderTable();
                itemSelect.value = '';
                quantity.value = 1;
            }

            async function removeItem(index) {
                const item = orderItems[index];

                try {
                    const confirmed = await showConfirmDialog(
                        'Remove Item',
                        `Are you sure you want to remove "${item.name}" from this order?`,
                        {
                            confirmText: 'Remove',
                            cancelText: 'Cancel',
                            confirmStyle: 'danger',
                            icon: 'fas fa-trash-alt'
                        }
                    );

                    if (confirmed) {
                        orderItems.splice(index, 1);
                        updateOrderTable();
                        showInfoToast(`${item.name} removed from order`);
                    }
                } catch (error) {
                    console.error('Dialog error:', error);
                    // Fallback to basic confirm
                    if (confirm(`Remove Item?\n\nAre you sure you want to remove "${item.name}" from this order?`)) {
                        orderItems.splice(index, 1);
                        updateOrderTable();
                    }
                }
            }

            function updateOrderTable() {
                const tbody = document.getElementById('orderItems');
                const totalSpan = document.getElementById('orderTotal');
                let total = 0;

                if (orderItems.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center text-secondary py-8">No items added yet</td></tr>';
                    totalSpan.textContent = '0.00';
                    return;
                }

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

            async function submitOrder() {
                const customerId = document.getElementById('customerId').value;

                // Validation
                if (!customerId) {
                    try {
                        showWarningToast('Please select a customer first');
                    } catch (error) {
                        alert('Please select a customer first');
                    }
                    return;
                }

                if (orderItems.length === 0) {
                    try {
                        showWarningToast('Please add at least one item to the order');
                    } catch (error) {
                        alert('Please add at least one item to the order');
                    }
                    return;
                }

                const total = orderItems.reduce((sum, item) => sum + (item.quantity * item.price), 0);

                try {
                    // Show confirmation dialog
                    const confirmed = await showConfirmDialog(
                        'Create Order',
                        `Are you sure you want to create this order with ${orderItems.length} item(s) for a total of $${total.toFixed(2)}?`,
                        {
                            confirmText: 'Create Order',
                            cancelText: 'Cancel',
                            confirmStyle: 'success',
                            icon: 'fas fa-shopping-cart'
                        }
                    );

                    if (!confirmed) return;
                } catch (error) {
                    console.error('Dialog error:', error);
                    // Fallback to basic confirm
                    if (!confirm(`Create Order?\n\nAre you sure you want to create this order with ${orderItems.length} item(s) for a total of $${total.toFixed(2)}?`)) {
                        return;
                    }
                }

                // Get the submit button from event context or find it
                const submitBtn = document.querySelector('button[onclick="submitOrder()"]');
                const originalText = submitBtn.innerHTML;
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Creating Order...';
                submitBtn.disabled = true;

                const orderData = {
                    customerId: customerId,
                    items: orderItems.map(item => ({
                        itemId: item.itemId,
                        quantity: item.quantity
                    }))
                };

                try {
                    const response = await fetch('${pageContext.request.contextPath}/orders/create', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(orderData)
                    });

                    if (response.ok) {
                        try {
                            showSuccessToast('Order created successfully! Redirecting...');
                        } catch (error) {
                            alert('Order created successfully! Redirecting...');
                        }
                        setTimeout(() => {
                            window.location.href = '${pageContext.request.contextPath}/orders';
                        }, 1500);
                    } else {
                        const errorText = await response.text();
                        try {
                            showErrorToast(errorText || 'Failed to create order. Please try again.');
                        } catch (error) {
                            alert(errorText || 'Failed to create order. Please try again.');
                        }
                    }
                } catch (error) {
                    console.error('Network error:', error);
                    try {
                        showErrorToast('An error occurred while creating the order. Please try again.');
                    } catch (error) {
                        alert('An error occurred while creating the order. Please try again.');
                    }
                } finally {
                    // Restore button state
                    submitBtn.innerHTML = originalText;
                    submitBtn.disabled = false;
                }
            }
        </script>
    </jsp:attribute>
</t:layout>
