DROP SCHEMA IF EXISTS ghdb;
CREATE SCHEMA IF NOT EXISTS ghdb;
USE ghdb;

DROP TABLE IF EXISTS shift_registrations;
DROP TABLE IF EXISTS required_certificates;
DROP TABLE IF EXISTS shifts;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS guard_certificates;
DROP TABLE IF EXISTS certificates;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS certificates (
    certificate_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    certificate_title TEXT
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name TEXT,
    email TEXT,
    password TEXT,
    phone_number TEXT,
    user_type VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS guard_certificates (
    guard_certificate_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    guard_id BIGINT,
    certificate_id BIGINT,
    FOREIGN KEY (guard_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (certificate_id) REFERENCES certificates(certificate_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS cities (
    city_name VARCHAR(128) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS clients (
    client_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name TEXT,
    email TEXT,
    city VARCHAR(128),
    address TEXT,
    FOREIGN KEY (city) REFERENCES cities(city_name)
);

CREATE TABLE IF NOT EXISTS shifts (
    shift_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT,
    title TEXT,
    shift_start DATETIME,
    shift_end DATETIME,
    description TEXT,
    required_guards INT,
    FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS required_certificates (
    required_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    shift_id BIGINT,
    certificate_id BIGINT,
    FOREIGN KEY (shift_id) REFERENCES shifts(shift_id),
    FOREIGN KEY (certificate_id) REFERENCES certificates(certificate_id)
);

CREATE TABLE IF NOT EXISTS shift_registrations (
    registration_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    guard_id BIGINT,
    shift_id BIGINT,
    registration_status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED'),
    FOREIGN KEY (guard_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (shift_id) REFERENCES shifts(shift_id) ON DELETE CASCADE
);