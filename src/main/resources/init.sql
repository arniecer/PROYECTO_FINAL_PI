CREATE DATABASE IF NOT EXISTS grandspicy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE grandspicy;

CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    price DOUBLE NOT NULL,
    image VARCHAR(255),
    image_data LONGBLOB,
    category VARCHAR(50) NOT NULL,
    scoville_level INT,
    country_of_origin VARCHAR(100),
    purchase_link VARCHAR(500),
    rating DOUBLE NOT NULL DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

INSERT IGNORE INTO users (username, email, password, role, name) VALUES
('admin', 'admin@gmail.com', '$2a$12$bOryToT6N9MoT6Xgb.bz..zZK3.CBGht/vvPx4rboTsbknbrJmeTG', 'ADMIN', 'Administrator'),
('user', 'user@grandspicy.com', '$2a$12$Xxs4FldRajN5.uTKVx35A.oSIwvvzPrcmhtXt7CpAhjjJJMaBoU2C', 'USER', 'Test User');

INSERT IGNORE INTO products (name, description, price, image, category, scoville_level, country_of_origin, purchase_link, rating) VALUES
('Habanero XXX Sauce', 'The spiciest sauce made with red habaneros and natural vinegar.', 12.99, 'habanero.jpg', 'Salsas', 10000, 'Mexico', 'https://example.com/habanero', 4.5),
('Dried Chiles Mix', 'A mix of dried chiles to add flavor to your dishes.', 8.50, 'chiles.jpg', 'Especias', 5000, 'Spain', 'https://example.com/chiles', 4.0),
('Wasabi Spicy Snack', 'Crunchy snack with a touch of wasabi and Asian spiciness.', 3.99, 'wasabi.jpg', 'Snacks', 3000, 'Japan', 'https://example.com/wasabi', 3.5),
('Sweet Chili Sauce', 'Sweet and sour sauce with a mild chili touch.', 6.50, 'sweetchili.jpg', 'Salsas', 500, 'Thailand', 'https://example.com/sweetchili', 4.8),
('Carolina Reaper Powder', 'Extreme spicy powder. Use with caution.', 15.99, 'reaper.jpg', 'Especias', 22000, 'United States', 'https://example.com/reaper', 5.0);

INSERT IGNORE INTO reviews (user_id, product_id, rating, comment) VALUES
(2, 1, 5, 'Amazing! Very spicy but with great flavor.'),
(2, 4, 4, 'Perfect for those who do not tolerate too much spice.'),
(2, 5, 5, 'This is another level. Just a tiny bit and you feel it.');
