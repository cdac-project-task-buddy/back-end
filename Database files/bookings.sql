-- MySQL schema and sample data for bookings
CREATE DATABASE IF NOT EXISTS admin_dashboard_db;
USE admin_dashboard_db;

CREATE TABLE bookings (
            booking_id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT NOT NULL,
            service_id INT NOT NULL,
            booking_date DATETIME NOT NULL,
            status ENUM('pending', 'confirmed', 'completed', 'cancelled') DEFAULT 'pending',
            remarks TEXT,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(user_id),
            FOREIGN KEY (service_id) REFERENCES services(service_id)
        );

INSERT INTO bookings (user_id, service_id, booking_date, status, remarks) VALUES
        (1, 1, NOW(), 'confirmed', 'Initial consultation booked'),
        (2, 2, NOW(), 'completed', 'Project finished successfully'),
        (3, 3, NOW(), 'pending', 'Awaiting confirmation');
