<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Help" layout="dashboard">
    <jsp:attribute name="content">
        <div class="max-w-4xl mx-auto">
            <div class="p-6">
                <h1 class="text-3xl font-bold mb-4">Help Section</h1>
                <p class="text-gray-700 mb-4">Welcome to the help section. This guide provides detailed instructions on using the billing system effectively.</p>

                <h2 class="text-xl font-semibold mb-2">Login and Registration</h2>
                <p class="text-gray-700 mb-4">To access the system, log in using your credentials. If you are a new user, register by clicking the "Register" link on the login page.</p>
                <ul class="list-disc pl-6">
                    <li><a href="<c:url value='/login'/>" class="text-blue-600 hover:text-blue-800">Login</a>: Enter your username and password to access the system.</li>
                    <li><a href="<c:url value='/register'/>" class="text-blue-600 hover:text-blue-800">Register</a>: Fill out the registration form to create a new account.</li>
                </ul>

                <h2 class="text-xl font-semibold mb-2">Dashboard Navigation</h2>
                <p class="text-gray-700 mb-4">The dashboard provides an overview of your business and quick access to all system features.</p>
                <ul class="list-disc pl-6">
                    <li><a href="<c:url value='/dashboard'/>" class="text-blue-600 hover:text-blue-800">Dashboard Overview</a>: View statistics, charts, and quick actions.</li>
                </ul>

                <h2 class="text-xl font-semibold mb-2">Customer Management</h2>
                <p class="text-gray-700 mb-4">Manage customers by navigating to the "Customers" section. You can add, edit, and view customer details.</p>
                <ul class="list-disc pl-6">
                    <li><a href="<c:url value='/customers/new'/>" class="text-blue-600 hover:text-blue-800">Add New Customer</a>: Fill out the form to add a new customer.</li>
                    <li><a href="<c:url value='/customers'/>" class="text-blue-600 hover:text-blue-800">View All Customers</a>: Browse and manage existing customers.</li>
                </ul>

                <h2 class="text-xl font-semibold mb-2">Item Management</h2>
                <p class="text-gray-700 mb-4">Manage inventory items by navigating to the "Items" section. You can add, edit, delete, and list items.</p>
                <ul class="list-disc pl-6">
                    <li><a href="<c:url value='/items/new'/>" class="text-blue-600 hover:text-blue-800">Add New Item</a>: Enter details to add a new inventory item.</li>
                    <li><a href="<c:url value='/items'/>" class="text-blue-600 hover:text-blue-800">View All Items</a>: Manage and update existing inventory.</li>
                </ul>

                <h2 class="text-xl font-semibold mb-2">Order/Billing System</h2>
                <p class="text-gray-700 mb-4">Create and manage orders by navigating to the "Orders" section. Generate receipts for completed orders.</p>
                <ul class="list-disc pl-6">
                    <li><a href="<c:url value='/orders/new'/>" class="text-blue-600 hover:text-blue-800">Create New Order</a>: Add items and customer details to create an order.</li>
                    <li><a href="<c:url value='/orders'/>" class="text-blue-600 hover:text-blue-800">View All Orders</a>: Browse and manage existing orders.</li>
                </ul>

                <h2 class="text-xl font-semibold mb-2">Support and Troubleshooting</h2>
                <p class="text-gray-700 mb-4">If you encounter issues, refer to this section for solutions or contact support.</p>
                <ul class="list-disc pl-6">
                    <li><a href="<c:url value='/help'/>" class="text-blue-600 hover:text-blue-800">Help Overview</a>: General guidance and FAQs.</li>
                    <li>Email: <a href="mailto:support@pahanabookshop.com" class="text-blue-600 hover:text-blue-800">support@pahanabookshop.com</a></li>
                    <li>Phone: (555) 123-4567</li>
                </ul>
            </div>
        </div>
    </jsp:attribute>
</t:layout>
