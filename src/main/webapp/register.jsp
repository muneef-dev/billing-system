<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Create Account" layout="auth">
    <jsp:attribute name="content">
        <div class="card p-8 w-96">
            <h1 class="text-2xl font-bold mb-6 text-center text-primary">Create Account</h1>
            <form action="/auth/register" method="POST">
                <div class="mb-4">
                    <div class="flex items-center space-x-2 mb-2">
                        <i class="fa-solid fa-envelope text-secondary"></i>
                        <label for="email" class="form-label">Email</label>
                    </div>
                    <input type="email" id="email" name="email"
                           class="form-input" required placeholder="Enter your email address">
                </div>
                <div class="mb-4">
                    <div class="flex items-center space-x-2 mb-2">
                        <i class="fa-solid fa-user text-secondary"></i>
                        <label for="username" class="form-label">Username (Optional)</label>
                    </div>
                    <input type="text" id="username" name="username"
                           class="form-input" placeholder="Enter a username (optional)">
                </div>
                <div class="mb-4">
                    <div class="flex items-center space-x-2 mb-2">
                        <i class="fa-solid fa-lock text-secondary"></i>
                        <label for="password" class="form-label">Password</label>
                    </div>
                    <input type="password" id="password" name="password"
                           class="form-input" required>
                </div>
                <div class="mb-4">
                    <div class="flex items-center space-x-2 mb-2">
                        <i class="fa-solid fa-user-tag text-secondary"></i>
                        <label for="role" class="form-label">Role</label>
                    </div>
                    <select id="role" name="role" class="form-input">
                        <option value="user">User</option>
                        <option value="admin">Admin</option>
                    </select>
                </div>
                <div class="text-danger text-sm mb-4">
                    <c:if test="${not empty requestScope.error}">
                        <i class="fas fa-exclamation-triangle mr-2"></i>${requestScope.error}
                    </c:if>
                </div>
                <button type="submit" class="btn btn-primary w-full mb-4">
                    <i class="fas fa-user-plus mr-2"></i>Create Account
                </button>
            </form>
            <div class="text-center">
                <a href="/login.jsp" class="text-primary hover:text-primary-hover transition-colors">
                    <i class="fas fa-sign-in-alt mr-2"></i>Already have an account? Login
                </a>
            </div>
        </div>
    </jsp:attribute>
</t:layout>
