/**
 * Common JavaScript utilities for the billing system
 * Provides toast notifications and confirmation dialogs
 */

// Toast notification system
class ToastManager {
    constructor() {
        this.toastQueue = [];
        this.maxToasts = 5;
        this.containerCreated = false;

        // Only create container if DOM is ready
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => {
                this.createToastContainer();
            });
        } else {
            this.createToastContainer();
        }
    }

    createToastContainer() {
        if (this.containerCreated || document.getElementById('toast-container')) {
            this.containerCreated = true;
            return;
        }

        // Ensure document.body exists
        if (!document.body) {
            console.error('Document body not available yet');
            setTimeout(() => this.createToastContainer(), 100);
            return;
        }

        const container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'fixed top-4 right-4 z-50 space-y-2';
        container.style.cssText = `
            position: fixed;
            top: 1rem;
            right: 1rem;
            z-index: 9999;
            pointer-events: none;
        `;
        document.body.appendChild(container);
        this.containerCreated = true;
    }

    ensureContainer() {
        if (!this.containerCreated || !document.getElementById('toast-container')) {
            this.createToastContainer();
        }
    }

    show(type, message, duration = 5000) {
        this.ensureContainer();

        const container = document.getElementById('toast-container');
        if (!container) {
            console.error('Toast container not available');
            return null;
        }

        const toast = this.createToast(type, message, duration);

        // Remove excess toasts
        while (container.children.length >= this.maxToasts) {
            container.removeChild(container.firstChild);
        }

        container.appendChild(toast);

        // Animate in
        setTimeout(() => {
            toast.style.transform = 'translateX(0)';
            toast.style.opacity = '1';
        }, 10);

        // Auto remove
        if (duration > 0) {
            setTimeout(() => {
                this.removeToast(toast);
            }, duration);
        }

        return toast;
    }

    createToast(type, message, duration) {
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;

        const colors = this.getToastColors(type);
        const icon = this.getToastIcon(type);

        toast.style.cssText = `
            background-color: ${colors.bg};
            color: ${colors.text};
            border: 1px solid ${colors.border};
            border-radius: 0.75rem;
            padding: 1rem 1.25rem;
            min-width: 300px;
            max-width: 500px;
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            transform: translateX(100%);
            opacity: 0;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            pointer-events: auto;
            position: relative;
            backdrop-filter: blur(8px);
        `;

        toast.innerHTML = `
            <div class="flex items-start space-x-3">
                <div class="flex-shrink-0 mt-0.5">
                    <i class="${icon}" style="color: ${colors.icon}; font-size: 1.25rem;"></i>
                </div>
                <div class="flex-1 min-w-0">
                    <p class="text-sm font-medium" style="color: ${colors.text}; margin: 0; line-height: 1.4;">
                        ${this.escapeHtml(message)}
                    </p>
                </div>
                <button class="toast-close-btn flex-shrink-0 ml-4 text-gray-400 hover:text-gray-600 transition-colors" 
                        style="background: none; border: none; cursor: pointer; padding: 0; font-size: 1.25rem;">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            ${duration > 0 ? `
                <div class="toast-progress" style="
                    position: absolute;
                    bottom: 0;
                    left: 0;
                    height: 3px;
                    background-color: ${colors.icon};
                    border-radius: 0 0 0.75rem 0.75rem;
                    animation: toastProgress ${duration}ms linear;
                "></div>
            ` : ''}
        `;

        // Add close functionality
        const closeBtn = toast.querySelector('.toast-close-btn');
        if (closeBtn) {
            closeBtn.addEventListener('click', () => {
                this.removeToast(toast);
            });
        }

        return toast;
    }

    getToastColors(type) {
        // Get CSS variables from the document root
        const root = getComputedStyle(document.documentElement);

        switch (type) {
            case 'success':
                return {
                    bg: root.getPropertyValue('--color-success-light').trim() || '#d1fae5',
                    text: root.getPropertyValue('--color-success').trim() || '#065f46',
                    border: root.getPropertyValue('--color-success').trim() || '#10b981',
                    icon: root.getPropertyValue('--color-success').trim() || '#10b981'
                };
            case 'error':
                return {
                    bg: root.getPropertyValue('--color-danger-light').trim() || '#fee2e2',
                    text: root.getPropertyValue('--color-danger').trim() || '#991b1b',
                    border: root.getPropertyValue('--color-danger').trim() || '#ef4444',
                    icon: root.getPropertyValue('--color-danger').trim() || '#ef4444'
                };
            case 'warning':
                return {
                    bg: root.getPropertyValue('--color-warning-light').trim() || '#fef3c7',
                    text: root.getPropertyValue('--color-warning').trim() || '#92400e',
                    border: root.getPropertyValue('--color-warning').trim() || '#f59e0b',
                    icon: root.getPropertyValue('--color-warning').trim() || '#f59e0b'
                };
            case 'info':
            default:
                return {
                    bg: '#e0f2fe',
                    text: '#0369a1',
                    border: '#0ea5e9',
                    icon: '#0ea5e9'
                };
        }
    }

    getToastIcon(type) {
        switch (type) {
            case 'success':
                return 'fas fa-check-circle';
            case 'error':
                return 'fas fa-exclamation-circle';
            case 'warning':
                return 'fas fa-exclamation-triangle';
            case 'info':
            default:
                return 'fas fa-info-circle';
        }
    }

    removeToast(toast) {
        if (toast && toast.parentNode) {
            toast.style.transform = 'translateX(100%)';
            toast.style.opacity = '0';

            setTimeout(() => {
                if (toast.parentNode) {
                    toast.parentNode.removeChild(toast);
                }
            }, 300);
        }
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Dialog confirmation system
class DialogManager {
    constructor() {
        this.stylesCreated = false;

        // Only create styles if DOM is ready
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => {
                this.createDialogStyles();
            });
        } else {
            this.createDialogStyles();
        }
    }

    createDialogStyles() {
        if (this.stylesCreated || document.getElementById('dialog-styles')) {
            this.stylesCreated = true;
            return;
        }

        // Ensure document.head exists
        if (!document.head) {
            console.error('Document head not available yet');
            setTimeout(() => this.createDialogStyles(), 100);
            return;
        }

        const styles = document.createElement('style');
        styles.id = 'dialog-styles';
        styles.textContent = `
            .dialog-overlay {
                position: fixed;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background-color: rgba(0, 0, 0, 0.5);
                z-index: 9998;
                opacity: 0;
                transition: opacity 0.3s ease;
                backdrop-filter: blur(4px);
            }
            
            .dialog-overlay.show {
                opacity: 1;
            }
            
            .dialog-container {
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%) scale(0.95);
                background-color: var(--color-bg-primary);
                border: 1px solid var(--color-border);
                border-radius: 1rem;
                box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
                z-index: 9999;
                min-width: 400px;
                max-width: 500px;
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
                opacity: 0;
            }
            
            .dialog-container.show {
                transform: translate(-50%, -50%) scale(1);
                opacity: 1;
            }
            
            .dialog-header {
                padding: 1.5rem 1.5rem 0 1.5rem;
                border-bottom: 1px solid var(--color-border);
                margin-bottom: 1rem;
            }
            
            .dialog-title {
                font-size: 1.25rem;
                font-weight: 600;
                color: var(--color-primary);
                margin: 0 0 0.5rem 0;
            }
            
            .dialog-description {
                font-size: 0.875rem;
                color: var(--color-secondary);
                margin: 0;
                line-height: 1.5;
            }
            
            .dialog-content {
                padding: 0 1.5rem 1.5rem 1.5rem;
            }
            
            .dialog-actions {
                display: flex;
                justify-content: flex-end;
                gap: 0.75rem;
                padding-top: 1rem;
                border-top: 1px solid var(--color-border);
                margin-top: 1rem;
            }
            
            @keyframes toastProgress {
                from {
                    width: 100%;
                }
                to {
                    width: 0%;
                }
            }
        `;

        document.head.appendChild(styles);
        this.stylesCreated = true;
    }

    ensureStyles() {
        if (!this.stylesCreated || !document.getElementById('dialog-styles')) {
            this.createDialogStyles();
        }
    }

    show(title, description, options = {}) {
        return new Promise((resolve) => {
            this.ensureStyles();

            if (!document.body) {
                console.error('Document body not available for dialog');
                resolve(false);
                return;
            }

            const {
                confirmText = 'Confirm',
                cancelText = 'Cancel',
                confirmStyle = 'danger',
                showCancel = true,
                icon = null
            } = options;

            const overlay = document.createElement('div');
            overlay.className = 'dialog-overlay';

            const dialog = document.createElement('div');
            dialog.className = 'dialog-container';

            // Get CSS variables for icon colors
            const root = getComputedStyle(document.documentElement);
            const iconColors = this.getDialogIconColors(confirmStyle, root);

            const iconHtml = icon ? `
                <div class="flex items-center justify-center w-12 h-12 mx-auto mb-4 rounded-full" style="background-color: ${iconColors.bg};">
                    <i class="${icon}" style="color: ${iconColors.icon}; font-size: 1.5rem;"></i>
                </div>
            ` : '';

            dialog.innerHTML = `
                <div class="dialog-header">
                    ${iconHtml}
                    <h3 class="dialog-title">${this.escapeHtml(title)}</h3>
                    <p class="dialog-description">${this.escapeHtml(description)}</p>
                </div>
                <div class="dialog-content">
                    <div class="dialog-actions">
                        ${showCancel ? `<button class="btn btn-secondary dialog-cancel">${cancelText}</button>` : ''}
                        <button class="btn btn-${confirmStyle} dialog-confirm">${confirmText}</button>
                    </div>
                </div>
            `;

            overlay.appendChild(dialog);
            document.body.appendChild(overlay);

            // Show animation
            setTimeout(() => {
                overlay.classList.add('show');
                dialog.classList.add('show');
            }, 10);

            // Event handlers
            const cleanup = () => {
                overlay.classList.remove('show');
                dialog.classList.remove('show');

                setTimeout(() => {
                    if (overlay.parentNode) {
                        overlay.parentNode.removeChild(overlay);
                    }
                }, 300);
            };

            const confirmBtn = dialog.querySelector('.dialog-confirm');
            const cancelBtn = dialog.querySelector('.dialog-cancel');

            if (confirmBtn) {
                confirmBtn.addEventListener('click', () => {
                    cleanup();
                    resolve(true);
                });
            }

            if (cancelBtn) {
                cancelBtn.addEventListener('click', () => {
                    cleanup();
                    resolve(false);
                });
            }

            // Close on overlay click
            overlay.addEventListener('click', (e) => {
                if (e.target === overlay) {
                    cleanup();
                    resolve(false);
                }
            });

            // Close on escape key
            const escapeHandler = (e) => {
                if (e.key === 'Escape') {
                    cleanup();
                    resolve(false);
                    document.removeEventListener('keydown', escapeHandler);
                }
            };

            document.addEventListener('keydown', escapeHandler);
        });
    }

    getDialogIconColors(confirmStyle, root) {
        switch (confirmStyle) {
            case 'danger':
                return {
                    bg: root.getPropertyValue('--color-danger-light').trim() || '#fee2e2',
                    icon: root.getPropertyValue('--color-danger').trim() || '#ef4444'
                };
            case 'warning':
                return {
                    bg: root.getPropertyValue('--color-warning-light').trim() || '#fef3c7',
                    icon: root.getPropertyValue('--color-warning').trim() || '#f59e0b'
                };
            case 'success':
                return {
                    bg: root.getPropertyValue('--color-success-light').trim() || '#d1fae5',
                    icon: root.getPropertyValue('--color-success').trim() || '#10b981'
                };
            case 'primary':
            default:
                return {
                    bg: '#e0f2fe',
                    icon: '#3b82f6'
                };
        }
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Initialize when DOM is ready
let toastManager = null;
let dialogManager = null;

function initializeManagers() {
    try {
        if (!toastManager) {
            toastManager = new ToastManager();
        }
        if (!dialogManager) {
            dialogManager = new DialogManager();
        }
    } catch (error) {
        console.error('Error initializing managers:', error);
    }
}

// Initialize immediately if DOM is ready, otherwise wait
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeManagers);
} else {
    initializeManagers();
}

// Global helper functions with safety checks
window.showToast = function(type, message, duration = 5000) {
    try {
        if (!toastManager) {
            initializeManagers();
        }
        if (toastManager) {
            return toastManager.show(type, message, duration);
        }
        console.warn('Toast manager not available, using fallback');
        return null;
    } catch (error) {
        console.error('Error showing toast:', error);
        return null;
    }
};

window.showConfirmDialog = function(title, description, options = {}) {
    try {
        if (!dialogManager) {
            initializeManagers();
        }
        if (dialogManager) {
            return dialogManager.show(title, description, options);
        }
        console.warn('Dialog manager not available, using fallback');
        return Promise.resolve(false);
    } catch (error) {
        console.error('Error showing dialog:', error);
        return Promise.resolve(false);
    }
};

// Additional helper functions
window.showSuccessToast = function(message, duration = 5000) {
    return showToast('success', message, duration);
};

window.showErrorToast = function(message, duration = 5000) {
    return showToast('error', message, duration);
};

window.showWarningToast = function(message, duration = 5000) {
    return showToast('warning', message, duration);
};

window.showInfoToast = function(message, duration = 5000) {
    return showToast('info', message, duration);
};

window.showDeleteConfirm = function(itemName, description = null) {
    return showConfirmDialog(
        `Delete ${itemName}?`,
        description || `Are you sure you want to delete this ${itemName.toLowerCase()}? This action cannot be undone.`,
        {
            confirmText: 'Delete',
            cancelText: 'Cancel',
            confirmStyle: 'danger',
            icon: 'fas fa-trash-alt'
        }
    );
};

window.showLogoutConfirm = function() {
    return showConfirmDialog(
        'Sign Out',
        'Are you sure you want to sign out of your account?',
        {
            confirmText: 'Sign Out',
            cancelText: 'Stay Signed In',
            confirmStyle: 'warning',
            icon: 'fas fa-sign-out-alt'
        }
    );
};

// Initialize when DOM is loaded and show server messages
document.addEventListener('DOMContentLoaded', function() {
    console.log('Common utilities loaded successfully');

    // Ensure managers are initialized
    initializeManagers();

    // Show any server-side messages
    if (window.serverMessage) {
        setTimeout(() => {
            showToast(window.serverMessage.type, window.serverMessage.message);
            window.serverMessage = null;
        }, 100); // Small delay to ensure DOM is fully ready
    }
});

// Export for module systems (if needed)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        showToast,
        showConfirmDialog,
        showSuccessToast,
        showErrorToast,
        showWarningToast,
        showInfoToast,
        showDeleteConfirm,
        showLogoutConfirm
    };
}
