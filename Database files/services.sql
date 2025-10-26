-- MySQL schema for services
CREATE DATABASE IF NOT EXISTS admin_dashboard_db;
USE admin_dashboard_db;

CREATE TABLE services (
            service_id INT AUTO_INCREMENT PRIMARY KEY,
            service_name VARCHAR(100) NOT NULL,
            description TEXT,
            price DECIMAL(10,2),
            is_active BOOLEAN DEFAULT TRUE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
