CREATE DATABASE IF NOT EXISTS billing_system;
USE billing_system;

CREATE TABLE users (
                       id VARCHAR(36) PRIMARY KEY,
                       username VARCHAR(50) UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       user_role VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       last_login TIMESTAMP
);

CREATE TABLE customers (
                           id VARCHAR(36) PRIMARY KEY,
                           account_number VARCHAR(20) NOT NULL UNIQUE,
                           name VARCHAR(100) NOT NULL,
                           address TEXT,
                           telephone VARCHAR(20),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE items (
                       id VARCHAR(36) PRIMARY KEY,
                       item_code VARCHAR(20) NOT NULL UNIQUE,
                       name VARCHAR(100) NOT NULL,
                       description TEXT,
                       price DECIMAL(10,2) NOT NULL,
                       stock_quantity INT NOT NULL DEFAULT 0,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE orders (
                        id VARCHAR(36) PRIMARY KEY,
                        order_number VARCHAR(20) NOT NULL UNIQUE,
                        customer_id VARCHAR(36) NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        total_amount DECIMAL(10,2) NOT NULL,
                        status VARCHAR(20) NOT NULL DEFAULT 'pending',
                        FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE order_items (
                             id VARCHAR(36) PRIMARY KEY,
                             order_id VARCHAR(36) NOT NULL,
                             item_id VARCHAR(36) NOT NULL,
                             quantity INT NOT NULL,
                             unit_price DECIMAL(10,2) NOT NULL,
                             subtotal DECIMAL(10,2) NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(id),
                             FOREIGN KEY (item_id) REFERENCES items(id)
);
