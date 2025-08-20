<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="User Profile" layout="dashboard">
    <jsp:attribute name="content">
        <div class="container mx-auto px-6 py-8">
            <!-- Page Header -->
            <div class="mb-8">
                <h1 class="text-3xl font-bold text-primary mb-2">User Profile</h1>
                <p class="text-secondary">Manage your account information and settings</p>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <!-- Profile Info Card -->
                <div class="lg:col-span-2">
                    <div class="card p-6">
                        <div class="flex items-center justify-between mb-6">
                            <h2 class="text-xl font-semibold text-primary">Account Information</h2>
                            <button id="editBtn" class="btn btn-primary">
                                <i class="fas fa-edit mr-2"></i>Edit Profile
                            </button>
                        </div>

                        <!-- Profile Form -->
                        <form id="profileForm" action="${pageContext.request.contextPath}/profile/update" method="POST">
                            <input type="hidden" name="action" value="update">

                            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <!-- Email Field -->
                                <div class="md:col-span-2">
                                    <div class="flex items-center space-x-2 mb-2">
                                        <i class="fas fa-envelope text-secondary"></i>
                                        <label for="email" class="form-label">Email Address</label>
                                    </div>
                                    <input type="email" id="email" name="email"
                                           value="${sessionScope.user.email}"
                                           class="form-input" readonly>
                                </div>

                                <!-- Username Field -->
                                <div class="md:col-span-2">
                                    <div class="flex items-center space-x-2 mb-2">
                                        <i class="fas fa-user text-secondary"></i>
                                        <label for="username" class="form-label">Username</label>
                                    </div>
                                    <input type="text" id="username" name="username"
                                           value="${sessionScope.user.username}"
                                           class="form-input" readonly placeholder="Enter username (optional)">
                                </div>

                                <!-- Role Field (Read-only) -->
                                <div>
                                    <div class="flex items-center space-x-2 mb-2">
                                        <i class="fas fa-user-tag text-secondary"></i>
                                        <label for="role" class="form-label">Role</label>
                                    </div>
                                    <input type="text" id="role" name="role"
                                           value="${sessionScope.user.role}"
                                           class="form-input bg-gray-100" readonly>
                                </div>

                                <!-- Created At Field (Read-only) -->
                                <div>
                                    <div class="flex items-center space-x-2 mb-2">
                                        <i class="fas fa-calendar text-secondary"></i>
                                        <label for="createdAt" class="form-label">Member Since</label>
                                    </div>
                                    <input type="text" id="createdAt" name="createdAt"
                                           value="${sessionScope.user.createdAt}"
                                           class="form-input bg-gray-100" readonly>
                                </div>
                            </div>

                            <!-- Form Actions -->
                            <div id="formActions" class="flex justify-end space-x-4 mt-6 hidden">
                                <button type="button" id="cancelBtn" class="btn btn-secondary">
                                    <i class="fas fa-times mr-2"></i>Cancel
                                </button>
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save mr-2"></i>Save Changes
                                </button>
                            </div>
                        </form>

                        <!-- Success/Error Messages are now handled by script.js automatically -->
                    </div>
                </div>

                <!-- Password Change Card -->
                <div class="lg:col-span-1">
                    <div class="card p-6">
                        <h3 class="text-lg font-semibold text-primary mb-4">Change Password</h3>

                        <form id="passwordForm" action="${pageContext.request.contextPath}/profile/change-password" method="POST">
                            <div class="space-y-4">
                                <div>
                                    <label for="currentPassword" class="form-label">Current Password</label>
                                    <input type="password" id="currentPassword" name="currentPassword"
                                           class="form-input" required>
                                </div>

                                <div>
                                    <label for="newPassword" class="form-label">New Password</label>
                                    <input type="password" id="newPassword" name="newPassword"
                                           class="form-input" required>
                                </div>

                                <div>
                                    <label for="confirmPassword" class="form-label">Confirm New Password</label>
                                    <input type="password" id="confirmPassword" name="confirmPassword"
                                           class="form-input" required>
                                </div>

                                <button type="submit" class="btn btn-warning w-full">
                                    <i class="fas fa-lock mr-2"></i>Update Password
                                </button>
                            </div>
                        </form>
                    </div>

                    <!-- Account Stats Card -->
                    <div class="card p-6 mt-6">
                        <h3 class="text-lg font-semibold text-primary mb-4">Account Statistics</h3>

                        <div class="space-y-3">
                            <div class="flex justify-between">
                                <span class="text-secondary">Last Login:</span>
                                <span class="text-primary font-medium">
                                    ${sessionScope.user.lastLogin != null ? sessionScope.user.lastLogin : 'N/A'}
                                </span>
                            </div>
                            <div class="flex justify-between">
                                <span class="text-secondary">Account Status:</span>
                                <span class="badge badge-success">Active</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- JavaScript for Profile Editing -->
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                // Check if functions are available
                if (typeof showConfirmDialog === 'undefined' || typeof showErrorToast === 'undefined') {
                    console.error('Toast and dialog functions not loaded. Please check script.js');
                    return;
                }

                const editBtn = document.getElementById('editBtn');
                const cancelBtn = document.getElementById('cancelBtn');
                const formActions = document.getElementById('formActions');
                const emailInput = document.getElementById('email');
                const usernameInput = document.getElementById('username');
                const profileForm = document.getElementById('profileForm');
                const passwordForm = document.getElementById('passwordForm');

                // Store original values
                const originalEmail = emailInput.value;
                const originalUsername = usernameInput.value;

                editBtn.addEventListener('click', function() {
                    // Enable editing
                    emailInput.readOnly = false;
                    usernameInput.readOnly = false;

                    // Show form actions
                    formActions.classList.remove('hidden');
                    editBtn.style.display = 'none';
                });

                cancelBtn.addEventListener('click', function() {
                    // Reset to original values
                    emailInput.value = originalEmail;
                    usernameInput.value = originalUsername;

                    // Disable editing
                    emailInput.readOnly = true;
                    usernameInput.readOnly = true;

                    // Hide form actions
                    formActions.classList.add('hidden');
                    editBtn.style.display = 'block';
                });

                // Profile form submission with confirmation
                profileForm.addEventListener('submit', async function(e) {
                    e.preventDefault();

                    try {
                        const confirmed = await showConfirmDialog(
                            'Update Profile',
                            'Are you sure you want to update your profile information?',
                            {
                                confirmText: 'Update',
                                cancelText: 'Cancel',
                                confirmStyle: 'primary',
                                icon: 'fas fa-user-edit'
                            }
                        );

                        if (confirmed) {
                            profileForm.submit();
                        }
                    } catch (error) {
                        console.error('Dialog error:', error);
                        if (confirm('Update Profile?\n\nAre you sure you want to update your profile information?')) {
                            profileForm.submit();
                        }
                    }
                });

                // Password form submission with confirmation
                passwordForm.addEventListener('submit', async function(e) {
                    e.preventDefault();

                    const newPassword = document.getElementById('newPassword').value;
                    const confirmPassword = document.getElementById('confirmPassword').value;

                    // Client-side validation
                    if (newPassword !== confirmPassword) {
                        try {
                            showErrorToast('Passwords do not match');
                        } catch (error) {
                            alert('Passwords do not match');
                        }
                        return;
                    }

                    if (newPassword.length < 4) {
                        try {
                            showErrorToast('Password must be at least 4 characters long');
                        } catch (error) {
                            alert('Password must be at least 4 characters long');
                        }
                        return;
                    }

                    try {
                        const confirmed = await showConfirmDialog(
                            'Change Password',
                            'Are you sure you want to change your password? You will need to use the new password for future logins.',
                            {
                                confirmText: 'Change Password',
                                cancelText: 'Cancel',
                                confirmStyle: 'warning',
                                icon: 'fas fa-key'
                            }
                        );

                        if (confirmed) {
                            passwordForm.submit();
                        }
                    } catch (error) {
                        console.error('Dialog error:', error);
                        if (confirm('Change Password?\n\nAre you sure you want to change your password?')) {
                            passwordForm.submit();
                        }
                    }
                });

                // Password confirmation validation
                const newPasswordInput = document.getElementById('newPassword');
                const confirmPasswordInput = document.getElementById('confirmPassword');

                confirmPasswordInput.addEventListener('input', function() {
                    if (newPasswordInput.value !== confirmPasswordInput.value) {
                        confirmPasswordInput.setCustomValidity('Passwords do not match');
                    } else {
                        confirmPasswordInput.setCustomValidity('');
                    }
                });
            });
        </script>
    </jsp:attribute>
</t:layout>
