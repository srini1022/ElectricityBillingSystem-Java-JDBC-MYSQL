
-- Normalized schema for Electricity Billing System
DROP DATABASE IF EXISTS nebs;
CREATE DATABASE nebs;
USE nebs;

-- Customers (one row per customer)
CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    meter_no VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    email VARCHAR(100),
    phone_no VARCHAR(20)
);

-- Meters (separate from customers to allow meter metadata and reassignment)
CREATE TABLE meters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    meter_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id INT,
    meter_location VARCHAR(100),
    meter_type VARCHAR(50),
    phase_code VARCHAR(50),
    bill_type VARCHAR(50),
    days INT,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Users (login accounts). `meter_id` links customers who are also users.
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    role VARCHAR(20) NOT NULL,
    meter_id INT,
    FOREIGN KEY (meter_id) REFERENCES meters(id) ON DELETE SET NULL
);

-- Taxes / Charges table (single source of truth for billing rates)
CREATE TABLE taxes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cost_per_unit DECIMAL(10,2) NOT NULL,
    meter_rent DECIMAL(10,2) NOT NULL,
    service_charge DECIMAL(10,2) NOT NULL,
    service_tax DECIMAL(10,2),
    swachh_bharat DECIMAL(10,2),
    fixed_tax DECIMAL(10,2),
    effective_from DATE DEFAULT (CURDATE())
);

-- Bills (one bill per meter per month/year)
CREATE TABLE bills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    meter_id INT NOT NULL,
    month VARCHAR(20) NOT NULL,
    year INT NOT NULL,
    units INT NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'Not Paid',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (meter_id) REFERENCES meters(id) ON DELETE CASCADE,
    UNIQUE KEY unique_bill_per_month (meter_id, month, year)
);

-- Payments for bills
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bill_id INT NOT NULL,
    paid_on DATETIME DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(12,2) NOT NULL,
    method VARCHAR(50),
    transaction_ref VARCHAR(100),
    FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE
);

-- Seed initial tax rates
INSERT INTO taxes (cost_per_unit, meter_rent, service_charge, service_tax, swachh_bharat, fixed_tax)
VALUES (10.00, 45.00, 20.00, 58.00, 5.00, 18.00);

-- Sample customer, meter and users
INSERT INTO customers (name, meter_no, address, city, state, email, phone_no)
VALUES ('John Doe', 'MTR001', '123 Main St', 'Cityville', 'Stateville', 'john@example.com', '1234567890');
SET @cust_id = LAST_INSERT_ID();

INSERT INTO meters (meter_number, customer_id, meter_location, meter_type, phase_code, bill_type, days)
VALUES ('MTR001', @cust_id, 'Rooftop', 'Electronic', 'Single', 'Monthly', 30);
SET @meter_id = LAST_INSERT_ID();

INSERT INTO users (username, password, name, role, meter_id)
VALUES ('admin', 'password123', 'Administrator', 'Admin', NULL),
             ('john_doe', 'password123', 'John Doe', 'Customer', @meter_id);

-- Example bill for the sample meter
INSERT INTO bills (meter_id, month, year, units, total_amount, status)
VALUES (@meter_id, 'March', YEAR(CURDATE()), 100, 100 * 10.00 + 45.00 + 20.00 + 5.00 + 18.00, 'Not Paid');

-- End of schema

