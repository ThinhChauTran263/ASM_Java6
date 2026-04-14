INSERT INTO categories (name, icon)
VALUES
    ('Phones', 'smartphone'),
    ('Laptops', 'laptop_mac'),
    ('Accessories', 'headphones'),
    ('Smartwatch', 'watch')
ON CONFLICT (name) DO NOTHING;

INSERT INTO products (name, description, price, original_price, image, available, badge, short_description, category_id)
VALUES
    ('Atelier Signature Wool Coat', 'Premium wool coat with structured silhouette.', 12500000, NULL, 'https://example.com/coat.jpg', TRUE, 'New', 'Merino wool, tailored fit', (SELECT id FROM categories WHERE name = 'Accessories' LIMIT 1)),
    ('iPhone 15 Pro Max', 'Natural Titanium, 256GB.', 34990000, NULL, 'https://example.com/iphone.jpg', TRUE, 'New', 'Titanium, 256GB', (SELECT id FROM categories WHERE name = 'Phones' LIMIT 1)),
    ('MacBook Pro M3', '14 inch, 16GB RAM, 512GB SSD.', 39990000, NULL, 'https://example.com/macbook.jpg', TRUE, NULL, '14 inch, 16GB, 512GB', (SELECT id FROM categories WHERE name = 'Laptops' LIMIT 1))
ON CONFLICT DO NOTHING;
