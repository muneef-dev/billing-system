<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="${item == null ? 'Add New' : 'Edit'} Item" layout="dashboard">
    <jsp:attribute name="content">
        <div class="max-w-3xl mx-auto">
            <div class="flex justify-between items-center mb-6">
                <h1 class="text-2xl font-bold text-primary">${item == null ? 'Add New' : 'Edit'} Item</h1>
                <a href="${pageContext.request.contextPath}/items"
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
                <form action="${pageContext.request.contextPath}/items${item == null ? '' : '/edit/'.concat(item.id)}"
                      method="POST" class="space-y-6">

                    <div>
                        <label for="itemCode" class="form-label">
                            Item Code
                        </label>
                        <input type="text"
                               id="itemCode"
                               name="itemCode"
                               value="${item.itemCode}"
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
                               value="${item.name}"
                               required
                               class="form-input">
                    </div>

                    <div>
                        <label for="description" class="form-label">
                            Description
                        </label>
                        <textarea id="description"
                                  name="description"
                                  rows="3"
                                  class="form-input">${item.description}</textarea>
                    </div>

                    <div>
                        <label for="price" class="form-label">
                            Price
                        </label>
                        <input type="number"
                               id="price"
                               name="price"
                               value="${item.price}"
                               step="0.01"
                               min="0"
                               required
                               class="form-input">
                    </div>

                    <div>
                        <label for="stockQuantity" class="form-label">
                            Stock Quantity
                        </label>
                        <input type="number"
                               id="stockQuantity"
                               name="stockQuantity"
                               value="${item.stockQuantity}"
                               min="0"
                               required
                               class="form-input">
                    </div>

                    <div class="flex items-center justify-between pt-4">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save mr-2"></i>${item == null ? 'Create' : 'Update'} Item
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
