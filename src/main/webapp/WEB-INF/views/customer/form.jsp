<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="${customer == null ? 'Add New' : 'Edit'} Customer" layout="dashboard">
    <jsp:attribute name="content">
        <div class="max-w-3xl mx-auto">
            <div class="flex justify-between items-center mb-6">
                <h1 class="text-2xl font-bold text-primary">${customer == null ? 'Add New' : 'Edit'} Customer</h1>
                <a href="${pageContext.request.contextPath}/customers"
                   class="text-primary hover:text-primary-hover transition-colors">
                    Back to List
                </a>
            </div>

            <c:if test="${error != null}">
                <div class="card p-4 mb-4 bg-danger-light border-danger text-danger">
                    <i class="fas fa-exclamation-triangle mr-2"></i>${error}
                </div>
            </c:if>

            <div class="card p-8">
                <form action="${pageContext.request.contextPath}/customers${customer == null ? '' : '/edit/'.concat(customer.id)}"
                      method="POST" class="space-y-6">

                    <!-- Remove account number field for new customers since it's auto-generated -->
                    <c:if test="${customer != null}">
                        <div>
                            <label for="accountNumber" class="form-label">
                                Account Number
                            </label>
                            <input type="text"
                                   id="accountNumber"
                                   name="accountNumber"
                                   value="${customer.accountNumber}"
                                   readonly
                                   class="form-input bg-gray-100">
                            <p class="text-sm text-secondary mt-1">Account number is automatically generated and cannot be changed.</p>
                        </div>
                    </c:if>

                    <div>
                        <label for="name" class="form-label">
                            Name <span class="text-red-500">*</span>
                        </label>
                        <input type="text"
                               id="name"
                               name="name"
                               value="${customer.name}"
                               required
                               class="form-input">
                    </div>

                    <div>
                        <label for="email" class="form-label">
                            Email Address
                        </label>
                        <input type="email"
                               id="email"
                               name="email"
                               value="${customer.email}"
                               class="form-input">
                    </div>

                    <div>
                        <label for="address" class="form-label">
                            Address
                        </label>
                        <textarea id="address"
                                  name="address"
                                  rows="3"
                                  class="form-input">${customer.address}</textarea>
                    </div>

                    <div>
                        <label for="telephone" class="form-label">
                            Telephone <span class="text-red-500">*</span>
                        </label>
                        <input type="tel"
                               id="telephone"
                               name="telephone"
                               value="${customer.telephone}"
                               required
                               class="form-input"
                               placeholder="Enter phone number">
                    </div>

                    <div>
                        <label for="notes" class="form-label">
                            Notes
                        </label>
                        <textarea id="notes"
                                  name="notes"
                                  rows="2"
                                  class="form-input"
                                  placeholder="Optional notes about the customer">${customer.notes}</textarea>
                    </div>

                    <div class="flex items-center justify-between pt-4">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save mr-2"></i>${customer == null ? 'Create' : 'Update'} Customer
                        </button>
                        <button type="reset" class="btn btn-secondary">
                            <i class="fas fa-undo mr-2"></i>Reset
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </jsp:attribute>
</t:layout>
