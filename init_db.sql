-- Script khởi tạo Database cho dự án Đặt Phòng Khách Sạn (Bản nâng cao)
CREATE DATABASE IF NOT EXISTS hotel_booking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_booking_db;

-- Xóa bảng cũ nếu tồn tại (theo thứ tự tránh lỗi khóa ngoại)
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS hotel_amenities;
DROP TABLE IF EXISTS hotels;
DROP TABLE IF EXISTS users;

-- 1. Bảng User
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    full_name VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng Khách Sạn
CREATE TABLE IF NOT EXISTS hotels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL, -- Giá từ (min price)
    rating FLOAT DEFAULT 0.0,
    image_url VARCHAR(255),
    latitude DOUBLE,  -- Tọa độ bản đồ
    longitude DOUBLE, -- Tọa độ bản đồ
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Bảng Tiện Ích của Khách Sạn (Amenities)
CREATE TABLE IF NOT EXISTS hotel_amenities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    amenity_name VARCHAR(100) NOT NULL,
    icon_name VARCHAR(100),
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE
);

-- 4. Bảng Phòng (Rooms)
CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    room_type VARCHAR(50) NOT NULL, -- Standard, Deluxe, Suite...
    price_per_night DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE', -- AVAILABLE, MAINTENANCE
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE
);

-- 5. Bảng Đặt Phòng (Bookings)
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED', -- CONFIRMED, CANCELLED, COMPLETED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

-- 6. Bảng Đánh Giá (Reviews)
CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    hotel_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE
);

-- CHÈN DỮ LIỆU MẪU

-- Khách Sạn (Thêm tọa độ Map và URL ảnh thật)
INSERT INTO hotels (name, location, description, price, rating, image_url, latitude, longitude) VALUES
('Mường Thanh Luxury', 'Hà Nội', 'Khách sạn 5 sao cao cấp với đầy đủ tiện nghi.', 1500000.00, 4.8, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&q=80', 21.028511, 105.804817),
('Vinpearl Resort', 'Nha Trang', 'Khu nghỉ dưỡng tuyệt đẹp bên bờ biển.', 2500000.00, 4.9, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=800&q=80', 12.217316, 109.241577),
('Pullman Saigon Centre', 'Hồ Chí Minh', 'Khách sạn hiện đại nằm ngay trung tâm.', 2000000.00, 4.7, 'https://images.unsplash.com/photo-1542314831-c6a4d14b1bdf?w=800&q=80', 10.763428, 106.691764),
('InterContinental', 'Đà Nẵng', 'Khu nghỉ dưỡng sang trọng bậc nhất.', 3500000.00, 5.0, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800&q=80', 16.126084, 108.315053),
('Novotel', 'Phú Quốc', 'Khách sạn tiêu chuẩn quốc tế.', 1800000.00, 4.5, 'https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=800&q=80', 10.165431, 103.974052),
('Sheraton Hà Nội', 'Hà Nội', 'Khách sạn quốc tế cao cấp bên Hồ Tây thơ mộng. Dịch vụ chuẩn Marriott.', 2200000.00, 4.7, 'https://images.unsplash.com/photo-1618773928121-c32242fa1199?w=800&q=80', 21.056151, 105.827362),
('Fusion Maia Resort', 'Đà Nẵng', 'Resort phong cách tối giản bên bờ biển. Gói spa trọn gói miễn phí.', 3000000.00, 4.8, 'https://images.unsplash.com/photo-1517840901100-8179e982acb7?w=800&q=80', 16.035414, 108.251411),
('JW Marriott Phú Quốc', 'Phú Quốc', 'Resort 5 sao đẳng cấp lấy cảm hứng từ Đại học tưởng tượng. Bãi biển riêng.', 3500000.00, 4.9, 'https://images.unsplash.com/photo-1522798514-97ceb8c4f1c8?w=800&q=80', 10.038477, 104.032230),
('La Siesta Hội An', 'Hội An', 'Boutique hotel phong cách Đông Dương giữa phố cổ Hội An. Lớp nấu ăn miễn phí.', 1200000.00, 4.6, 'https://images.unsplash.com/photo-1496417263034-38ec4f0b665a?w=800&q=80', 15.879441, 108.319323),
('Colline Hotel', 'Đà Lạt', 'Khách sạn mang phong cách kiến trúc Pháp cổ điển ngay trung tâm chợ Đà Lạt.', 1300000.00, 4.5, 'https://images.unsplash.com/photo-1549294413-26f195200c16?w=800&q=80', 11.940419, 108.437253),
('Hôtel de la Coupole', 'Sa Pa', 'Kiến trúc đỉnh cao kết hợp giữa thời trang Pháp và sắc màu Tây Bắc.', 3200000.00, 4.9, 'https://images.unsplash.com/photo-1561501878-aabd62634533?w=800&q=80', 22.335272, 103.843799),
('Imperial Vũng Tàu', 'Vũng Tàu', 'Khách sạn phong cách phục hưng duy nhất tại bãi sau Vũng Tàu.', 2100000.00, 4.6, 'https://images.unsplash.com/photo-1584132967334-10e028bd69f7?w=800&q=80', 10.339243, 107.094595),
('Vinpearl Resort & Spa', 'Hạ Long', 'Lâu đài nguy nga tráng lệ trên đảo Rều với tầm nhìn 360 độ ra vịnh.', 2800000.00, 4.8, 'https://images.unsplash.com/photo-1621293954908-907159247fc8?w=800&q=80', 20.941951, 106.989218),
('Melia Hồ Tràm', 'Vũng Tàu', 'Khu nghỉ dưỡng biển sang trọng bậc nhất với phong cách thiết kế đương đại.', 3800000.00, 4.9, 'https://images.unsplash.com/photo-1551882547-ff40c0d589rx?w=800&q=80', 10.457884, 107.399587),
('Amanoi Resort', 'Ninh Thuận', 'Khu nghỉ dưỡng siêu cao cấp ẩn mình giữa Vườn Quốc Gia Núi Chúa.', 15000000.00, 5.0, 'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=800&q=80', 11.724578, 109.176465),
('Silk Path Grand', 'Huế', 'Kiến trúc Đông Dương thanh lịch nằm ngay bên dòng sông Hương thơ mộng.', 1800000.00, 4.7, 'https://images.unsplash.com/photo-1560662105-57f8ad6fa5f1?w=800&q=80', 16.463713, 107.590533),
('FLC Luxury Resort', 'Quy Nhơn', 'Biệt thự biển ngắm bình minh đẹp nhất Việt Nam với sân Golf 36 hố.', 2200000.00, 4.6, 'https://images.unsplash.com/photo-1540541338287-41700207dee6?w=800&q=80', 13.882194, 109.255478),
('Anantara Resort', 'Quy Nhơn', 'Ốc đảo nghỉ dưỡng ẩn mình giữa ba mặt núi và một mặt biển khơi.', 8500000.00, 4.9, 'https://images.unsplash.com/photo-1521591871239-011dd5d57b32?w=800&q=80', 13.722511, 109.231267),
('Zannier Hotels Bãi San Hô', 'Phú Yên', 'Thiên đường sinh thái nguyên sơ rộng 98ha dọc bờ biển Phú Yên.', 9000000.00, 5.0, 'https://images.unsplash.com/photo-1510798831971-661eb04b3739?w=800&q=80', 13.411620, 109.243572),
('The Anam', 'Mũi Né', 'Resort mang đậm phong cách kiến trúc Indochine thanh lịch.', 3100000.00, 4.8, 'https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800&q=80', 10.957262, 108.283188),
('Centara Mirage Resort', 'Mũi Né', 'Khu nghỉ dưỡng giải trí theo chủ đề Tây Ban Nha với công viên nước.', 2600000.00, 4.5, 'https://images.unsplash.com/photo-1541971875076-8f970d573be6?w=800&q=80', 10.963478, 108.297486),
('Azerai Cần Thơ', 'Cần Thơ', 'Khu nghỉ dưỡng biệt lập tọa lạc trên Cồn Ấu thơ mộng.', 4500000.00, 4.8, 'https://images.unsplash.com/photo-1498503182468-3b51cbb6cb24?w=800&q=80', 10.024564, 105.794321),
('Victoria Resort', 'Cần Thơ', 'Khách sạn duy nhất nằm bên bờ sông Hậu mang đậm nét văn hóa Tây Đô.', 2000000.00, 4.6, 'https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?w=800&q=80', 10.035411, 105.787623),
('Six Senses Ninh Vân Bay', 'Nha Trang', 'Kiệt tác nghỉ dưỡng sinh thái tàng hình giữa thiên nhiên hùng vĩ.', 16000000.00, 5.0, 'https://images.unsplash.com/photo-1561501900-3701fa6a0864?w=800&q=80', 12.364448, 109.282631),
('Amiana Resort', 'Nha Trang', 'Khu nghỉ dưỡng sở hữu hồ bơi nước biển lớn nhất Nha Trang.', 3400000.00, 4.8, 'https://images.unsplash.com/photo-1596436889106-be35e843f974?w=800&q=80', 12.302511, 109.215321),
('Naman Retreat', 'Đà Nẵng', 'Sự pha trộn hài hòa giữa di sản văn hóa Việt Nam và kiến trúc tre đương đại.', 4200000.00, 4.7, 'https://images.unsplash.com/photo-1505577058444-a3abab9dc6e4?w=800&q=80', 15.986324, 108.283124),
('Hyatt Regency', 'Đà Nẵng', 'Resort 5 sao ven biển tuyệt đẹp thích hợp cho gia đình và khách doanh nhân.', 3800000.00, 4.6, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800&q=80', 16.012351, 108.261435),
('Sofitel Legend Metropole', 'Hà Nội', 'Khách sạn biểu tượng lịch sử hơn 120 năm tuổi giữa lòng thủ đô.', 5500000.00, 4.9, 'https://images.unsplash.com/photo-1542314831-c6a4d14b1bdf?w=800&q=80', 21.025345, 105.856231),
('Capella Hà Nội', 'Hà Nội', 'Kiệt tác nghệ thuật tôn vinh thời hoàng kim của Opera do Bill Bensley thiết kế.', 8000000.00, 5.0, 'https://images.unsplash.com/photo-1618773928121-c32242fa1199?w=800&q=80', 21.027415, 105.853312),
('Lotte Hotel', 'Hà Nội', 'Khách sạn nằm trên cao ngắm trọn vẹn khung cảnh thủ đô sôi động.', 2800000.00, 4.7, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&q=80', 21.031523, 105.812323),
('The Reverie Saigon', 'Hồ Chí Minh', 'Tuyệt tác thiết kế siêu sang trọng mang đậm dấu ấn Ý giữa trung tâm Sài Gòn.', 6500000.00, 4.9, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=800&q=80', 10.774513, 106.702315);

-- Tiện Ích
INSERT INTO hotel_amenities (hotel_id, amenity_name, icon_name) VALUES
(1, 'Wifi miễn phí', 'ic_wifi'), (1, 'Hồ bơi', 'ic_pool'), (1, 'Bữa sáng', 'ic_breakfast'),
(2, 'Spa', 'ic_spa'), (2, 'Hồ bơi vô cực', 'ic_pool'), (2, 'Bãi biển riêng', 'ic_beach'),
(3, 'Phòng Gym', 'ic_gym'), (3, 'Bar', 'ic_bar'), (3, 'Wifi tốc độ cao', 'ic_wifi'),
(4, 'Nhà hàng 5 sao', 'ic_restaurant'), (4, 'Hồ bơi', 'ic_pool'),
(5, 'Bãi biển', 'ic_beach'), (5, 'Spa', 'ic_spa'),
(6, 'Hồ bơi', 'ic_pool'), (6, 'Spa', 'ic_spa'), (6, 'Nhà hàng', 'ic_restaurant'),
(7, 'Spa miễn phí', 'ic_spa'), (7, 'Yoga', 'ic_gym'), (7, 'Hồ bơi', 'ic_pool'),
(8, 'Waterpark', 'ic_pool'), (8, 'Golf', 'ic_gym'), (8, 'Kids Club', 'ic_star'),
(9, 'Xe đạp', 'ic_star'), (9, 'Nấu ăn', 'ic_restaurant'), (9, 'Hồ bơi', 'ic_pool'),
(10, 'Wifi miễn phí', 'ic_wifi'), (10, 'View phố', 'ic_star'), (10, 'Nhà hàng', 'ic_restaurant'),
(11, 'Hồ bơi nước nóng', 'ic_pool'), (11, 'Nhà hàng 5 sao', 'ic_restaurant'), (11, 'Spa', 'ic_spa'),
(12, 'Bãi biển', 'ic_beach'), (12, 'Bar', 'ic_bar'), (12, 'Nhà hàng', 'ic_restaurant'),
(13, 'Hồ bơi', 'ic_pool'), (13, 'Phòng Gym', 'ic_gym'), (13, 'Spa', 'ic_spa'),
(14, 'Bãi biển', 'ic_beach'), (14, 'Hồ bơi vô cực', 'ic_pool'), (14, 'Yoga', 'ic_gym'),
(15, 'Bãi biển riêng', 'ic_beach'), (15, 'Yoga', 'ic_gym'), (15, 'Spa miễn phí', 'ic_spa'),
(16, 'Nhà hàng', 'ic_restaurant'), (16, 'Bữa sáng', 'ic_breakfast'), (16, 'Hồ bơi', 'ic_pool'),
(17, 'Sân Golf', 'ic_gym'), (17, 'Bãi biển', 'ic_beach'), (17, 'Hồ bơi lớn', 'ic_pool'),
(18, 'Spa', 'ic_spa'), (18, 'View Biển', 'ic_beach'), (18, 'Phòng Gym', 'ic_gym'),
(19, 'Kiến trúc Việt', 'ic_star'), (19, 'Bãi biển hoang sơ', 'ic_beach'), (19, 'Hồ bơi', 'ic_pool'),
(20, 'Bãi biển', 'ic_beach'), (20, 'Nhà hàng Âu Á', 'ic_restaurant'), (20, 'Wifi', 'ic_wifi'),
(21, 'Công viên nước', 'ic_pool'), (21, 'Kids Club', 'ic_star'), (21, 'Spa', 'ic_spa'),
(22, 'Cano đưa đón', 'ic_star'), (22, 'Hồ bơi vách kính', 'ic_pool'), (22, 'Yoga', 'ic_gym'),
(23, 'Sông nước', 'ic_star'), (23, 'Nhà hàng', 'ic_restaurant'), (23, 'Hồ bơi', 'ic_pool'),
(24, 'Villa biệt lập', 'ic_star'), (24, 'Quản gia riêng', 'ic_star'), (24, 'Bãi biển', 'ic_beach'),
(25, 'Tắm bùn', 'ic_spa'), (25, 'Hồ bơi nước mặn', 'ic_pool'), (25, 'Bãi biển', 'ic_beach'),
(26, 'Kiến trúc tre', 'ic_star'), (26, 'Spa miễn phí', 'ic_spa'), (26, 'Hồ bơi', 'ic_pool'),
(27, 'Phòng họp', 'ic_gym'), (27, 'Bãi biển', 'ic_beach'), (27, 'Hồ bơi', 'ic_pool'),
(28, 'Kiến trúc Pháp', 'ic_star'), (28, 'Hầm ngầm', 'ic_star'), (28, 'Nhà hàng Pháp', 'ic_restaurant'),
(29, 'Nghệ thuật Opera', 'ic_star'), (29, 'Hồ bơi trong nhà', 'ic_pool'), (29, 'Spa', 'ic_spa'),
(30, 'View 360 độ', 'ic_star'), (30, 'Sky Bar', 'ic_bar'), (30, 'Phòng Gym', 'ic_gym'),
(31, 'Siêu xe đưa đón', 'ic_star'), (31, 'Nội thất Ý', 'ic_star'), (31, 'Spa đẳng cấp', 'ic_spa');

-- Phòng
INSERT INTO rooms (hotel_id, room_number, room_type, price_per_night, status) VALUES
(1, '101', 'Standard', 1500000.00, 'AVAILABLE'), (1, '102', 'Deluxe', 2000000.00, 'AVAILABLE'), (1, '103', 'Suite', 3500000.00, 'AVAILABLE'), (1, '104', 'Family', 4000000.00, 'AVAILABLE'),
(2, 'V1', 'Standard', 2500000.00, 'AVAILABLE'), (2, 'V2', 'Deluxe', 3500000.00, 'AVAILABLE'), (2, 'V3', 'Ocean View', 4500000.00, 'AVAILABLE'),
(3, 'P10', 'Standard', 2000000.00, 'AVAILABLE'), (3, 'P11', 'Suite', 4000000.00, 'AVAILABLE'), (3, 'P12', 'Presidential', 10000000.00, 'AVAILABLE'),
(4, 'I1', 'Standard', 3500000.00, 'AVAILABLE'), (4, 'I2', 'Deluxe', 4500000.00, 'AVAILABLE'),
(5, 'N1', 'Standard', 1800000.00, 'AVAILABLE'), (5, 'N2', 'Bungalow', 3000000.00, 'AVAILABLE'),
(6, 'S1', 'Standard', 2200000.00, 'AVAILABLE'), (6, 'S2', 'Lake View', 3000000.00, 'AVAILABLE'),
(7, 'F1', 'Standard', 3000000.00, 'AVAILABLE'), (7, 'F2', 'Villa 1BR', 5000000.00, 'AVAILABLE'),
(8, 'M1', 'Standard', 3500000.00, 'AVAILABLE'), (8, 'M2', 'Ocean Villa', 7000000.00, 'AVAILABLE'),
(9, 'L1', 'Standard', 1200000.00, 'AVAILABLE'), (9, 'L2', 'Suite', 2500000.00, 'AVAILABLE'),
(10, 'C1', 'Superior', 1300000.00, 'AVAILABLE'), (10, 'C2', 'Deluxe', 1800000.00, 'AVAILABLE'),
(11, 'HC1', 'Classic', 3200000.00, 'AVAILABLE'), (11, 'HC2', 'Executive', 4500000.00, 'AVAILABLE'),
(12, 'IM1', 'Standard', 2100000.00, 'AVAILABLE'), (12, 'IM2', 'Ocean View', 3200000.00, 'AVAILABLE'),
(13, 'VP1', 'Standard', 2800000.00, 'AVAILABLE'), (13, 'VP2', 'Panorama', 4000000.00, 'AVAILABLE'),
(14, 'ME1', 'Deluxe', 3800000.00, 'AVAILABLE'), (14, 'ME2', 'Level Villa', 8000000.00, 'AVAILABLE'),
(15, 'AM1', 'Pavilion', 15000000.00, 'AVAILABLE'), (15, 'AM2', 'Ocean Pool Villa', 30000000.00, 'AVAILABLE'),
(16, 'SP1', 'Classic', 1800000.00, 'AVAILABLE'), (16, 'SP2', 'River View', 2500000.00, 'AVAILABLE'),
(17, 'FLC1', 'Studio', 2200000.00, 'AVAILABLE'), (17, 'FLC2', 'Golf Villa', 6500000.00, 'AVAILABLE'),
(18, 'ANA1', 'Ocean Villa', 8500000.00, 'AVAILABLE'), (18, 'ANA2', 'Pool Villa', 12000000.00, 'AVAILABLE'),
(19, 'ZN1', 'Paddy Villa', 9000000.00, 'AVAILABLE'), (19, 'ZN2', 'Beach Villa', 15000000.00, 'AVAILABLE'),
(20, 'AN1', 'Balcony', 3100000.00, 'AVAILABLE'), (20, 'AN2', 'Pool Villa', 7500000.00, 'AVAILABLE'),
(21, 'CE1', 'Superior', 2600000.00, 'AVAILABLE'), (21, 'CE2', 'Family Suite', 4200000.00, 'AVAILABLE'),
(22, 'AZ1', 'River Room', 4500000.00, 'AVAILABLE'), (22, 'AZ2', 'River Villa', 9500000.00, 'AVAILABLE'),
(23, 'VC1', 'Superior', 2000000.00, 'AVAILABLE'), (23, 'VC2', 'Suite', 3500000.00, 'AVAILABLE'),
(24, 'SS1', 'Hill Top Villa', 16000000.00, 'AVAILABLE'), (24, 'SS2', 'Water Villa', 25000000.00, 'AVAILABLE'),
(25, 'AMN1', 'Ocean Room', 3400000.00, 'AVAILABLE'), (25, 'AMN2', 'Ocean Villa 2BR', 11000000.00, 'AVAILABLE'),
(26, 'NM1', 'Babylon Room', 4200000.00, 'AVAILABLE'), (26, 'NM2', 'Pool Villa 1BR', 7500000.00, 'AVAILABLE'),
(27, 'HY1', 'Ocean View', 3800000.00, 'AVAILABLE'), (27, 'HY2', 'Regency Suite', 6500000.00, 'AVAILABLE'),
(28, 'SM1', 'Premium', 5500000.00, 'AVAILABLE'), (28, 'SM2', 'Grand Premium', 7500000.00, 'AVAILABLE'),
(29, 'CP1', 'Premier', 8000000.00, 'AVAILABLE'), (29, 'CP2', 'Suite', 12000000.00, 'AVAILABLE'),
(30, 'LT1', 'Deluxe', 2800000.00, 'AVAILABLE'), (30, 'LT2', 'Club Floor', 4500000.00, 'AVAILABLE'),
(31, 'RV1', 'Deluxe', 6500000.00, 'AVAILABLE'), (31, 'RV2', 'Panorama Suite', 10500000.00, 'AVAILABLE');

-- Thêm User
INSERT INTO users (username, password, role, full_name, phone) VALUES
('admin', '$2a$10$B2SvAO3vvcoWGp9ATpp0e.a3fT2yJCOUELfnP0tkWPz9cJdEFuUvi', 'ROLE_ADMIN', 'Quản trị viên', '0123456789'),
('phamcongt56@gmail.com', '$2a$10$B2SvAO3vvcoWGp9ATpp0e.a3fT2yJCOUELfnP0tkWPz9cJdEFuUvi', 'ROLE_USER', 'Phạm Công Thành', '0988888888'),
('phuc', '$2a$10$9XN62E2H.1A9ZpU9A3jAmuUvA/h25Q5q1uE1O6y/6J1K/sA3.e9m2', 'ROLE_USER', 'Phúc Nguyễn', '0987654321'),
('khachhang1', '$2a$10$9XN62E2H.1A9ZpU9A3jAmuUvA/h25Q5q1uE1O6y/6J1K/sA3.e9m2', 'ROLE_USER', 'Nguyễn Văn A', '0912345678');

-- Một số đánh giá mẫu đa dạng hơn
INSERT INTO reviews (user_id, hotel_id, rating, comment) VALUES
(2, 1, 5, 'Khách sạn rất đẹp và tiện nghi, nằm ngay trung tâm dễ di chuyển.'),
(3, 1, 4, 'Bữa sáng ngon nhưng đông khách vào cuối tuần.'),
(2, 2, 5, 'Bãi biển tuyệt vời, nhân viên nhiệt tình chu đáo.'),
(3, 3, 4, 'Phòng sạch sẽ, vị trí tốt, giá hơi cao chút xíu.'),
(2, 7, 5, 'Spa miễn phí thực sự làm kỳ nghỉ thư giãn hoàn hảo!'),
(3, 11, 5, 'Kiến trúc quá xuất sắc, check-in mỏi tay luôn.'),
(2, 15, 5, 'Trải nghiệm đỉnh cao của nghỉ dưỡng. Không còn gì để chê.'),
(3, 24, 5, 'Biệt thự Six Senses trên vách đá như một kiệt tác, quá xuất sắc!'),
(2, 31, 5, 'Cảm giác như ông hoàng bà chúa khi ở The Reverie. Sang trọng tột bậc.'),
(3, 18, 5, 'Nhân viên chu đáo, cảnh quan thiên nhiên bao quanh rất yên bình.'),
(2, 29, 5, 'Khách sạn Capella đẹp từng góc nhỏ, phòng ốc bài trí đậm chất nghệ thuật.');
