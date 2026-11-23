const fs = require('fs');
const path = require('path');

const products = [
  // ---------------- CATEGORY 1: Quần Áo (ID: 1) ----------------
  {
    category_id: 1,
    name: 'Áo Sơ Mi Nam Oxford Dài Tay Cao Cấp Chống Nhăn',
    description: 'Chất liệu vải Oxford 100% cotton dệt chéo cao cấp, thoáng mát, thấm hút mồ hôi tốt. Thiết kế form regular fit trẻ trung, phù hợp cả đi làm công sở lẫn đi chơi dạo phố. Đường may tỉ mỉ, nút bấm chắc chắn.',
    thumbnail_url: 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop',
    rating: 4.8,
    reviews: 142,
    sold: 580,
    images: [
      'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1603252109303-2751441dd157?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'SM-OXFORD-TRANG-M', attributes: { "Màu sắc": "Trắng", "Kích cỡ": "M" }, price: 289000, quantity: 45, weight: 250, image: 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop' },
      { sku: 'SM-OXFORD-TRANG-L', attributes: { "Màu sắc": "Trắng", "Kích cỡ": "L" }, price: 289000, quantity: 40, weight: 250, image: 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop' },
      { sku: 'SM-OXFORD-XANH-M', attributes: { "Màu sắc": "Xanh Pastel", "Kích cỡ": "M" }, price: 299000, quantity: 35, weight: 250, image: 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&auto=format&fit=crop' },
      { sku: 'SM-OXFORD-XANH-L', attributes: { "Màu sắc": "Xanh Pastel", "Kích cỡ": "L" }, price: 299000, quantity: 30, weight: 250, image: 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 1,
    name: 'Quần Jogger Nam Nỉ Bông Co Giãn Thể Thao',
    description: 'Quần Jogger thể thao chất liệu nỉ da cá dệt dày dặn, giữ ấm tốt nhưng không bí bách. Bo gấu co giãn năng động, túi hai bên có khóa kéo tiện lợi khi vận động hoặc để điện thoại, ví tiền.',
    thumbnail_url: 'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=600&auto=format&fit=crop',
    rating: 4.7,
    reviews: 95,
    sold: 340,
    images: [
      'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1506630448388-4e683c67ddb0?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'JG-NI-XAM-M', attributes: { "Màu sắc": "Xám Khói", "Kích cỡ": "M" }, price: 199000, quantity: 60, weight: 350, image: 'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=600&auto=format&fit=crop' },
      { sku: 'JG-NI-XAM-L', attributes: { "Màu sắc": "Xám Khói", "Kích cỡ": "L" }, price: 199000, quantity: 50, weight: 350, image: 'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=600&auto=format&fit=crop' },
      { sku: 'JG-NI-DEN-L', attributes: { "Màu sắc": "Đen Đậm", "Kích cỡ": "L" }, price: 199000, quantity: 70, weight: 350, image: 'https://images.unsplash.com/photo-1506630448388-4e683c67ddb0?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 1,
    name: 'Áo Len Cổ Lọ Nam Dệt Kim Phong Cách Hàn Quốc',
    description: 'Chất len lông thỏ mềm mượt, không xù lông, không gây ngứa dặm. Giữ ấm vượt trội trong mùa đông lạnh, dễ dàng phối cùng măng tô, áo khoác dạ hoặc blazer tạo vẻ ngoài lịch lãm, hiện đại.',
    thumbnail_url: 'https://images.unsplash.com/photo-1614975058789-41316d0e2e9c?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 68,
    sold: 210,
    images: [
      'https://images.unsplash.com/photo-1614975058789-41316d0e2e9c?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'AL-COLO-KEM-L', attributes: { "Màu sắc": "Kem Be", "Kích cỡ": "L" }, price: 275000, quantity: 35, weight: 400, image: 'https://images.unsplash.com/photo-1614975058789-41316d0e2e9c?w=600&auto=format&fit=crop' },
      { sku: 'AL-COLO-DEN-L', attributes: { "Màu sắc": "Đen", "Kích cỡ": "L" }, price: 275000, quantity: 40, weight: 400, image: 'https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 2: Thời Trang (ID: 2) ----------------
  {
    category_id: 2,
    name: 'Áo Blazer Nữ 2 Lớp Form Rộng Chuẩn Hàn Quốc',
    description: 'Áo vest blazer nữ thiết kế thanh lịch 2 lớp lót lụa mềm mại, đứng form tôn dáng. Thích hợp mặc đi làm, hội họp hoặc phối cùng quần jeans đi cà phê cuối tuần cực kỳ thời thượng.',
    thumbnail_url: 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 215,
    sold: 890,
    images: [
      'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'BLAZER-BE-S', attributes: { "Màu sắc": "Be Nude", "Kích cỡ": "S" }, price: 389000, quantity: 30, weight: 500, image: 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600&auto=format&fit=crop' },
      { sku: 'BLAZER-BE-M', attributes: { "Màu sắc": "Be Nude", "Kích cỡ": "M" }, price: 389000, quantity: 25, weight: 500, image: 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600&auto=format&fit=crop' },
      { sku: 'BLAZER-DEN-M', attributes: { "Màu sắc": "Đen Sang Trọng", "Kích cỡ": "M" }, price: 399000, quantity: 40, weight: 500, image: 'https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 2,
    name: 'Chân Váy Chữ A Lưng Cao Xếp Ly Tôn Dáng',
    description: 'Chân váy chữ A chất vải tuyết mưa cao cấp không bai xù, có quần trong bảo hộ kín đáo giúp chị em tự tin di chuyển. Dễ dàng phối cùng áo phông, áo croptop hay sơ mi.',
    thumbnail_url: 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=600&auto=format&fit=crop',
    rating: 4.8,
    reviews: 84,
    sold: 430,
    images: [
      'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'CV-CHUA-DEN-S', attributes: { "Màu sắc": "Đen", "Kích cỡ": "S" }, price: 175000, quantity: 50, weight: 200, image: 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=600&auto=format&fit=crop' },
      { sku: 'CV-CHUA-DEN-M', attributes: { "Màu sắc": "Đen", "Kích cỡ": "M" }, price: 175000, quantity: 45, weight: 200, image: 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=600&auto=format&fit=crop' },
      { sku: 'CV-CHUA-NAU-M', attributes: { "Màu sắc": "Nâu Tây", "Kích cỡ": "M" }, price: 175000, quantity: 35, weight: 200, image: 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 3: Thiết Bị Điện Tử (ID: 3) ----------------
  {
    category_id: 3,
    name: 'Tai Nghe Bluetooth Chống Ồn Chủ Động Sony WH-1000XM5',
    description: 'Tai nghe chụp tai hàng đầu với công nghệ chống ồn đỉnh cao Auto NC Optimizer. Thời lượng pin lên tới 30 giờ, hỗ trợ sạc nhanh 3 phút nghe được 3 giờ. Âm thanh Hi-Res Audio chuẩn phòng thu.',
    thumbnail_url: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop',
    rating: 5.0,
    reviews: 310,
    sold: 620,
    images: [
      'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'SONY-XM5-BLACK', attributes: { "Màu sắc": "Đen Huyền Bí" }, price: 7990000, quantity: 20, weight: 250, image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop' },
      { sku: 'SONY-XM5-SILVER', attributes: { "Màu sắc": "Bạc Ánh Kim" }, price: 7990000, quantity: 15, weight: 250, image: 'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 3,
    name: 'Bàn Phím Cơ Không Dây RGB Hot-Swap Gasket Mount',
    description: 'Bàn phím cơ layout 75% gọn gàng, kết nối 3 chế độ (Bluetooth 5.1 / Wireless 2.4Ghz / Type-C có dây). Switch Linear êm ái đã được lube sẵn tại nhà máy, pin 4000mAh dùng nhiều tuần liên tục.',
    thumbnail_url: 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 180,
    sold: 520,
    images: [
      'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'KB-GASKET-RED-SW', attributes: { "Switch": "Red Switch (Êm)", "Màu case": "Trắng Xanh" }, price: 1250000, quantity: 40, weight: 950, image: 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop' },
      { sku: 'KB-GASKET-BROWN-SW', attributes: { "Switch": "Brown Switch (Khấc nhẹ)", "Màu case": "Đen Nhám" }, price: 1250000, quantity: 35, weight: 950, image: 'https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 3,
    name: 'Loa Bluetooth Di Động Marshall Emberton II Âm Thanh 360',
    description: 'Loa không dây siêu nhỏ gọn nhưng công suất mạnh mẽ, âm thanh True Stereophonic đặc trưng của Marshall. Kháng bụi kháng nước IP67, pin trâu hơn 30 giờ chơi nhạc chỉ sau một lần sạc đầy.',
    thumbnail_url: 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 165,
    sold: 380,
    images: [
      'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'MS-EMBERTON-BLACK-BRASS', attributes: { "Màu sắc": "Đen Viền Vàng Kim" }, price: 3690000, quantity: 25, weight: 700, image: 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop' },
      { sku: 'MS-EMBERTON-CREAM', attributes: { "Màu sắc": "Kem Cổ Điển" }, price: 3690000, quantity: 20, weight: 700, image: 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 4: Thể Thao (ID: 4) ----------------
  {
    category_id: 4,
    name: 'Vợt Cầu Lông Yonex Astrox 88D Pro Full Carbon Chính Hãng',
    description: 'Dòng vợt tấn công uy lực dành cho người chơi cầu lông từ phong trào tới chuyên nghiệp. Khung vợt ứng dụng vật liệu Namd tân tiến tăng khả năng đàn hồi và lực đập cầu cực cắm.',
    thumbnail_url: 'https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 78,
    sold: 190,
    images: [
      'https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'YNX-88D-3U', attributes: { "Trọng lượng": "3U (88g)", "Độ căng": "11.5kg" }, price: 3850000, quantity: 15, weight: 88, image: 'https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?w=600&auto=format&fit=crop' },
      { sku: 'YNX-88D-4U', attributes: { "Trọng lượng": "4U (83g)", "Độ căng": "11kg" }, price: 3850000, quantity: 20, weight: 83, image: 'https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 4,
    name: 'Thảm Tập Yoga Chống Trượt TPE 8mm Kèm Đường Kẻ Định Tuyến',
    description: 'Thảm Yoga chất liệu TPE thân thiện môi trường, đàn hồi vượt trội, chống trơn trượt tối đa ngay cả khi đổ nhiều mồ hôi. Đường vạch định tuyến chuẩn xác hỗ trợ người tập chuẩn phom thế yoga.',
    thumbnail_url: 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600&auto=format&fit=crop',
    rating: 4.8,
    reviews: 130,
    sold: 670,
    images: [
      'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'THAM-YOGA-TIM', attributes: { "Màu sắc": "Tím Violet", "Độ dày": "8mm" }, price: 219000, quantity: 40, weight: 900, image: 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600&auto=format&fit=crop' },
      { sku: 'THAM-YOGA-XANH', attributes: { "Màu sắc": "Xanh Cốm", "Độ dày": "8mm" }, price: 219000, quantity: 50, weight: 900, image: 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 5: Điện Thoại & Phụ Kiện (ID: 5) ----------------
  {
    category_id: 5,
    name: 'Điện Thoại Apple iPhone 15 Pro Max 256GB Khung Titan',
    description: 'Trang bị chip A17 Pro mạnh mẽ vượt bậc chiến mọi tựa game đồ họa nặng. Khung viền Titan chuẩn hàng không vũ trụ siêu bền nhẹ, màn hình Super Retina XDR ProMotion 120Hz mượt mà và camera tiềm vọng 5x.',
    thumbnail_url: 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop',
    rating: 5.0,
    reviews: 420,
    sold: 1250,
    images: [
      'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'IP15PM-256GB-NATURAL', attributes: { "Màu sắc": "Titan Tự Nhiên", "Dung lượng": "256GB" }, price: 28990000, quantity: 30, weight: 221, image: 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop' },
      { sku: 'IP15PM-256GB-BLACK', attributes: { "Màu sắc": "Titan Đen", "Dung lượng": "256GB" }, price: 28990000, quantity: 25, weight: 221, image: 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop' },
      { sku: 'IP15PM-512GB-BLUE', attributes: { "Màu sắc": "Titan Xanh", "Dung lượng": "512GB" }, price: 33990000, quantity: 15, weight: 221, image: 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 5,
    name: 'Điện Thoại Samsung Galaxy S24 Ultra 5G AI Thông Minh',
    description: 'Quyền năng Galaxy AI đỉnh cao hỗ trợ dịch cuộc gọi thời gian thực, khoanh vùng tìm kiếm đa năng. Bút S-Pen tích hợp tiện lợi, camera mắt thần bóng đêm 200MP và chip Snapdragon 8 Gen 3 for Galaxy.',
    thumbnail_url: 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 350,
    sold: 980,
    images: [
      'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'SS-S24U-256GB-XAM', attributes: { "Màu sắc": "Xám Titan", "Dung lượng": "256GB" }, price: 26990000, quantity: 25, weight: 232, image: 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop' },
      { sku: 'SS-S24U-512GB-DEN', attributes: { "Màu sắc": "Đen Titan", "Dung lượng": "512GB" }, price: 30990000, quantity: 20, weight: 232, image: 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 5,
    name: 'Củ Sạc Nhanh Anker GaNPrime 65W 3 Cổng Tiện Lợi',
    description: 'Công nghệ bán dẫn Gallium Nitride (GaN) giúp củ sạc nhỏ gọn hơn 53% so với củ sạc thông thường. 2 cổng Type-C và 1 cổng USB-A sạc đồng thời cho cả MacBook, iPad và điện thoại an toàn.',
    thumbnail_url: 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 280,
    sold: 1400,
    images: [
      'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'ANKER-GAN-65W-DEN', attributes: { "Màu sắc": "Đen Nhám" }, price: 790000, quantity: 80, weight: 130, image: 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop' },
      { sku: 'ANKER-GAN-65W-TRANG', attributes: { "Màu sắc": "Trắng Tinh Khôi" }, price: 790000, quantity: 60, weight: 130, image: 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 5,
    name: 'Pin Sạc Dự Phòng Không Dây Từ Tính MagSafe 10.000mAh',
    description: 'Hít nam châm cực mạnh tương thích chuẩn MagSafe, sạc không dây 15W tiện dụng và sạc nhanh có dây 20W chuẩn PD. Màn hình LED thông minh hiển thị chính xác % pin còn lại.',
    thumbnail_url: 'https://images.unsplash.com/photo-1609592424109-dd9892f1b177?w=600&auto=format&fit=crop',
    rating: 4.8,
    reviews: 190,
    sold: 890,
    images: [
      'https://images.unsplash.com/photo-1609592424109-dd9892f1b177?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'PB-MAGSAFE-10K-XAM', attributes: { "Màu sắc": "Xám Không Gian", "Dung lượng": "10000mAh" }, price: 490000, quantity: 50, weight: 210, image: 'https://images.unsplash.com/photo-1609592424109-dd9892f1b177?w=600&auto=format&fit=crop' },
      { sku: 'PB-MAGSAFE-10K-TRANG', attributes: { "Màu sắc": "Trắng Ngọc Trai", "Dung lượng": "10000mAh" }, price: 490000, quantity: 45, weight: 210, image: 'https://images.unsplash.com/photo-1609592424109-dd9892f1b177?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 6: Máy Tính & Laptop (ID: 6) ----------------
  {
    category_id: 6,
    name: 'Apple MacBook Air 13.6 inch M3 (16GB RAM / 256GB SSD)',
    description: 'Thiết kế nhôm nguyên khối siêu mỏng nhẹ chỉ 1.24kg. Chip Apple M3 thế hệ mới xử lý mượt mà tác vụ đồ họa và lập trình, pin dùng liên tục lên đến 18 tiếng cả ngày dài.',
    thumbnail_url: 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop',
    rating: 5.0,
    reviews: 180,
    sold: 490,
    images: [
      'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop',
      'https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'MBA-M3-16-256-MIDNIGHT', attributes: { "Màu sắc": "Midnight (Xanh Đen)", "Bộ nhớ": "16GB RAM - 256GB SSD" }, price: 27990000, quantity: 15, weight: 1240, image: 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop' },
      { sku: 'MBA-M3-16-256-STARLIGHT', attributes: { "Màu sắc": "Starlight (Ánh Sao)", "Bộ nhớ": "16GB RAM - 256GB SSD" }, price: 27990000, quantity: 20, weight: 1240, image: 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop' },
      { sku: 'MBA-M3-16-512-SPACEGRAY', attributes: { "Màu sắc": "Xám Không Gian", "Bộ nhớ": "16GB RAM - 512GB SSD" }, price: 32990000, quantity: 12, weight: 1240, image: 'https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 6,
    name: 'Laptop Gaming ASUS ROG Zephyrus G16 OLED Core Ultra 9',
    description: 'Cỗ máy gaming đỉnh cao mỏng nhẹ ấn tượng. Màn hình ROG Nebula Display OLED 2.5K 240Hz sắc nét rực rỡ, card đồ họa NVIDIA GeForce RTX 4070 cân mọi game AAA và render video 4K siêu tốc.',
    thumbnail_url: 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 65,
    sold: 140,
    images: [
      'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'ROG-G16-RTX4070-32GB', attributes: { "Cấu hình": "Ultra 9 / 32GB RAM / 1TB SSD / RTX 4070", "Màu sắc": "Eclipse Gray" }, price: 54990000, quantity: 10, weight: 1850, image: 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 6,
    name: 'Màn Hình Đồ Họa Chuyên Nghiệp Dell UltraSharp 27 Inch 4K IPS',
    description: 'Độ phủ màu 100% sRGB và 98% DCI-P3 cực chuẩn xác cho dân thiết kế, chỉnh sửa ảnh phim. Cổng kết nối USB-C hỗ trợ truyền hình ảnh và sạc ngược công suất 90W cho laptop gọn gàng.',
    thumbnail_url: 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 110,
    sold: 280,
    images: [
      'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'DELL-U2723QE-4K', attributes: { "Kích thước": "27 inch 4K IPS", "Cổng kết nối": "Type-C 90W Hub" }, price: 12890000, quantity: 20, weight: 6500, image: 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 7: Gia Dụng & Đời Sống (ID: 7) ----------------
  {
    category_id: 7,
    name: 'Nồi Chiên Không Dầu Điện Tử Philips 6.2 Lít XXL Rapid Air',
    description: 'Dung tích khủng 6.2L chiên nguyên con gà 1.5kg dễ dàng. Công nghệ Rapid Air độc quyền giảm tới 90% lượng dầu mỡ thừa, bảng điều khiển cảm ứng với 7 chương trình nấu cài đặt sẵn.',
    thumbnail_url: 'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600&auto=format&fit=crop',
    rating: 4.8,
    reviews: 230,
    sold: 760,
    images: [
      'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'PHILIPS-NCKD-6L2-DEN', attributes: { "Màu sắc": "Đen Bóng", "Dung tích": "6.2L" }, price: 2690000, quantity: 35, weight: 5500, image: 'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 7,
    name: 'Robot Hút Bụi Lau Nhà Tự Giặt Giẻ Dreame L20 Ultra',
    description: 'Công nghệ cánh tay robot MopExtend tiếp cận sát chân tường và ngóc ngách. Lực hút khủng 7000Pa hút sạch mọi bụi mịn, trạm sạc đa năng tự giặt giẻ nước nóng và sấy khô kháng khuẩn.',
    thumbnail_url: 'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=600&auto=format&fit=crop',
    rating: 5.0,
    reviews: 95,
    sold: 180,
    images: [
      'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'DREAME-L20U-TRANG', attributes: { "Màu sắc": "Trắng Tinh Tế" }, price: 18990000, quantity: 15, weight: 12000, image: 'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 7,
    name: 'Máy Pha Cà Phê Tự Động DeLonghi Magnifica S Nhập Khẩu Ý',
    description: 'Thưởng thức Espresso và Cappuccino chuẩn vị Ý ngay tại nhà chỉ với một nút bấm. Tích hợp cối xay hạt thép không gỉ với 13 cấp độ xay mịn, vòi tạo bọt sữa bằng tay bông dày sánh mịn.',
    thumbnail_url: 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 140,
    sold: 230,
    images: [
      'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'DELONGHI-ECAM-DEN', attributes: { "Màu sắc": "Đen Bạc Cổ Điển" }, price: 14500000, quantity: 12, weight: 9000, image: 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 8: Mỹ Phẩm & Sắc Đẹp (ID: 8) ----------------
  {
    category_id: 8,
    name: 'Serum Phục Hồi Và Làm Sáng Da La Roche-Posay Hyalu B5 30ml',
    description: 'Công thức dưỡng chất độc đáo gồm 2 loại Hyaluronic Acid nguyên chất, Vitamin B5 và Madecassoside giúp tái tạo phục hồi hàng rào bảo vệ da, cấp ẩm sâu cho da căng mọng đàn hồi.',
    thumbnail_url: 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 490,
    sold: 2100,
    images: [
      'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'LRP-HYALU-B5-30ML', attributes: { "Dung tích": "30ml" }, price: 890000, quantity: 100, weight: 150, image: 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop' },
      { sku: 'LRP-HYALU-B5-50ML', attributes: { "Dung tích": "50ml (Tiết kiệm)" }, price: 1290000, quantity: 60, weight: 200, image: 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 8,
    name: 'Kem Chống Nắng Kiềm Dầu Anessa Perfect UV Milk SPF50+ PA++++',
    description: 'Sữa chống nắng quốc dân đến từ Nhật Bản với công nghệ Auto Booster chống trôi nước, mồ hôi và ma sát cực tốt. Kết cấu sữa mỏng nhẹ, nâng tông tự nhiên và kiềm dầu suốt cả ngày dài.',
    thumbnail_url: 'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 620,
    sold: 3400,
    images: [
      'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'ANESSA-GOLD-60ML', attributes: { "Dung tích": "60ml" }, price: 545000, quantity: 150, weight: 90, image: 'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 8,
    name: 'Son Thỏi Lì Cao Cấp YSL Rouge Pur Couture The Slim Velvet',
    description: 'Thiết kế thỏi vuông vắn sang trọng đặc trưng của Yves Saint Laurent. Chất son mịn như nhung matte nhưng không hề làm khô môi, màu sắc lên chuẩn xác tôn da phụ nữ Á Đông.',
    thumbnail_url: 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 310,
    sold: 1100,
    images: [
      'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'YSL-SLIM-301', attributes: { "Mã màu": "Màu 301 Nude Pulsion (Cam Đất Trầm)" }, price: 980000, quantity: 40, weight: 40, image: 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&auto=format&fit=crop' },
      { sku: 'YSL-SLIM-302', attributes: { "Mã màu": "Màu 302 Brown No Way Back (Đỏ Đất Quyến Rũ)" }, price: 980000, quantity: 50, weight: 40, image: 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 9: Giày Dép & Phụ Kiện (ID: 9) ----------------
  {
    category_id: 9,
    name: 'Giày Thể Thao Sneaker Nike Air Force 1 \'07 All White',
    description: 'Biểu tượng giày sneaker huyền thoại không bao giờ lỗi mốt. Da thật cao cấp êm ái, đế đệm Air êm nhẹ mang lại sự thoải mái trong từng bước chân cả ngày.',
    thumbnail_url: 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop',
    rating: 5.0,
    reviews: 410,
    sold: 1650,
    images: [
      'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'NIKE-AF1-WHT-40', attributes: { "Màu sắc": "Trắng Toàn Bộ", "Kích cỡ": "40 EU" }, price: 2690000, quantity: 30, weight: 800, image: 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop' },
      { sku: 'NIKE-AF1-WHT-41', attributes: { "Màu sắc": "Trắng Toàn Bộ", "Kích cỡ": "41 EU" }, price: 2690000, quantity: 35, weight: 800, image: 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop' },
      { sku: 'NIKE-AF1-WHT-42', attributes: { "Màu sắc": "Trắng Toàn Bộ", "Kích cỡ": "42 EU" }, price: 2690000, quantity: 25, weight: 800, image: 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 9,
    name: 'Giày Chạy Bộ Nam Nữ Siêu Nhẹ Siêu Êm Đàn Hồi',
    description: 'Thân giày vải dệt Flyknit thoáng khí tản nhiệt tốt, đệm bọt Boost hoàn trả năng lượng tối ưu giúp bước chạy nhẹ nhàng êm ái, bảo vệ khớp gối khi vận động cường độ cao.',
    thumbnail_url: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop',
    rating: 4.8,
    reviews: 180,
    sold: 720,
    images: [
      'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'RUN-SHOES-DO-41', attributes: { "Màu sắc": "Đỏ Nổi Bật", "Kích cỡ": "41" }, price: 850000, quantity: 30, weight: 600, image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop' },
      { sku: 'RUN-SHOES-DO-42', attributes: { "Màu sắc": "Đỏ Nổi Bật", "Kích cỡ": "42" }, price: 850000, quantity: 20, weight: 600, image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 10: Đồng Hồ & Trang Sức (ID: 10) ----------------
  {
    category_id: 10,
    name: 'Đồng Hồ Nam Cơ Tự Động Seiko 5 Sports Automatic Kính Hardlex',
    description: 'Bộ máy tự động 4R36 bền bỉ của Seiko với 24 chân kính, khả năng trữ cót lên đến 41 giờ. Chống nước 100 mét (10 ATM), mặt kính Hardlex chống trầy xước và kim dạ quang LumiBrite sáng rực trong đêm.',
    thumbnail_url: 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 135,
    sold: 360,
    images: [
      'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'SEIKO5-DEN-THEP', attributes: { "Mặt số": "Mặt Đen", "Dây": "Thép Không Gỉ 316L" }, price: 5490000, quantity: 20, weight: 145, image: 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop' },
      { sku: 'SEIKO5-XANH-THEP', attributes: { "Mặt số": "Mặt Xanh Biển", "Dây": "Thép Không Gỉ 316L" }, price: 5490000, quantity: 15, weight: 145, image: 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 10,
    name: 'Đồng Hồ Thông Minh Apple Watch Series 9 GPS Viền Nhôm',
    description: 'Chip S9 SiP thế hệ mới đem lại thao tác cử chỉ chạm hai lần (Double Tap) kỳ diệu mà không cần chạm tay vào màn hình. Đo nồng độ oxy trong máu, điện tâm đồ ECG và theo dõi giấc ngủ chi tiết.',
    thumbnail_url: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 260,
    sold: 790,
    images: [
      'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'AW-S9-41-MIDNIGHT', attributes: { "Kích thước": "41mm", "Màu sắc": "Midnight (Nhôm Tối)" }, price: 9290000, quantity: 25, weight: 32, image: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop' },
      { sku: 'AW-S9-45-STARLIGHT', attributes: { "Kích thước": "45mm", "Màu sắc": "Starlight (Nhôm Ánh Sao)" }, price: 9990000, quantity: 20, weight: 39, image: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 11: Sách & Văn Phòng Phẩm (ID: 11) ----------------
  {
    category_id: 11,
    name: 'Sách Atomic Habits - Thay Đổi Tí Hon Hiệu Quả Bất Ngờ (Tái Bản)',
    description: 'Cuốn sách bán chạy hàng đầu thế giới của James Clear chia sẻ hệ thống từng bước đã được khoa học chứng minh giúp bạn xây dựng thói quen tốt và từ bỏ thói quen xấu một cách dễ dàng, bền bỉ.',
    thumbnail_url: 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop',
    rating: 5.0,
    reviews: 580,
    sold: 3800,
    images: [
      'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'BOOK-ATOMIC-HABITS-BIA-MEM', attributes: { "Phiên bản": "Bìa Mềm Tay Gấp" }, price: 159000, quantity: 150, weight: 450, image: 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop' },
      { sku: 'BOOK-ATOMIC-HABITS-BIA-CUNG', attributes: { "Phiên bản": "Bìa Cứng Đặc Biệt (Kèm Bookmark)" }, price: 239000, quantity: 80, weight: 650, image: 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 11,
    name: 'Bút Ký Kim Loại Khắc Tên Cao Cấp Kèm Hộp Đựng Sang Trọng',
    description: 'Vỏ hợp kim nguyên khối sơn tĩnh điện phủ bóng chống trầy, ngòi bút mực gel bi lăn nhập khẩu Đức viết trơn êm không tắc mực. Món quà tặng ý nghĩa cho đối tác, sếp, bạn bè.',
    thumbnail_url: 'https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=600&auto=format&fit=crop',
    rating: 4.8,
    reviews: 95,
    sold: 410,
    images: [
      'https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'BUT-KY-DEN-VANG', attributes: { "Màu thân": "Đen Cài Vàng Kim", "Ngòi": "0.5mm Mực Đen" }, price: 185000, quantity: 60, weight: 120, image: 'https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 12: Mẹ & Bé (ID: 12) ----------------
  {
    category_id: 12,
    name: 'Xe Đẩy Em Bé Gấp Gọn Siêu Nhẹ Đi Máy Bay Du Lịch',
    description: 'Trọng lượng chỉ 4.8kg, dễ dàng gấp gọn bằng một tay xách lên khoang hành lý máy bay. Lưng ghế ngả đa nấc 100° - 175° cho bé nằm hoặc ngồi thoải mái, mái che chống tia UV 50+.',
    thumbnail_url: 'https://images.unsplash.com/photo-1519689680058-324335c77eba?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 110,
    sold: 320,
    images: [
      'https://images.unsplash.com/photo-1519689680058-324335c77eba?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'XE-DAY-XAM', attributes: { "Màu sắc": "Xám Ghi Cao Cấp" }, price: 1450000, quantity: 20, weight: 4800, image: 'https://images.unsplash.com/photo-1519689680058-324335c77eba?w=600&auto=format&fit=crop' },
      { sku: 'XE-DAY-HONG', attributes: { "Màu sắc": "Hồng Pastel" }, price: 1450000, quantity: 15, weight: 4800, image: 'https://images.unsplash.com/photo-1519689680058-324335c77eba?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 12,
    name: 'Máy Tiệt Trùng Sấy Khô Khử Mùi Bình Sữa Bằng Tia UVC Đa Năng',
    description: 'Công nghệ khử khuẩn tia UVC 360 độ tiêu diệt 99.9% vi khuẩn có hại chỉ trong vài phút. Dung tích khoang chứa rộng rãi để vừa 12 bình sữa cùng núm ti và phụ kiện ăn dặm.',
    thumbnail_url: 'https://images.unsplash.com/photo-1515488042361-ee00e0ddd4e4?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 145,
    sold: 450,
    images: [
      'https://images.unsplash.com/photo-1515488042361-ee00e0ddd4e4?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'MAY-TIET-TRUNG-UVC-TRANG', attributes: { "Màu sắc": "Trắng Sữa", "Dung tích": "18L" }, price: 1890000, quantity: 25, weight: 3500, image: 'https://images.unsplash.com/photo-1515488042361-ee00e0ddd4e4?w=600&auto=format&fit=crop' }
    ]
  },

  // ---------------- CATEGORY 13: Bách Hóa Online (ID: 13) ----------------
  {
    category_id: 13,
    name: 'Cà Phê Hạt Rang Mộc Arabica Cầu Đất Thượng Hạng 500g',
    description: '100% hạt cà phê Arabica tuyển chọn từ vùng cao nguyên Cầu Đất - Đà Lạt ở độ cao 1.600m. Hương thơm hoa quả dịu nhẹ quyến rũ, vị chua thanh tự nhiên cùng hậu vị ngọt sâu lắng.',
    thumbnail_url: 'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=600&auto=format&fit=crop',
    rating: 5.0,
    reviews: 320,
    sold: 1850,
    images: [
      'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'CAFE-ARABICA-HAT-500G', attributes: { "Quy cách": "Hạt Nguyên Chất Rang Mộc", "Khối lượng": "500g" }, price: 185000, quantity: 80, weight: 520, image: 'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=600&auto=format&fit=crop' },
      { sku: 'CAFE-ARABICA-XAY-500G', attributes: { "Quy cách": "Bột Xay Pha Phin / Pha Máy", "Khối lượng": "500g" }, price: 185000, quantity: 100, weight: 520, image: 'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=600&auto=format&fit=crop' }
    ]
  },
  {
    category_id: 13,
    name: 'Set 5 Hộp Hạt Dinh Dưỡng Cao Cấp Nhập Khẩu (Hạt Óc Chó, Macca, Hạnh Nhân)',
    description: 'Hạt dinh dưỡng sấy giòn tự nhiên không tẩm ướp đường hóa học hay muối, giữ trọn vẹn chất chống oxy hóa, Omega 3-6 tốt cho tim mạch và não bộ. Món quà sức khỏe tuyệt hảo cho gia đình.',
    thumbnail_url: 'https://images.unsplash.com/photo-1536591375315-1b8368903277?w=600&auto=format&fit=crop',
    rating: 4.9,
    reviews: 210,
    sold: 940,
    images: [
      'https://images.unsplash.com/photo-1536591375315-1b8368903277?w=600&auto=format&fit=crop'
    ],
    variants: [
      { sku: 'SET-HAT-5-HOP-500G', attributes: { "Quy cách": "Set 5 Hũ x 500g" }, price: 420000, quantity: 45, weight: 2600, image: 'https://images.unsplash.com/photo-1536591375315-1b8368903277?w=600&auto=format&fit=crop' }
    ]
  }
];

function escapeSql(str) {
  if (!str) return "''";
  return "'" + str.replace(/'/g, "''") + "'";
}

let sql = `-- AUTO-GENERATED PRODUCT SEED DATA
DO $$
DECLARE
  v_prod_id BIGINT;
BEGIN
`;

for (const p of products) {
  const minPrice = Math.min(...p.variants.map(v => v.price));
  sql += `
  -- ========================================================
  -- Product: ${p.name}
  -- ========================================================
  INSERT INTO products (name, description, category_id, thumbnail_url, average_rating, review_count, min_price, sold_count, is_deleted, created_at, created_by)
  VALUES (
    ${escapeSql(p.name)},
    ${escapeSql(p.description)},
    ${p.category_id},
    ${escapeSql(p.thumbnail_url)},
    ${p.rating},
    ${p.reviews},
    ${minPrice},
    ${p.sold},
    false,
    CURRENT_TIMESTAMP,
    'system'
  )
  RETURNING id INTO v_prod_id;

`;

  // Variants
  for (const v of p.variants) {
    const attrJson = escapeSql(JSON.stringify(v.attributes));
    sql += `  INSERT INTO product_variants (product_id, sku, attributes, price, quantity, weight, image_url, is_deleted, created_at, created_by)
  VALUES (v_prod_id, ${escapeSql(v.sku)}, ${attrJson}::jsonb, ${v.price}, ${v.quantity}, ${v.weight}, ${escapeSql(v.image)}, false, CURRENT_TIMESTAMP, 'system')
  ON CONFLICT (sku) DO NOTHING;
`;
  }

  // Images
  p.images.forEach((img, idx) => {
    sql += `  INSERT INTO product_images (product_id, image_url, display_order, is_deleted, created_at, created_by)
  VALUES (v_prod_id, ${escapeSql(img)}, ${idx}, false, CURRENT_TIMESTAMP, 'system');
`;
  });
}

sql += `
END $$;
`;

const outputPath = path.join(__dirname, 'seed_products_full.sql');
fs.writeFileSync(outputPath, sql, 'utf8');
console.log('Successfully generated SQL script at:', outputPath);

