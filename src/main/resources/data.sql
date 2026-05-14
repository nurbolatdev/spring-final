-- seed categories
INSERT INTO categories (title, description)
SELECT 'Programming', 'Software development and coding courses'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE title = 'Programming');

INSERT INTO categories (title, description)
SELECT 'Design', 'UI/UX and graphic design courses'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE title = 'Design');

INSERT INTO categories (title, description)
SELECT 'Data Science', 'Machine learning and data analysis courses'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE title = 'Data Science');

-- seed admin user (password: admin123)
INSERT INTO users (username, email, password, role, created_at)
SELECT 'admin', 'admin@course.com',
       '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',
       'ROLE_ADMIN', NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@course.com');
