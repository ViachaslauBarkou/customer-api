CREATE TABLE IF NOT EXISTS customer (
    id CHAR(36) NOT NULL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    email_address VARCHAR(50) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL
);
