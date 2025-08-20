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

                    <!-- Remove item code field for new items since it's auto-generated -->
                    <c:if test="${item != null}">
                        <div>
                            <label for="itemCode" class="form-label">
                                Item Code
                            </label>
                            <input type="text"
                                   id="itemCode"
                                   name="itemCode"
                                   value="${item.itemCode}"
                                   readonly
                                   class="form-input bg-gray-100">
                            <p class="text-sm text-secondary mt-1">Item code is automatically generated and cannot be changed.</p>
                        </div>
                    </c:if>

                    <div>
                        <label for="itemName" class="form-label">
                            Item Name <span class="text-red-500">*</span>
                        </label>
                        <input type="text"
                               id="itemName"
                               name="itemName"
                               value="${item.itemName}"
                               required
                               class="form-input">
                    </div>

                    <div>
                        <label for="category" class="form-label">
                            Category
                        </label>
                        <input type="text"
                               id="category"
                               name="category"
                               value="${item.category}"
                               class="form-input"
                               placeholder="e.g., Fiction, Non-Fiction, Textbooks">
                    </div>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <label for="author" class="form-label">
                                Author
                            </label>
                            <input type="text"
                                   id="author"
                                   name="author"
                                   value="${item.author}"
                                   class="form-input">
                        </div>

                        <div>
                            <label for="publisher" class="form-label">
                                Publisher
                            </label>
                            <input type="text"
                                   id="publisher"
                                   name="publisher"
                                   value="${item.publisher}"
                                   class="form-input">
                        </div>
                    </div>

                    <div>
                        <label for="description" class="form-label">
                            Description
                        </label>
                        <textarea id="description"
                                  name="description"
                                  rows="3"
                                  class="form-input"
                                  placeholder="Brief description of the item">${item.description}</textarea>
                    </div>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <label for="unitPrice" class="form-label">
                                Unit Price <span class="text-red-500">*</span>
                            </label>
                            <input type="number"
                                   id="unitPrice"
                                   name="unitPrice"
                                   value="${item.unitPrice}"
                                   step="0.01"
                                   min="0"
                                   required
                                   class="form-input">
                        </div>

                        <div>
                            <label for="costPrice" class="form-label">
                                Cost Price
                            </label>
                            <input type="number"
                                   id="costPrice"
                                   name="costPrice"
                                   value="${item.costPrice}"
                                   step="0.01"
                                   min="0"
                                   class="form-input">
                        </div>
                    </div>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <label for="stockQuantity" class="form-label">
                                Stock Quantity <span class="text-red-500">*</span>
                            </label>
                            <input type="number"
                                   id="stockQuantity"
                                   name="stockQuantity"
                                   value="${item.stockQuantity}"
                                   min="0"
                                   required
                                   class="form-input">
                        </div>

                        <div>
                            <label for="minimumStockLevel" class="form-label">
                                Minimum Stock Level
                            </label>
                            <input type="number"
                                   id="minimumStockLevel"
                                   name="minimumStockLevel"
                                   value="${item.minimumStockLevel}"
                                   min="0"
                                   class="form-input"
                                   placeholder="Default: 5">
                        </div>
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
