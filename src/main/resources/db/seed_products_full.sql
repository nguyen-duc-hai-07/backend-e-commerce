-- AUTO-GENERATED PRODUCT SEED DATA
DO $$
DECLARE
  v_prod_id BIGINT;
BEGIN

  -- ========================================================
  -- Product: Áo Sơ Mi Nam Oxford Dài Tay Cao Cấp Chống Nhăn
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Áo Sơ Mi Nam Oxford Dài Tay Cao Cấp Chống Nhăn',
    'Chất liệu vải Oxford 100% cotton dệt chéo cao cấp, thoáng mát, thấm hút mồ hôi tốt. Thiết kế form regular fit trẻ trung, phù hợp cả đi làm công sở lẫn đi chơi dạo phố. Đường may tỉ mỉ, nút bấm chắc chắn.',
    1,
    'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop',
    4.8,
    142,
    289000,
    580,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SM-OXFORD-TRANG-M', '{"Màu sắc":"Trắng","Kích cỡ":"M"}'::jsonb, 289000, 45, 250, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SM-OXFORD-TRANG-L', '{"Màu sắc":"Trắng","Kích cỡ":"L"}'::jsonb, 289000, 40, 250, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SM-OXFORD-XANH-M', '{"Màu sắc":"Xanh Pastel","Kích cỡ":"M"}'::jsonb, 299000, 35, 250, 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SM-OXFORD-XANH-L', '{"Màu sắc":"Xanh Pastel","Kích cỡ":"L"}'::jsonb, 299000, 30, 250, 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&auto=format&fit=crop', 1, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1603252109303-2751441dd157?w=600&auto=format&fit=crop', 2, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Quần Jogger Nam Nỉ Bông Co Giãn Thể Thao
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Quần Jogger Nam Nỉ Bông Co Giãn Thể Thao',
    'Quần Jogger thể thao chất liệu nỉ da cá dệt dày dặn, giữ ấm tốt nhưng không bí bách. Bo gấu co giãn năng động, túi hai bên có khóa kéo tiện lợi khi vận động hoặc để điện thoại, ví tiền.',
    1,
    'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=600&auto=format&fit=crop',
    4.7,
    95,
    199000,
    340,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'JG-NI-XAM-M', '{"Màu sắc":"Xám Khói","Kích cỡ":"M"}'::jsonb, 199000, 60, 350, 'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'JG-NI-XAM-L', '{"Màu sắc":"Xám Khói","Kích cỡ":"L"}'::jsonb, 199000, 50, 350, 'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'JG-NI-DEN-L', '{"Màu sắc":"Đen Đậm","Kích cỡ":"L"}'::jsonb, 199000, 70, 350, 'https://images.unsplash.com/photo-1506630448388-4e683c67ddb0?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1506630448388-4e683c67ddb0?w=600&auto=format&fit=crop', 1, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Áo Len Cổ Lọ Nam Dệt Kim Phong Cách Hàn Quốc
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Áo Len Cổ Lọ Nam Dệt Kim Phong Cách Hàn Quốc',
    'Chất len lông thỏ mềm mượt, không xù lông, không gây ngứa dặm. Giữ ấm vượt trội trong mùa đông lạnh, dễ dàng phối cùng măng tô, áo khoác dạ hoặc blazer tạo vẻ ngoài lịch lãm, hiện đại.',
    1,
    'https://images.unsplash.com/photo-1614975058789-41316d0e2e9c?w=600&auto=format&fit=crop',
    4.9,
    68,
    275000,
    210,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'AL-COLO-KEM-L', '{"Màu sắc":"Kem Be","Kích cỡ":"L"}'::jsonb, 275000, 35, 400, 'https://images.unsplash.com/photo-1614975058789-41316d0e2e9c?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'AL-COLO-DEN-L', '{"Màu sắc":"Đen","Kích cỡ":"L"}'::jsonb, 275000, 40, 400, 'https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1614975058789-41316d0e2e9c?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=600&auto=format&fit=crop', 1, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Áo Blazer Nữ 2 Lớp Form Rộng Chuẩn Hàn Quốc
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Áo Blazer Nữ 2 Lớp Form Rộng Chuẩn Hàn Quốc',
    'Áo vest blazer nữ thiết kế thanh lịch 2 lớp lót lụa mềm mại, đứng form tôn dáng. Thích hợp mặc đi làm, hội họp hoặc phối cùng quần jeans đi cà phê cuối tuần cực kỳ thời thượng.',
    2,
    'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600&auto=format&fit=crop',
    4.9,
    215,
    389000,
    890,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'BLAZER-BE-S', '{"Màu sắc":"Be Nude","Kích cỡ":"S"}'::jsonb, 389000, 30, 500, 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'BLAZER-BE-M', '{"Màu sắc":"Be Nude","Kích cỡ":"M"}'::jsonb, 389000, 25, 500, 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'BLAZER-DEN-M', '{"Màu sắc":"Đen Sang Trọng","Kích cỡ":"M"}'::jsonb, 399000, 40, 500, 'https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=600&auto=format&fit=crop', 1, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Chân Váy Chữ A Lưng Cao Xếp Ly Tôn Dáng
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Chân Váy Chữ A Lưng Cao Xếp Ly Tôn Dáng',
    'Chân váy chữ A chất vải tuyết mưa cao cấp không bai xù, có quần trong bảo hộ kín đáo giúp chị em tự tin di chuyển. Dễ dàng phối cùng áo phông, áo croptop hay sơ mi.',
    2,
    'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=600&auto=format&fit=crop',
    4.8,
    84,
    175000,
    430,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'CV-CHUA-DEN-S', '{"Màu sắc":"Đen","Kích cỡ":"S"}'::jsonb, 175000, 50, 200, 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'CV-CHUA-DEN-M', '{"Màu sắc":"Đen","Kích cỡ":"M"}'::jsonb, 175000, 45, 200, 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'CV-CHUA-NAU-M', '{"Màu sắc":"Nâu Tây","Kích cỡ":"M"}'::jsonb, 175000, 35, 200, 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=600&auto=format&fit=crop', 1, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Tai Nghe Bluetooth Chống Ồn Chủ Động Sony WH-1000XM5
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Tai Nghe Bluetooth Chống Ồn Chủ Động Sony WH-1000XM5',
    'Tai nghe chụp tai hàng đầu với công nghệ chống ồn đỉnh cao Auto NC Optimizer. Thời lượng pin lên tới 30 giờ, hỗ trợ sạc nhanh 3 phút nghe được 3 giờ. Âm thanh Hi-Res Audio chuẩn phòng thu.',
    3,
    'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop',
    5,
    310,
    7990000,
    620,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SONY-XM5-BLACK', '{"Màu sắc":"Đen Huyền Bí"}'::jsonb, 7990000, 20, 250, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SONY-XM5-SILVER', '{"Màu sắc":"Bạc Ánh Kim"}'::jsonb, 7990000, 15, 250, 'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=600&auto=format&fit=crop', 1, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Bàn Phím Cơ Không Dây RGB Hot-Swap Gasket Mount
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Bàn Phím Cơ Không Dây RGB Hot-Swap Gasket Mount',
    'Bàn phím cơ layout 75% gọn gàng, kết nối 3 chế độ (Bluetooth 5.1 / Wireless 2.4Ghz / Type-C có dây). Switch Linear êm ái đã được lube sẵn tại nhà máy, pin 4000mAh dùng nhiều tuần liên tục.',
    3,
    'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop',
    4.9,
    180,
    1250000,
    520,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'KB-GASKET-RED-SW', '{"Switch":"Red Switch (Êm)","Màu case":"Trắng Xanh"}'::jsonb, 1250000, 40, 950, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'KB-GASKET-BROWN-SW', '{"Switch":"Brown Switch (Khấc nhẹ)","Màu case":"Đen Nhám"}'::jsonb, 1250000, 35, 950, 'https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?w=600&auto=format&fit=crop', 1, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Loa Bluetooth Di Động Marshall Emberton II Âm Thanh 360
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Loa Bluetooth Di Động Marshall Emberton II Âm Thanh 360',
    'Loa không dây siêu nhỏ gọn nhưng công suất mạnh mẽ, âm thanh True Stereophonic đặc trưng của Marshall. Kháng bụi kháng nước IP67, pin trâu hơn 30 giờ chơi nhạc chỉ sau một lần sạc đầy.',
    3,
    'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop',
    4.9,
    165,
    3690000,
    380,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'MS-EMBERTON-BLACK-BRASS', '{"Màu sắc":"Đen Viền Vàng Kim"}'::jsonb, 3690000, 25, 700, 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'MS-EMBERTON-CREAM', '{"Màu sắc":"Kem Cổ Điển"}'::jsonb, 3690000, 20, 700, 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Vợt Cầu Lông Yonex Astrox 88D Pro Full Carbon Chính Hãng
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Vợt Cầu Lông Yonex Astrox 88D Pro Full Carbon Chính Hãng',
    'Dòng vợt tấn công uy lực dành cho người chơi cầu lông từ phong trào tới chuyên nghiệp. Khung vợt ứng dụng vật liệu Namd tân tiến tăng khả năng đàn hồi và lực đập cầu cực cắm.',
    4,
    'https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?w=600&auto=format&fit=crop',
    4.9,
    78,
    3850000,
    190,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'YNX-88D-3U', '{"Trọng lượng":"3U (88g)","Độ căng":"11.5kg"}'::jsonb, 3850000, 15, 88, 'https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'YNX-88D-4U', '{"Trọng lượng":"4U (83g)","Độ căng":"11kg"}'::jsonb, 3850000, 20, 83, 'https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Thảm Tập Yoga Chống Trượt TPE 8mm Kèm Đường Kẻ Định Tuyến
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Thảm Tập Yoga Chống Trượt TPE 8mm Kèm Đường Kẻ Định Tuyến',
    'Thảm Yoga chất liệu TPE thân thiện môi trường, đàn hồi vượt trội, chống trơn trượt tối đa ngay cả khi đổ nhiều mồ hôi. Đường vạch định tuyến chuẩn xác hỗ trợ người tập chuẩn phom thế yoga.',
    4,
    'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600&auto=format&fit=crop',
    4.8,
    130,
    219000,
    670,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'THAM-YOGA-TIM', '{"Màu sắc":"Tím Violet","Độ dày":"8mm"}'::jsonb, 219000, 40, 900, 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'THAM-YOGA-XANH', '{"Màu sắc":"Xanh Cốm","Độ dày":"8mm"}'::jsonb, 219000, 50, 900, 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Điện Thoại Apple iPhone 15 Pro Max 256GB Khung Titan
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Điện Thoại Apple iPhone 15 Pro Max 256GB Khung Titan',
    'Trang bị chip A17 Pro mạnh mẽ vượt bậc chiến mọi tựa game đồ họa nặng. Khung viền Titan chuẩn hàng không vũ trụ siêu bền nhẹ, màn hình Super Retina XDR ProMotion 120Hz mượt mà và camera tiềm vọng 5x.',
    5,
    'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop',
    5,
    420,
    28990000,
    1250,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'IP15PM-256GB-NATURAL', '{"Màu sắc":"Titan Tự Nhiên","Dung lượng":"256GB"}'::jsonb, 28990000, 30, 221, 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'IP15PM-256GB-BLACK', '{"Màu sắc":"Titan Đen","Dung lượng":"256GB"}'::jsonb, 28990000, 25, 221, 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'IP15PM-512GB-BLUE', '{"Màu sắc":"Titan Xanh","Dung lượng":"512GB"}'::jsonb, 33990000, 15, 221, 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=600&auto=format&fit=crop', 1, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Điện Thoại Samsung Galaxy S24 Ultra 5G AI Thông Minh
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Điện Thoại Samsung Galaxy S24 Ultra 5G AI Thông Minh',
    'Quyền năng Galaxy AI đỉnh cao hỗ trợ dịch cuộc gọi thời gian thực, khoanh vùng tìm kiếm đa năng. Bút S-Pen tích hợp tiện lợi, camera mắt thần bóng đêm 200MP và chip Snapdragon 8 Gen 3 for Galaxy.',
    5,
    'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop',
    4.9,
    350,
    26990000,
    980,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SS-S24U-256GB-XAM', '{"Màu sắc":"Xám Titan","Dung lượng":"256GB"}'::jsonb, 26990000, 25, 232, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SS-S24U-512GB-DEN', '{"Màu sắc":"Đen Titan","Dung lượng":"512GB"}'::jsonb, 30990000, 20, 232, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Củ Sạc Nhanh Anker GaNPrime 65W 3 Cổng Tiện Lợi
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Củ Sạc Nhanh Anker GaNPrime 65W 3 Cổng Tiện Lợi',
    'Công nghệ bán dẫn Gallium Nitride (GaN) giúp củ sạc nhỏ gọn hơn 53% so với củ sạc thông thường. 2 cổng Type-C và 1 cổng USB-A sạc đồng thời cho cả MacBook, iPad và điện thoại an toàn.',
    5,
    'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop',
    4.9,
    280,
    790000,
    1400,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'ANKER-GAN-65W-DEN', '{"Màu sắc":"Đen Nhám"}'::jsonb, 790000, 80, 130, 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'ANKER-GAN-65W-TRANG', '{"Màu sắc":"Trắng Tinh Khôi"}'::jsonb, 790000, 60, 130, 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Pin Sạc Dự Phòng Không Dây Từ Tính MagSafe 10.000mAh
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Pin Sạc Dự Phòng Không Dây Từ Tính MagSafe 10.000mAh',
    'Hít nam châm cực mạnh tương thích chuẩn MagSafe, sạc không dây 15W tiện dụng và sạc nhanh có dây 20W chuẩn PD. Màn hình LED thông minh hiển thị chính xác % pin còn lại.',
    5,
    'https://images.unsplash.com/photo-1609592424109-dd9892f1b177?w=600&auto=format&fit=crop',
    4.8,
    190,
    490000,
    890,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'PB-MAGSAFE-10K-XAM', '{"Màu sắc":"Xám Không Gian","Dung lượng":"10000mAh"}'::jsonb, 490000, 50, 210, 'https://images.unsplash.com/photo-1609592424109-dd9892f1b177?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'PB-MAGSAFE-10K-TRANG', '{"Màu sắc":"Trắng Ngọc Trai","Dung lượng":"10000mAh"}'::jsonb, 490000, 45, 210, 'https://images.unsplash.com/photo-1609592424109-dd9892f1b177?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1609592424109-dd9892f1b177?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Apple MacBook Air 13.6 inch M3 (16GB RAM / 256GB SSD)
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Apple MacBook Air 13.6 inch M3 (16GB RAM / 256GB SSD)',
    'Thiết kế nhôm nguyên khối siêu mỏng nhẹ chỉ 1.24kg. Chip Apple M3 thế hệ mới xử lý mượt mà tác vụ đồ họa và lập trình, pin dùng liên tục lên đến 18 tiếng cả ngày dài.',
    6,
    'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop',
    5,
    180,
    27990000,
    490,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'MBA-M3-16-256-MIDNIGHT', '{"Màu sắc":"Midnight (Xanh Đen)","Bộ nhớ":"16GB RAM - 256GB SSD"}'::jsonb, 27990000, 15, 1240, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'MBA-M3-16-256-STARLIGHT', '{"Màu sắc":"Starlight (Ánh Sao)","Bộ nhớ":"16GB RAM - 256GB SSD"}'::jsonb, 27990000, 20, 1240, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'MBA-M3-16-512-SPACEGRAY', '{"Màu sắc":"Xám Không Gian","Bộ nhớ":"16GB RAM - 512GB SSD"}'::jsonb, 32990000, 12, 1240, 'https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=600&auto=format&fit=crop', 1, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Laptop Gaming ASUS ROG Zephyrus G16 OLED Core Ultra 9
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Laptop Gaming ASUS ROG Zephyrus G16 OLED Core Ultra 9',
    'Cỗ máy gaming đỉnh cao mỏng nhẹ ấn tượng. Màn hình ROG Nebula Display OLED 2.5K 240Hz sắc nét rực rỡ, card đồ họa NVIDIA GeForce RTX 4070 cân mọi game AAA và render video 4K siêu tốc.',
    6,
    'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600&auto=format&fit=crop',
    4.9,
    65,
    54990000,
    140,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'ROG-G16-RTX4070-32GB', '{"Cấu hình":"Ultra 9 / 32GB RAM / 1TB SSD / RTX 4070","Màu sắc":"Eclipse Gray"}'::jsonb, 54990000, 10, 1850, 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Màn Hình Đồ Họa Chuyên Nghiệp Dell UltraSharp 27 Inch 4K IPS
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Màn Hình Đồ Họa Chuyên Nghiệp Dell UltraSharp 27 Inch 4K IPS',
    'Độ phủ màu 100% sRGB và 98% DCI-P3 cực chuẩn xác cho dân thiết kế, chỉnh sửa ảnh phim. Cổng kết nối USB-C hỗ trợ truyền hình ảnh và sạc ngược công suất 90W cho laptop gọn gàng.',
    6,
    'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop',
    4.9,
    110,
    12890000,
    280,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'DELL-U2723QE-4K', '{"Kích thước":"27 inch 4K IPS","Cổng kết nối":"Type-C 90W Hub"}'::jsonb, 12890000, 20, 6500, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Nồi Chiên Không Dầu Điện Tử Philips 6.2 Lít XXL Rapid Air
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Nồi Chiên Không Dầu Điện Tử Philips 6.2 Lít XXL Rapid Air',
    'Dung tích khủng 6.2L chiên nguyên con gà 1.5kg dễ dàng. Công nghệ Rapid Air độc quyền giảm tới 90% lượng dầu mỡ thừa, bảng điều khiển cảm ứng với 7 chương trình nấu cài đặt sẵn.',
    7,
    'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600&auto=format&fit=crop',
    4.8,
    230,
    2690000,
    760,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'PHILIPS-NCKD-6L2-DEN', '{"Màu sắc":"Đen Bóng","Dung tích":"6.2L"}'::jsonb, 2690000, 35, 5500, 'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Robot Hút Bụi Lau Nhà Tự Giặt Giẻ Dreame L20 Ultra
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Robot Hút Bụi Lau Nhà Tự Giặt Giẻ Dreame L20 Ultra',
    'Công nghệ cánh tay robot MopExtend tiếp cận sát chân tường và ngóc ngách. Lực hút khủng 7000Pa hút sạch mọi bụi mịn, trạm sạc đa năng tự giặt giẻ nước nóng và sấy khô kháng khuẩn.',
    7,
    'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=600&auto=format&fit=crop',
    5,
    95,
    18990000,
    180,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'DREAME-L20U-TRANG', '{"Màu sắc":"Trắng Tinh Tế"}'::jsonb, 18990000, 15, 12000, 'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Máy Pha Cà Phê Tự Động DeLonghi Magnifica S Nhập Khẩu Ý
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Máy Pha Cà Phê Tự Động DeLonghi Magnifica S Nhập Khẩu Ý',
    'Thưởng thức Espresso và Cappuccino chuẩn vị Ý ngay tại nhà chỉ với một nút bấm. Tích hợp cối xay hạt thép không gỉ với 13 cấp độ xay mịn, vòi tạo bọt sữa bằng tay bông dày sánh mịn.',
    7,
    'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop',
    4.9,
    140,
    14500000,
    230,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'DELONGHI-ECAM-DEN', '{"Màu sắc":"Đen Bạc Cổ Điển"}'::jsonb, 14500000, 12, 9000, 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Serum Phục Hồi Và Làm Sáng Da La Roche-Posay Hyalu B5 30ml
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Serum Phục Hồi Và Làm Sáng Da La Roche-Posay Hyalu B5 30ml',
    'Công thức dưỡng chất độc đáo gồm 2 loại Hyaluronic Acid nguyên chất, Vitamin B5 và Madecassoside giúp tái tạo phục hồi hàng rào bảo vệ da, cấp ẩm sâu cho da căng mọng đàn hồi.',
    8,
    'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop',
    4.9,
    490,
    890000,
    2100,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'LRP-HYALU-B5-30ML', '{"Dung tích":"30ml"}'::jsonb, 890000, 100, 150, 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'LRP-HYALU-B5-50ML', '{"Dung tích":"50ml (Tiết kiệm)"}'::jsonb, 1290000, 60, 200, 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Kem Chống Nắng Kiềm Dầu Anessa Perfect UV Milk SPF50+ PA++++
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Kem Chống Nắng Kiềm Dầu Anessa Perfect UV Milk SPF50+ PA++++',
    'Sữa chống nắng quốc dân đến từ Nhật Bản với công nghệ Auto Booster chống trôi nước, mồ hôi và ma sát cực tốt. Kết cấu sữa mỏng nhẹ, nâng tông tự nhiên và kiềm dầu suốt cả ngày dài.',
    8,
    'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=600&auto=format&fit=crop',
    4.9,
    620,
    545000,
    3400,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'ANESSA-GOLD-60ML', '{"Dung tích":"60ml"}'::jsonb, 545000, 150, 90, 'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Son Thỏi Lì Cao Cấp YSL Rouge Pur Couture The Slim Velvet
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Son Thỏi Lì Cao Cấp YSL Rouge Pur Couture The Slim Velvet',
    'Thiết kế thỏi vuông vắn sang trọng đặc trưng của Yves Saint Laurent. Chất son mịn như nhung matte nhưng không hề làm khô môi, màu sắc lên chuẩn xác tôn da phụ nữ Á Đông.',
    8,
    'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&auto=format&fit=crop',
    4.9,
    310,
    980000,
    1100,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'YSL-SLIM-301', '{"Mã màu":"Màu 301 Nude Pulsion (Cam Đất Trầm)"}'::jsonb, 980000, 40, 40, 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'YSL-SLIM-302', '{"Mã màu":"Màu 302 Brown No Way Back (Đỏ Đất Quyến Rũ)"}'::jsonb, 980000, 50, 40, 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Giày Thể Thao Sneaker Nike Air Force 1 '07 All White
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Giày Thể Thao Sneaker Nike Air Force 1 ''07 All White',
    'Biểu tượng giày sneaker huyền thoại không bao giờ lỗi mốt. Da thật cao cấp êm ái, đế đệm Air êm nhẹ mang lại sự thoải mái trong từng bước chân cả ngày.',
    9,
    'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop',
    5,
    410,
    2690000,
    1650,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'NIKE-AF1-WHT-40', '{"Màu sắc":"Trắng Toàn Bộ","Kích cỡ":"40 EU"}'::jsonb, 2690000, 30, 800, 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'NIKE-AF1-WHT-41', '{"Màu sắc":"Trắng Toàn Bộ","Kích cỡ":"41 EU"}'::jsonb, 2690000, 35, 800, 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'NIKE-AF1-WHT-42', '{"Màu sắc":"Trắng Toàn Bộ","Kích cỡ":"42 EU"}'::jsonb, 2690000, 25, 800, 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Giày Chạy Bộ Nam Nữ Siêu Nhẹ Siêu Êm Đàn Hồi
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Giày Chạy Bộ Nam Nữ Siêu Nhẹ Siêu Êm Đàn Hồi',
    'Thân giày vải dệt Flyknit thoáng khí tản nhiệt tốt, đệm bọt Boost hoàn trả năng lượng tối ưu giúp bước chạy nhẹ nhàng êm ái, bảo vệ khớp gối khi vận động cường độ cao.',
    9,
    'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop',
    4.8,
    180,
    850000,
    720,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'RUN-SHOES-DO-41', '{"Màu sắc":"Đỏ Nổi Bật","Kích cỡ":"41"}'::jsonb, 850000, 30, 600, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'RUN-SHOES-DO-42', '{"Màu sắc":"Đỏ Nổi Bật","Kích cỡ":"42"}'::jsonb, 850000, 20, 600, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Đồng Hồ Nam Cơ Tự Động Seiko 5 Sports Automatic Kính Hardlex
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Đồng Hồ Nam Cơ Tự Động Seiko 5 Sports Automatic Kính Hardlex',
    'Bộ máy tự động 4R36 bền bỉ của Seiko với 24 chân kính, khả năng trữ cót lên đến 41 giờ. Chống nước 100 mét (10 ATM), mặt kính Hardlex chống trầy xước và kim dạ quang LumiBrite sáng rực trong đêm.',
    10,
    'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop',
    4.9,
    135,
    5490000,
    360,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SEIKO5-DEN-THEP', '{"Mặt số":"Mặt Đen","Dây":"Thép Không Gỉ 316L"}'::jsonb, 5490000, 20, 145, 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SEIKO5-XANH-THEP', '{"Mặt số":"Mặt Xanh Biển","Dây":"Thép Không Gỉ 316L"}'::jsonb, 5490000, 15, 145, 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Đồng Hồ Thông Minh Apple Watch Series 9 GPS Viền Nhôm
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Đồng Hồ Thông Minh Apple Watch Series 9 GPS Viền Nhôm',
    'Chip S9 SiP thế hệ mới đem lại thao tác cử chỉ chạm hai lần (Double Tap) kỳ diệu mà không cần chạm tay vào màn hình. Đo nồng độ oxy trong máu, điện tâm đồ ECG và theo dõi giấc ngủ chi tiết.',
    10,
    'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop',
    4.9,
    260,
    9290000,
    790,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'AW-S9-41-MIDNIGHT', '{"Kích thước":"41mm","Màu sắc":"Midnight (Nhôm Tối)"}'::jsonb, 9290000, 25, 32, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'AW-S9-45-STARLIGHT', '{"Kích thước":"45mm","Màu sắc":"Starlight (Nhôm Ánh Sao)"}'::jsonb, 9990000, 20, 39, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Sách Atomic Habits - Thay Đổi Tí Hon Hiệu Quả Bất Ngờ (Tái Bản)
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Sách Atomic Habits - Thay Đổi Tí Hon Hiệu Quả Bất Ngờ (Tái Bản)',
    'Cuốn sách bán chạy hàng đầu thế giới của James Clear chia sẻ hệ thống từng bước đã được khoa học chứng minh giúp bạn xây dựng thói quen tốt và từ bỏ thói quen xấu một cách dễ dàng, bền bỉ.',
    11,
    'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop',
    5,
    580,
    159000,
    3800,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'BOOK-ATOMIC-HABITS-BIA-MEM', '{"Phiên bản":"Bìa Mềm Tay Gấp"}'::jsonb, 159000, 150, 450, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'BOOK-ATOMIC-HABITS-BIA-CUNG', '{"Phiên bản":"Bìa Cứng Đặc Biệt (Kèm Bookmark)"}'::jsonb, 239000, 80, 650, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Bút Ký Kim Loại Khắc Tên Cao Cấp Kèm Hộp Đựng Sang Trọng
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Bút Ký Kim Loại Khắc Tên Cao Cấp Kèm Hộp Đựng Sang Trọng',
    'Vỏ hợp kim nguyên khối sơn tĩnh điện phủ bóng chống trầy, ngòi bút mực gel bi lăn nhập khẩu Đức viết trơn êm không tắc mực. Món quà tặng ý nghĩa cho đối tác, sếp, bạn bè.',
    11,
    'https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=600&auto=format&fit=crop',
    4.8,
    95,
    185000,
    410,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'BUT-KY-DEN-VANG', '{"Màu thân":"Đen Cài Vàng Kim","Ngòi":"0.5mm Mực Đen"}'::jsonb, 185000, 60, 120, 'https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Xe Đẩy Em Bé Gấp Gọn Siêu Nhẹ Đi Máy Bay Du Lịch
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Xe Đẩy Em Bé Gấp Gọn Siêu Nhẹ Đi Máy Bay Du Lịch',
    'Trọng lượng chỉ 4.8kg, dễ dàng gấp gọn bằng một tay xách lên khoang hành lý máy bay. Lưng ghế ngả đa nấc 100° - 175° cho bé nằm hoặc ngồi thoải mái, mái che chống tia UV 50+.',
    12,
    'https://images.unsplash.com/photo-1519689680058-324335c77eba?w=600&auto=format&fit=crop',
    4.9,
    110,
    1450000,
    320,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'XE-DAY-XAM', '{"Màu sắc":"Xám Ghi Cao Cấp"}'::jsonb, 1450000, 20, 4800, 'https://images.unsplash.com/photo-1519689680058-324335c77eba?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'XE-DAY-HONG', '{"Màu sắc":"Hồng Pastel"}'::jsonb, 1450000, 15, 4800, 'https://images.unsplash.com/photo-1519689680058-324335c77eba?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1519689680058-324335c77eba?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Máy Tiệt Trùng Sấy Khô Khử Mùi Bình Sữa Bằng Tia UVC Đa Năng
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Máy Tiệt Trùng Sấy Khô Khử Mùi Bình Sữa Bằng Tia UVC Đa Năng',
    'Công nghệ khử khuẩn tia UVC 360 độ tiêu diệt 99.9% vi khuẩn có hại chỉ trong vài phút. Dung tích khoang chứa rộng rãi để vừa 12 bình sữa cùng núm ti và phụ kiện ăn dặm.',
    12,
    'https://images.unsplash.com/photo-1515488042361-ee00e0ddd4e4?w=600&auto=format&fit=crop',
    4.9,
    145,
    1890000,
    450,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'MAY-TIET-TRUNG-UVC-TRANG', '{"Màu sắc":"Trắng Sữa","Dung tích":"18L"}'::jsonb, 1890000, 25, 3500, 'https://images.unsplash.com/photo-1515488042361-ee00e0ddd4e4?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1515488042361-ee00e0ddd4e4?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Cà Phê Hạt Rang Mộc Arabica Cầu Đất Thượng Hạng 500g
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Cà Phê Hạt Rang Mộc Arabica Cầu Đất Thượng Hạng 500g',
    '100% hạt cà phê Arabica tuyển chọn từ vùng cao nguyên Cầu Đất - Đà Lạt ở độ cao 1.600m. Hương thơm hoa quả dịu nhẹ quyến rũ, vị chua thanh tự nhiên cùng hậu vị ngọt sâu lắng.',
    13,
    'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=600&auto=format&fit=crop',
    5,
    320,
    185000,
    1850,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'CAFE-ARABICA-HAT-500G', '{"Quy cách":"Hạt Nguyên Chất Rang Mộc","Khối lượng":"500g"}'::jsonb, 185000, 80, 520, 'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'CAFE-ARABICA-XAY-500G', '{"Quy cách":"Bột Xay Pha Phin / Pha Máy","Khối lượng":"500g"}'::jsonb, 185000, 100, 520, 'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

  -- ========================================================
  -- Product: Set 5 Hộp Hạt Dinh Dưỡng Cao Cấp Nhập Khẩu (Hạt Óc Chó, Macca, Hạnh Nhân)
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    'Set 5 Hộp Hạt Dinh Dưỡng Cao Cấp Nhập Khẩu (Hạt Óc Chó, Macca, Hạnh Nhân)',
    'Hạt dinh dưỡng sấy giòn tự nhiên không tẩm ướp đường hóa học hay muối, giữ trọn vẹn chất chống oxy hóa, Omega 3-6 tốt cho tim mạch và não bộ. Món quà sức khỏe tuyệt hảo cho gia đình.',
    13,
    'https://images.unsplash.com/photo-1536591375315-1b8368903277?w=600&auto=format&fit=crop',
    4.9,
    210,
    420000,
    940,
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'SET-HAT-5-HOP-500G', '{"Quy cách":"Set 5 Hũ x 500g"}'::jsonb, 420000, 45, 2600, 'https://images.unsplash.com/photo-1536591375315-1b8368903277?w=600&auto=format&fit=crop', false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, 'https://images.unsplash.com/photo-1536591375315-1b8368903277?w=600&auto=format&fit=crop', 0, false, CURRENT_TIMESTAMP, 'system');

END $$;
