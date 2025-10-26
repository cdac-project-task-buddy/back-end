-- MySQL schema and sample data for reports
CREATE DATABASE IF NOT EXISTS admin_dashboard_db;
USE admin_dashboard_db;

CREATE TABLE reports (
            report_id INT AUTO_INCREMENT PRIMARY KEY,
            report_name VARCHAR(100) NOT NULL,
            generated_by INT NOT NULL,
            report_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            file_path VARCHAR(255),
            FOREIGN KEY (generated_by) REFERENCES admins(admin_id)
        );

INSERT INTO reports (report_name, generated_by, file_path) VALUES
        ('Monthly Performance - Sept', 1, '/reports/monthly_sept.pdf'),
        ('User Activity Summary', 2, '/reports/user_activity.pdf');
