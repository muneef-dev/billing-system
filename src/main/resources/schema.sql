DROP DATABASE billing_system;
CREATE DATABASE IF NOT EXISTS billing_system;
USE billing_system;

CREATE TABLE users (
                       id VARCHAR(36) PRIMARY KEY,
                       username VARCHAR(50) UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role ENUM('admin', 'staff') DEFAULT 'staff',
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       last_login TIMESTAMP
);

CREATE TABLE customers (
                           id VARCHAR(36) PRIMARY KEY,
                           account_number VARCHAR(20) NOT NULL UNIQUE,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(100),
                           address TEXT,
                           telephone VARCHAR(20) NOT NULL UNIQUE,
                           notes TEXT,
                           is_active BOOLEAN DEFAULT TRUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE categories (
                            id VARCHAR(36) PRIMARY KEY,
                            category_name VARCHAR(100) UNIQUE NOT NULL,
                            description TEXT,
                            category_id VARCHAR(36) NOT NULL,
                            is_active BOOLEAN DEFAULT TRUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE items (
                       id VARCHAR(36) PRIMARY KEY,
                       item_code VARCHAR(20) NOT NULL UNIQUE,
                       item_name VARCHAR(100) NOT NULL UNIQUE,
                       category VARCHAR(50) NOT NULL,
                       author VARCHAR(100),
                       publisher VARCHAR(100),
                       description TEXT,
                       cover_image_url VARCHAR(255),
                       unit_price DECIMAL(10,2) NOT NULL,
                       cost_price DECIMAL(10,2),
                       stock_quantity INT NOT NULL DEFAULT 0,
                       minimum_stock_level INT DEFAULT 5,
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE orders (
                        id VARCHAR(36) PRIMARY KEY,
                        order_number VARCHAR(20) NOT NULL UNIQUE,
                        customer_id VARCHAR(36) NOT NULL,
                        subtotal DECIMAL(10,2) NOT NULL,
                        discount_amount DECIMAL(10,2) DEFAULT 0.00,
                        total_amount DECIMAL(10,2) NOT NULL,
                        status ENUM('Pending', 'Paid', 'Cancelled') DEFAULT 'Pending' NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (customer_id) REFERENCES customers(id)

);

CREATE TABLE order_items (
                             id VARCHAR(36) PRIMARY KEY,
                             order_id VARCHAR(36) NOT NULL,
                             item_id VARCHAR(36) NOT NULL,
                             quantity INT NOT NULL,
                             unit_price DECIMAL(10,2) NOT NULL,
                             total_price DECIMAL(10,2) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (order_id) REFERENCES orders(id),
                             FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE payments (
                          id VARCHAR(36) PRIMARY KEY,
                          order_id VARCHAR(36) NOT NULL,
                          amount DECIMAL(10,2) NOT NULL,
                          method ENUM('Cash', 'BankTransfer') NOT NULL,
                          reference_number VARCHAR(50),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE stock_movements (
                                 id VARCHAR(36) PRIMARY KEY,
                                 item_id VARCHAR(36) NOT NULL,
                                 change_quantity INT NOT NULL,
                                 reason ENUM('Sale','Return','Adjustment','Purchase') NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 FOREIGN KEY (item_id) REFERENCES items(id)
);