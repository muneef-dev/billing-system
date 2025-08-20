<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Login" layout="auth">
    <jsp:attribute name="content">
        <div class="card p-8 w-96">
            <h1 class="text-2xl font-bold mb-6 text-center text-primary">Login</h1>
            <form action="/auth/login" method="POST">
                <div class="mb-4">
                    <div class="flex items-center space-x-2 mb-2">
                        <i class="fa-solid fa-user text-secondary"></i>
                        <label for="username" class="form-label">Username</label>
                    </div>
                    <input type="text" id="username" name="username" value="moha1234"
                           class="form-input" required>
                </div>
                <div class="mb-4">
                    <div class="flex items-center space-x-2 mb-2">
                        <i class="fa-solid fa-lock text-secondary"></i>
                        <label for="password" class="form-label">Password</label>
                    </div>
                    <input type="password" id="password" name="password" value="1234"
                           class="form-input" required>
                </div>
                <div class="text-danger text-sm mb-4">
                    <c:if test="${not empty requestScope.error}">
                        <i class="fas fa-exclamation-triangle mr-2"></i>${requestScope.error}
                    </c:if>
                </div>
                <button type="submit" class="btn btn-primary w-full mb-4">
                    <i class="fas fa-sign-in-alt mr-2"></i>Login
                </button>
            </form>
            <div class="text-center">
                <a href="/register.jsp" class="text-primary hover:text-primary-hover transition-colors">
                    <i class="fas fa-user-plus mr-2"></i>Create a new account
                </a>
            </div>
        </div>
    </jsp:attribute>
</t:layout>
