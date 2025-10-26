CREATE TABLE taskers (
    tasker_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    skillset VARCHAR(255),
    experience_years INT DEFAULT 0,
    rating DECIMAL(3,2) DEFAULT 0.0 CHECK (rating BETWEEN 0 AND 5),
    status ENUM('available', 'busy', 'inactive') DEFAULT 'available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO taskers (full_name, email, phone, skillset, experience_years, rating, status) VALUES
('Alice Carter', 'alice.tasker@example.com', '1231112222', 'Web Design, UI/UX', 5, 4.8, 'available'),
('Brian Howard', 'brian.tasker@example.com', '3214445555', 'SEO, Marketing', 4, 4.5, 'available'),
('Clara Kim', 'clara.tasker@example.com', '5559998888', 'App Development, API Integration', 6, 4.9, 'busy'),
('David Patel', 'david.tasker@example.com', '7778889999', 'Content Writing, Copy Editing', 3, 4.2, 'available'),
('Evelyn Brooks', 'evelyn.tasker@example.com', '4445556666', 'Logo Design, Branding', 2, 4.0, 'inactive'),
('Frank Lin', 'frank.tasker@example.com', '8887776666', 'Tech Support, System Maintenance', 7, 4.6, 'available'),
('Grace Moore', 'grace.tasker@example.com', '2223334444', 'Project Management, Communication', 8, 4.9, 'busy');
