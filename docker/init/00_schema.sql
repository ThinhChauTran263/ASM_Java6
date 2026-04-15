CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255),
    phone VARCHAR(30),
    address TEXT,
    provider VARCHAR(50),
    provider_id VARCHAR(100),
    image_url VARCHAR(500),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_date TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    icon VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price NUMERIC(15, 2) NOT NULL,
    original_price NUMERIC(15, 2),
    image TEXT,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    badge VARCHAR(50),
    short_description VARCHAR(255),
    category_id BIGINT REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS product_images (
    id BIGSERIAL PRIMARY KEY,
    image_url TEXT NOT NULL,
    alt_text VARCHAR(255),
    product_id BIGINT NOT NULL REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    email VARCHAR(120) NOT NULL,
    address TEXT NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    discount_code VARCHAR(50),
    total_amount NUMERIC(15, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_date TIMESTAMP,
    user_id BIGINT REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS order_details (
    id BIGSERIAL PRIMARY KEY,
    quantity INT NOT NULL,
    price NUMERIC(15, 2) NOT NULL,
    size VARCHAR(20),
    color VARCHAR(30),
    order_id BIGINT NOT NULL REFERENCES orders(id),
    product_id BIGINT NOT NULL REFERENCES products(id)
);
