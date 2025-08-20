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

                    <div>
                        <label for="accountNumber" class="form-label">
                            Account Number
                        </label>
                        <input type="text"
                               id="accountNumber"
                               name="accountNumber"
                               value="${customer.accountNumber}"
                               required
                               class="form-input">
                    </div>

                    <div>
                        <label for="name" class="form-label">
                            Name
                        </label>
                        <input type="text"
                               id="name"
                               name="name"
                               value="${customer.name}"
                               required
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
                            Telephone
                        </label>
                        <input type="tel"
                               id="telephone"
                               name="telephone"
                               value="${customer.telephone}"
                               pattern="[0-9]{10}"
                               title="Please enter a valid 10-digit phone number"
                               class="form-input">
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
