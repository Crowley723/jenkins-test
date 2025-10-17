-- Initial database schema
CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

-- Create indexes for common queries
CREATE INDEX idx_customer_first_name ON customer(first_name);
CREATE INDEX idx_customer_last_name ON customer(last_name);