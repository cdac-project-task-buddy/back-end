-- MySQL schema and sample data for feedback
CREATE DATABASE IF NOT EXISTS admin_dashboard_db;
USE admin_dashboard_db;

CREATE TABLE feedback (
            feedback_id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT NOT NULL,
            subject VARCHAR(150),
            message TEXT,
            response TEXT,
            responded_by INT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(user_id),
            FOREIGN KEY (responded_by) REFERENCES admins(admin_id)
        );

INSERT INTO feedback (user_id, subject, message, response, responded_by) VALUES
        (1, 'Service Quality', 'Loved the web design service!', 'Thank you for your feedback!', 1),
        (2, 'Delay in Response', 'Took too long to get a reply.', 'We apologize and will improve.', 2);
