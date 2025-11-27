DROP SEQUENCE IF EXISTS user_seq;
DROP SEQUENCE IF EXISTS item_seq;
DROP SEQUENCE IF EXISTS booking_seq;
DROP SEQUENCE IF EXISTS comment_seq;
DROP SEQUENCE IF EXISTS req_seq;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS users;


CREATE SEQUENCE IF NOT EXISTS user_seq START 1 INCREMENT 1;
CREATE TABLE IF NOT EXISTS users (
    id INT8 PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS req_seq START 1 INCREMENT 1;
CREATE TABLE IF NOT EXISTS requests (
    id INT8 PRIMARY KEY,
    description VARCHAR(65535) NOT NULL,
    user_id INT8 NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE SEQUENCE IF NOT EXISTS item_seq START 1 INCREMENT 1;
CREATE TABLE IF NOT EXISTS items (
    id INT8 PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    user_id INT8 NOT NULL,
    is_available BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    request_id INT8,

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_request_id FOREIGN KEY (request_id) REFERENCES requests(id)
);

CREATE SEQUENCE IF NOT EXISTS booking_seq START 1 INCREMENT 1;
CREATE TABLE IF NOT EXISTS bookings (
    id INT8 PRIMARY KEY,
    item_id INT8 NOT NULL,
    user_id INT8 NOT NULL,
    start_date_time TIMESTAMP NOT NULL,
    end_date_time TIMESTAMP NOT NULL,
    status VARCHAR(15) NOT NULL,

    CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES items(id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT chk_date_order CHECK (start_date_time < end_date_time),
    CONSTRAINT chk_status_value CHECK (status IN ('WAITING', 'APPROVED', 'REJECTED', 'CANCELLED'))
);

CREATE SEQUENCE IF NOT EXISTS comment_seq START 1 INCREMENT 1;
CREATE TABLE IF NOT EXISTS comments (
    id INT8 PRIMARY KEY,
    text VARCHAR(65535) NOT NULL,
    item_id INT8 NOT NULL,
    user_id INT8 NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES items(id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);