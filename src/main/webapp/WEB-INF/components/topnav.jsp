<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="bg-white shadow p-4 flex justify-between items-center">
    <h1 class="text-xl font-bold">${param.pageTitle != null ? param.pageTitle : 'Pahana Edu Bookshop'}</h1>
    <div class="relative">
        <button id="userMenuButton" class="flex items-center space-x-2">
            <span class="text-gray-700">Welcome, ${sessionScope.user.username}</span>
            <img src="${pageContext.request.contextPath}/assets/user-icon.png" alt="User Icon" class="w-8 h-8 rounded-full">
        </button>
        <div id="userMenu" class="absolute right-0 mt-2 bg-white shadow rounded hidden">
            <a href="${pageContext.request.contextPath}/auth/logout" class="block px-4 py-2 text-gray-700 hover:bg-gray-100">Logout</a>
        </div>
    </div>
</div>

<script>
    const userMenuButton = document.getElementById('userMenuButton');
    const userMenu = document.getElementById('userMenu');

    userMenuButton.addEventListener('click', () => {
        userMenu.classList.toggle('hidden');
    });

    // Close menu when clicking outside
    document.addEventListener('click', (event) => {
        if (!userMenuButton.contains(event.target) && !userMenu.contains(event.target)) {
            userMenu.classList.add('hidden');
        }
    });
</script>
