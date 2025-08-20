<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar card shadow-sm border-0 border-b border-color">
    <div class="flex justify-between items-center p-4">
        <!-- Page Title -->
        <div class="flex items-center space-x-4">
            <h1 class="text-xl font-semibold text-primary">${param.pageTitle != null ? param.pageTitle : 'Dashboard'}</h1>
            <div class="hidden md:flex items-center text-sm text-secondary">
                <i class="fas fa-calendar mr-2"></i>
                <span id="currentDate"></span>
            </div>
        </div>

        <!-- Right Side Controls -->
        <div class="flex items-center space-x-4">
            <!-- Theme Toggle Button -->
            <button id="themeToggle" onclick="toggleTheme()"
                    class="flex items-center px-3 py-2 rounded-lg text-sm font-medium transition-all duration-200 hover:bg-secondary focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
                <i class="fas fa-moon mr-2"></i>
                <span>Dark Mode</span>
            </button>

            <!-- Notifications -->
            <button class="relative p-2 text-secondary hover:text-primary transition-colors rounded-lg hover:bg-secondary focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
                <i class="fas fa-bell text-lg"></i>
                <span class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">3</span>
            </button>

            <!-- User Menu -->
            <div class="relative">
                <button id="userMenuButton" class="flex items-center space-x-3 p-2 rounded-lg hover:bg-secondary transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
                    <div class="text-right hidden sm:block">
                        <div class="text-sm font-medium text-primary">Welcome back!</div>
                        <div class="text-xs text-secondary">${sessionScope.user.username}</div>
                    </div>
                    <div class="w-10 h-10 bg-gradient-to-br from-blue-500 to-purple-600 rounded-full flex items-center justify-center text-white font-semibold">
                        ${sessionScope.user.username.substring(0,1).toUpperCase()}
                    </div>
                    <i class="fas fa-chevron-down text-xs text-secondary"></i>
                </button>

                <!-- User Dropdown Menu -->
                <div id="userMenu" class="absolute right-0 mt-2 w-48 card shadow-lg rounded-lg border border-color hidden z-50">
                    <div class="py-2">
                        <a href="${pageContext.request.contextPath}/profile"
                           class="flex items-center px-4 py-2 text-sm text-primary hover:bg-secondary transition-colors">
                            <i class="fas fa-user mr-3 text-secondary"></i>
                            <span>Profile Settings</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/settings"
                           class="flex items-center px-4 py-2 text-sm text-primary hover:bg-secondary transition-colors">
                            <i class="fas fa-cog mr-3 text-secondary"></i>
                            <span>Preferences</span>
                        </a>
                        <div class="border-t border-color my-2"></div>
                        <a href="${pageContext.request.contextPath}/auth/logout"
                           class="flex items-center px-4 py-2 text-sm text-danger hover:bg-danger-light transition-colors">
                            <i class="fas fa-sign-out-alt mr-3"></i>
                            <span>Sign Out</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</nav>

<script>
    // Update current date
    document.addEventListener('DOMContentLoaded', function() {
        const dateElement = document.getElementById('currentDate');
        if (dateElement) {
            const now = new Date();
            const options = {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            };
            dateElement.textContent = now.toLocaleDateString('en-US', options);
        }
    });

    // User menu toggle
    const userMenuButton = document.getElementById('userMenuButton');
    const userMenu = document.getElementById('userMenu');

    if (userMenuButton && userMenu) {
        userMenuButton.addEventListener('click', () => {
            userMenu.classList.toggle('hidden');
        });

        // Close menu when clicking outside
        document.addEventListener('click', (event) => {
            if (!userMenuButton.contains(event.target) && !userMenu.contains(event.target)) {
                userMenu.classList.add('hidden');
            }
        });
    }
</script>
