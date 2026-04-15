-- Fix created_date column for existing users
-- This script will be executed manually via docker exec

-- Step 1: Add column as nullable first (if not exists)
ALTER TABLE users ADD COLUMN IF NOT EXISTS created_date TIMESTAMP;

-- Step 2: Set default value for existing records
UPDATE users SET created_date = NOW() WHERE created_date IS NULL;

-- Step 3: Verify the fix
SELECT id, email, created_date FROM users ORDER BY id;
