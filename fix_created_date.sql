-- Fix created_date column for existing users
-- Run this SQL script in your PostgreSQL database

-- Step 1: Add column as nullable first
ALTER TABLE users ADD COLUMN IF NOT EXISTS created_date TIMESTAMP;

-- Step 2: Set default value for existing records
UPDATE users SET created_date = NOW() WHERE created_date IS NULL;

-- Step 3: Make it NOT NULL
ALTER TABLE users ALTER COLUMN created_date SET NOT NULL;

-- Verify
SELECT id, email, created_date FROM users;
