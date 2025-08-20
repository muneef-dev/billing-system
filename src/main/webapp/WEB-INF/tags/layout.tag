<%@tag description="Main Layout" pageEncoding="UTF-8"%>
<%@attribute name="title" required="true"%>
<%@attribute name="layout" required="false"%>
<%@attribute name="content" fragment="true"%>
<%@attribute name="scripts" fragment="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - Pahana Edu Bookshop</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-primary text-primary">
    <c:choose>
        <c:when test="${layout == 'auth'}">
            <!-- Auth Layout -->
            <div class="min-h-screen flex items-center justify-center bg-secondary">
                <jsp:invoke fragment="content"/>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Dashboard Layout -->
            <div class="flex min-h-screen">
                <!-- Sidebar -->
                <jsp:include page="/WEB-INF/components/sidebar.jsp"/>

                <!-- Main Content -->
                <div class="main-content flex-1 flex flex-col">
                    <!-- Top Navigation -->
                    <jsp:include page="/WEB-INF/components/topnav.jsp">
                        <jsp:param name="pageTitle" value="${title}"/>
                    </jsp:include>

                    <!-- Page Content -->
                    <main class="flex-1 p-6 bg-secondary animate-fadeIn">
                        <jsp:invoke fragment="content"/>
                    </main>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Theme Toggle Script -->
    <script>
        // Theme management
        const html = document.documentElement;

        // Check for saved theme preference or default to light
        const currentTheme = localStorage.getItem('theme') || 'light';
        html.classList.toggle('dark', currentTheme === 'dark');

        // Theme toggle functionality
        function toggleTheme() {
            const isDark = html.classList.contains('dark');
            html.classList.toggle('dark', !isDark);
            localStorage.setItem('theme', !isDark ? 'dark' : 'light');

            // Update theme toggle icon
            updateThemeIcon();
        }

        function updateThemeIcon() {
            const themeToggle = document.getElementById('themeToggle');
            if (themeToggle) {
                const isDark = html.classList.contains('dark');
                themeToggle.innerHTML = isDark ?
                    '<i class="fas fa-sun mr-2"></i>Light Mode' :
                    '<i class="fas fa-moon mr-2"></i>Dark Mode';
            }
        }

        // Initialize theme icon on page load
        document.addEventListener('DOMContentLoaded', function() {
            updateThemeIcon();

            // Add loading animations to cards
            const cards = document.querySelectorAll('.card');
            cards.forEach((card, index) => {
                setTimeout(() => {
                    card.classList.add('animate-fadeIn');
                }, index * 100);
            });
        });
    </script>

    <jsp:invoke fragment="scripts"/>
</body>
</html>
