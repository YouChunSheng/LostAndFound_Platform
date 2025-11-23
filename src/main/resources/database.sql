-- Create database
CREATE DATABASE IF NOT EXISTS lostandfound;
USE lostandfound;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role ENUM('user', 'admin') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create lost_items table
CREATE TABLE IF NOT EXISTS lost_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    lost_location VARCHAR(200),
    lost_time TIMESTAMP,
    image_url VARCHAR(500),
    contact_info VARCHAR(200),
    status ENUM('unclaimed', 'claimed') DEFAULT 'unclaimed',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create found_items table
CREATE TABLE IF NOT EXISTS found_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    found_location VARCHAR(200),
    found_time TIMESTAMP,
    image_url VARCHAR(500),
    contact_info VARCHAR(200),
    status ENUM('unclaimed', 'claimed') DEFAULT 'unclaimed',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create matches table
CREATE TABLE IF NOT EXISTS matches (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lost_item_id INT NOT NULL,
    found_item_id INT NOT NULL,
    user_id INT NOT NULL,
    status ENUM('pending', 'confirmed', 'rejected') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lost_item_id) REFERENCES lost_items(id) ON DELETE CASCADE,
    FOREIGN KEY (found_item_id) REFERENCES found_items(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO users (username, password, email, phone, role) VALUES 
('admin', '88888', 'admin@example.com', '1234567890', 'admin'),
('youchunsheng', '123456', 'youchunsheng@example.com', '1234567891', 'user'),
('user1', 'user123', 'user1@example.com', '1234567891', 'user'),
('user2', 'user456', 'user2@example.com', '1234567892', 'user');

INSERT INTO lost_items (user_id, title, description, category, lost_location, lost_time, contact_info) VALUES 
(2, '丢失的手机', '黑色iPhone手机', '电子设备', '图书馆', '2025-11-15 10:00:00', 'user1@example.com'),
(3, '校园卡', '学生校园卡', '证件', '食堂', '2025-11-14 12:00:00', 'user2@example.com');

INSERT INTO found_items (user_id, title, description, category, found_location, found_time, contact_info) VALUES 
(3, '捡到的手机', '黑色iPhone手机', '电子设备', '图书馆', '2025-11-15 11:00:00', 'user2@example.com'),
(2, '捡到的钥匙', '一串钥匙', '其他', '教学楼', '2025-11-15 14:00:00', 'user1@example.com');