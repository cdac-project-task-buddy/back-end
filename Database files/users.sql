-- MySQL schema and sample data for users
CREATE DATABASE IF NOT EXISTS admin_dashboard_db;
USE admin_dashboard_db;

CREATE TABLE users (
            user_id INT AUTO_INCREMENT PRIMARY KEY,
            full_name VARCHAR(100) NOT NULL,
            email VARCHAR(100) UNIQUE NOT NULL,
            phone VARCHAR(20),
            password_hash VARCHAR(255) NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            status ENUM('active', 'inactive', 'banned') DEFAULT 'active'
        );

INSERT INTO users (full_name, email, phone, password_hash, status) VALUES
        ('John Doe', 'john@example.com', '1234567890', 'userpass1', 'active'),
        ('Jane Smith', 'jane@example.com', '9876543210', 'userpass2', 'active'),
        ('Robert Lee', 'robert@example.com', '5551234567', 'userpass3', 'inactive');
