-- ========================================================
-- SEED DATA: CATEGORIES, PRODUCTS, VARIANTS, PRODUCT IMAGES
-- ========================================================

BEGIN;

-- 1. Thêm các Categories mới (tránh trùng lặp slug)
INSERT INTO categories (name, slug, is_deleted, created_at, created_by)
VALUES 
  ('Điện Thoại & Phụ Kiện', 'dien-thoai-phu-kien', false, CURRENT_TIMESTAMP, 'system'),
  ('Máy Tính & Laptop', 'may-tinh-laptop', false, CURRENT_TIMESTAMP, 'system'),
  ('Gia Dụng & Đời Sống', 'gia-dung-doi-song', false, CURRENT_TIMESTAMP, 'system'),
  ('Mỹ Phẩm & Sắc Đẹp', 'my-pham-sac-dep', false, CURRENT_TIMESTAMP, 'system'),
  ('Giày Dép & Phụ Kiện', 'giay-dep-phu-kien', false, CURRENT_TIMESTAMP, 'system'),
  ('Đồng Hồ & Trang Sức', 'dong-ho-trang-suc', false, CURRENT_TIMESTAMP, 'system'),
  ('Sách & Văn Phòng Phẩm', 'sach-van-phong-pham', false, CURRENT_TIMESTAMP, 'system'),
  ('Mẹ & Bé', 'me-va-be', false, CURRENT_TIMESTAMP, 'system'),
  ('Bách Hóa Online', 'bach-hoa-online', false, CURRENT_TIMESTAMP, 'system')
ON CONFLICT (slug) DO UPDATE SET is_deleted = false;

-- Cập nhật min_price và thêm variants cho các sản phẩm cũ (1, 3, 4, 5, 6) nếu chưa có
UPDATE products SET min_price = 129000.00, average_rating = 4.8, review_count = 85, sold_count = 320 
WHERE id = 1 AND min_price = 0;

INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
VALUES 
  (1, 'ATN-COTTON-TRANG-M', '{"Màu sắc": "Trắng", "Kích cỡ": "M"}'::jsonb, 129000.00, 50, 200, 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system'),
  (1, 'ATN-COTTON-TRANG-L', '{"Màu sắc": "Trắng", "Kích cỡ": "L"}'::jsonb, 129000.00, 45, 200, 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system'),
  (1, 'ATN-COTTON-DEN-M', '{"Màu sắc": "Đen", "Kích cỡ": "M"}'::jsonb, 129000.00, 60, 200, 'https://images.unsplash.com/photo-1503342217505-b0a15ec3261c?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
ON CONFLICT (sku) DO NOTHING;

UPDATE products SET min_price = 320000.00, average_rating = 4.7, review_count = 42, sold_count = 110 
WHERE id = 3 AND min_price = 0;

INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
VALUES 
  (3, 'AK-BOMBER-DEN-L', '{"Màu sắc": "Đen", "Kích cỡ": "L"}'::jsonb, 320000.00, 30, 450, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system'),
  (3, 'AK-BOMBER-REU-XL', '{"Màu sắc": "Xanh Rêu", "Kích cỡ": "XL"}'::jsonb, 340000.00, 25, 450, 'https://images.unsplash.com/photo-1544441893-675973e31985?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
ON CONFLICT (sku) DO NOTHING;

UPDATE products SET min_price = 220000.00, average_rating = 4.9, review_count = 64, sold_count = 240 
WHERE id = 4 AND min_price = 0;

INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
VALUES 
  (4, 'SM-SATIN-BE-S', '{"Màu sắc": "Màu Be", "Kích cỡ": "S"}'::jsonb, 220000.00, 40, 250, 'https://images.unsplash.com/photo-1598554747436-c9293d6a588f?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system'),
  (4, 'SM-SATIN-TRANG-M', '{"Màu sắc": "Trắng Tinh Khôi", "Kích cỡ": "M"}'::jsonb, 235000.00, 35, 250, 'https://images.unsplash.com/photo-1589810635657-232948472d98?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
ON CONFLICT (sku) DO NOTHING;

UPDATE products SET min_price = 280000.00, average_rating = 4.6, review_count = 29, sold_count = 85 
WHERE id = 5 AND min_price = 0;

INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
VALUES 
  (5, 'VD-MAXI-HOA-M', '{"Họa tiết": "Hoa Nhí Vàng", "Kích cỡ": "Freesize"}'::jsonb, 280000.00, 20, 300, 'https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
ON CONFLICT (sku) DO NOTHING;

UPDATE products SET min_price = 195000.00, average_rating = 4.8, review_count = 115, sold_count = 450 
WHERE id = 6 AND min_price = 0;

INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
VALUES 
  (6, 'TX-CHEO-PU-DEN', '{"Màu sắc": "Đen Huyền Bí"}'::jsonb, 195000.00, 50, 350, 'https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system'),
  (6, 'TX-CHEO-PU-NAU', '{"Màu sắc": "Nâu Bò Vintage"}'::jsonb, 195000.00, 40, 350, 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
ON CONFLICT (sku) DO NOTHING;

UPDATE products SET min_price = 125000.00, average_rating = 4.7, review_count = 38, sold_count = 145 
WHERE id = 2 AND min_price = 0;

COMMIT;
