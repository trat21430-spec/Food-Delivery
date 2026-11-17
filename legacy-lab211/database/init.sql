-- ==============================================================================
-- FOOD DELIVERY PLATFORM (FDS) - MASTER PLAN INITIAL SEED DATA
-- ==============================================================================

-- 1. Roles
INSERT INTO roles (role_id, role_name, description) VALUES
('ROLE_CUSTOMER', 'Khách Hàng', 'Người dùng đặt món trực tuyến'),
('ROLE_MERCHANT', 'Nhà Hàng', 'Chủ quán và quản lý bếp'),
('ROLE_DRIVER', 'Tài Xế', 'Shipper giao đồ ăn'),
('ROLE_DISPATCHER', 'Điều Phối Viên', 'Giám sát điều phối xe trung tâm'),
('ROLE_ADMIN', 'Quản Trị Viên', 'Quản trị hệ thống và tài chính sàn')
ON CONFLICT (role_id) DO NOTHING;

-- 2. Food Categories
INSERT INTO food_categories (category_id, name, slug, display_order) VALUES
('CAT_BANH_XEO', 'Bánh xèo tôm nhảy', 'banh-xeo', 1),
('CAT_BUN_CA', 'Bún chả cá', 'bun-cha-ca', 2),
('CAT_COM_GA', 'Cơm gà Quy Nhơn', 'com-ga', 3),
('CAT_HAI_SAN', 'Hải sản tươi sống', 'hai-san', 4),
('CAT_DAC_SAN', 'Đặc sản Bình Định', 'dac-san', 5)
ON CONFLICT (category_id) DO NOTHING;

-- 3. Initial Admin User
INSERT INTO users (user_id, username, password_hash, email, phone, full_name, status) VALUES
('U_ADMIN_01', 'admin', '$FDS$V1$d41d8cd98f00b204e9800998ecf8427e$e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'admin@fooddelivery.vn', '0905111222', 'Quản Trị Viên Sàn FDS', 'ACTIVE'),
('U_DISP_01', 'dispatcher', '$FDS$V1$d41d8cd98f00b204e9800998ecf8427e$e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'dispatch@fooddelivery.vn', '0905333444', 'Điều Phối Viên Quy Nhơn', 'ACTIVE')
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id) VALUES
('U_ADMIN_01', 'ROLE_ADMIN'),
('U_DISP_01', 'ROLE_DISPATCHER')
ON CONFLICT DO NOTHING;
