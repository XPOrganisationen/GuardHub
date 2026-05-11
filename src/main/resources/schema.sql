DROP DATABASE IF EXISTS ghdb;
CREATE DATABASE IF NOT EXISTS ghdb;
USE ghdb;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS cities;



CREATE TABLE IF NOT EXISTS clients (
                                      client_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      client_name TEXT,
                                      client_email TEXT,
                                      client_phone_number TEXT,
                                      client_city ENUM('Copenhagen','Roskilde', 'Odense', 'Aarhus', 'Aalborg', 'Esbjerg' ) NOT NULL,
                                      client_address TEXT
);

CREATE TABLE IF NOT EXISTS cities (
                                       city TEXT
);

