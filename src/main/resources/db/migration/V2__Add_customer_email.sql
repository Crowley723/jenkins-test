-- Add email column to customer table
ALTER TABLE customer ADD COLUMN email VARCHAR(255);

-- Add unique constraint for email
ALTER TABLE customer ADD CONSTRAINT uk_customer_email UNIQUE (email);

-- Create index for email lookups
CREATE INDEX idx_customer_email ON customer(email);