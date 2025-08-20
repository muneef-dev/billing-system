<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Forgot Password" layout="auth">
    <jsp:attribute name="content">
        <div class="card p-8 w-96">
            <!-- Step 1: Email Input -->
            <div id="emailStep" class="step-container">
                <div class="text-center mb-6">
                    <i class="fas fa-key text-4xl text-primary mb-4"></i>
                    <h1 class="text-2xl font-bold text-primary">Forgot Password?</h1>
                    <p class="text-secondary mt-2">Don't worry! Enter your email and we'll send you a reset code.</p>
                </div>

                <form id="emailForm" action="${pageContext.request.contextPath}/forgot-password" method="POST">
                    <input type="hidden" name="step" value="email">

                    <div class="mb-4">
                        <div class="flex items-center space-x-2 mb-2">
                            <i class="fas fa-envelope text-secondary"></i>
                            <label for="email" class="form-label">Email Address</label>
                        </div>
                        <input type="email" id="email" name="email"
                               class="form-input" required
                               placeholder="Enter your registered email">
                    </div>

                    <div class="text-danger text-sm mb-4" id="emailError">
                        <c:if test="${not empty requestScope.error && param.step == 'email'}">
                            <i class="fas fa-exclamation-triangle mr-2"></i>${requestScope.error}
                        </c:if>
                    </div>

                    <button type="submit" class="btn btn-primary w-full mb-4" id="sendOtpBtn">
                        <i class="fas fa-paper-plane mr-2"></i>Send Reset Code
                    </button>
                </form>
            </div>

            <!-- Step 2: OTP Verification -->
            <div id="otpStep" class="step-container hidden">
                <div class="text-center mb-6">
                    <i class="fas fa-shield-alt text-4xl text-success mb-4"></i>
                    <h1 class="text-2xl font-bold text-primary">Verify Code</h1>
                    <p class="text-secondary mt-2">Enter the 6-digit code sent to your email</p>
                    <p class="text-sm text-primary mt-1" id="emailDisplay"></p>
                </div>

                <form id="otpForm" action="${pageContext.request.contextPath}/forgot-password" method="POST">
                    <input type="hidden" name="step" value="verify">
                    <input type="hidden" name="email" id="hiddenEmail">

                    <div class="mb-4">
                        <div class="flex items-center space-x-2 mb-2">
                            <i class="fas fa-lock text-secondary"></i>
                            <label for="otp" class="form-label">Verification Code</label>
                        </div>
                        <input type="text" id="otp" name="otp"
                               class="form-input text-center tracking-widest text-lg"
                               maxlength="6" required
                               placeholder="123456">
                    </div>

                    <div class="text-danger text-sm mb-4" id="otpError">
                        <c:if test="${not empty requestScope.error && param.step == 'verify'}">
                            <i class="fas fa-exclamation-triangle mr-2"></i>${requestScope.error}
                        </c:if>
                    </div>

                    <button type="submit" class="btn btn-success w-full mb-4" id="verifyOtpBtn">
                        <i class="fas fa-check mr-2"></i>Verify Code
                    </button>

                    <div class="text-center">
                        <button type="button" class="text-secondary hover:text-primary transition-colors text-sm"
                                id="resendOtpBtn">
                            <i class="fas fa-redo mr-1"></i>Resend Code
                        </button>
                        <span class="text-sm text-secondary ml-2" id="countdown"></span>
                    </div>
                </form>
            </div>

            <!-- Step 3: Reset Password -->
            <div id="resetStep" class="step-container hidden">
                <div class="text-center mb-6">
                    <i class="fas fa-key text-4xl text-warning mb-4"></i>
                    <h1 class="text-2xl font-bold text-primary">Reset Password</h1>
                    <p class="text-secondary mt-2">Enter your new password</p>
                </div>

                <form id="resetForm" action="${pageContext.request.contextPath}/forgot-password" method="POST">
                    <input type="hidden" name="step" value="reset">
                    <input type="hidden" name="email" id="resetEmail">
                    <input type="hidden" name="otp" id="resetOtp">

                    <div class="mb-4">
                        <div class="flex items-center space-x-2 mb-2">
                            <i class="fas fa-lock text-secondary"></i>
                            <label for="newPassword" class="form-label">New Password</label>
                        </div>
                        <input type="password" id="newPassword" name="newPassword"
                               class="form-input" required minlength="4"
                               placeholder="Enter new password">
                    </div>

                    <div class="mb-4">
                        <div class="flex items-center space-x-2 mb-2">
                            <i class="fas fa-lock text-secondary"></i>
                            <label for="confirmPassword" class="form-label">Confirm Password</label>
                        </div>
                        <input type="password" id="confirmPassword" name="confirmPassword"
                               class="form-input" required minlength="4"
                               placeholder="Confirm new password">
                    </div>

                    <div class="text-danger text-sm mb-4" id="resetError">
                        <c:if test="${not empty requestScope.error && param.step == 'reset'}">
                            <i class="fas fa-exclamation-triangle mr-2"></i>${requestScope.error}
                        </c:if>
                    </div>

                    <button type="submit" class="btn btn-warning w-full mb-4" id="resetPasswordBtn">
                        <i class="fas fa-save mr-2"></i>Reset Password
                    </button>
                </form>
            </div>

            <!-- Back to Login -->
            <div class="text-center mt-6">
                <a href="${pageContext.request.contextPath}/login.jsp"
                   class="text-primary hover:text-primary-hover transition-colors">
                    <i class="fas fa-arrow-left mr-2"></i>Back to Login
                </a>
            </div>
        </div>

        <!-- Success Step -->
        <div id="successStep" class="card p-8 w-96 hidden">
            <div class="text-center">
                <i class="fas fa-check-circle text-5xl text-success mb-4"></i>
                <h1 class="text-2xl font-bold text-primary mb-4">Password Reset Successful!</h1>
                <p class="text-secondary mb-6">Your password has been successfully reset. You can now log in with your new password.</p>

                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary w-full">
                    <i class="fas fa-sign-in-alt mr-2"></i>Go to Login
                </a>
            </div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function() {
                let currentStep = 1;
                let resendTimer = 0;
                let resendInterval;

                // Step management
                const steps = {
                    1: document.getElementById('emailStep'),
                    2: document.getElementById('otpStep'),
                    3: document.getElementById('resetStep'),
                    4: document.getElementById('successStep')
                };

                // Check URL parameters to determine current step
                const urlParams = new URLSearchParams(window.location.search);
                const step = urlParams.get('step');
                const email = urlParams.get('email');
                const success = urlParams.get('success');

                if (success === 'true') {
                    showStep(4);
                } else if (step === 'verify' && email) {
                    showStep(2);
                    document.getElementById('hiddenEmail').value = email;
                    document.getElementById('emailDisplay').textContent = email;
                    startResendTimer();
                } else if (step === 'reset' && email) {
                    showStep(3);
                    document.getElementById('resetEmail').value = email;
                }

                function showStep(stepNumber) {
                    currentStep = stepNumber;
                    Object.values(steps).forEach(step => step.classList.add('hidden'));
                    if (steps[stepNumber]) {
                        steps[stepNumber].classList.remove('hidden');
                    }
                }

                // Form submissions with loading states
                document.getElementById('emailForm').addEventListener('submit', function(e) {
                    const btn = document.getElementById('sendOtpBtn');
                    btn.disabled = true;
                    btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Sending...';
                });

                document.getElementById('otpForm').addEventListener('submit', function(e) {
                    const btn = document.getElementById('verifyOtpBtn');
                    btn.disabled = true;
                    btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Verifying...';
                });

                document.getElementById('resetForm').addEventListener('submit', function(e) {
                    const newPassword = document.getElementById('newPassword').value;
                    const confirmPassword = document.getElementById('confirmPassword').value;

                    if (newPassword !== confirmPassword) {
                        e.preventDefault();
                        document.getElementById('resetError').innerHTML =
                            '<i class="fas fa-exclamation-triangle mr-2"></i>Passwords do not match';
                        return;
                    }

                    const btn = document.getElementById('resetPasswordBtn');
                    btn.disabled = true;
                    btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Resetting...';
                });

                // OTP input formatting
                document.getElementById('otp').addEventListener('input', function(e) {
                    this.value = this.value.replace(/\D/g, '');
                });

                // Password confirmation validation
                document.getElementById('confirmPassword').addEventListener('input', function() {
                    const newPassword = document.getElementById('newPassword').value;
                    const confirmPassword = this.value;

                    if (newPassword !== confirmPassword) {
                        this.setCustomValidity('Passwords do not match');
                    } else {
                        this.setCustomValidity('');
                    }
                });

                // Resend OTP functionality
                document.getElementById('resendOtpBtn').addEventListener('click', function() {
                    const email = document.getElementById('hiddenEmail').value;

                    // Send resend request
                    fetch('${pageContext.request.contextPath}/forgot-password', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'step=resend&email=' + encodeURIComponent(email)
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            startResendTimer();
                            if (typeof showSuccessToast !== 'undefined') {
                                showSuccessToast('Verification code resent successfully');
                            }
                        } else {
                            if (typeof showErrorToast !== 'undefined') {
                                showErrorToast(data.message || 'Failed to resend code');
                            }
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        if (typeof showErrorToast !== 'undefined') {
                            showErrorToast('Failed to resend code');
                        }
                    });
                });

                function startResendTimer() {
                    resendTimer = 60;
                    const resendBtn = document.getElementById('resendOtpBtn');
                    const countdown = document.getElementById('countdown');

                    resendBtn.disabled = true;
                    resendBtn.classList.add('opacity-50', 'cursor-not-allowed');

                    resendInterval = setInterval(() => {
                        countdown.textContent = `(${resendTimer}s)`;
                        resendTimer--;

                        if (resendTimer < 0) {
                            clearInterval(resendInterval);
                            resendBtn.disabled = false;
                            resendBtn.classList.remove('opacity-50', 'cursor-not-allowed');
                            countdown.textContent = '';
                        }
                    }, 1000);
                }
            });
        </script>
    </jsp:attribute>
</t:layout>
