/*
 Navicat Premium Dump SQL

 Source Server         : shoestore
 Source Server Type    : MySQL
 Source Server Version : 80410 (8.4.10)
 Source Host           : localhost:3306
 Source Schema         : shoe_store

 Target Server Type    : MySQL
 Target Server Version : 80410 (8.4.10)
 File Encoding         : 65001

 Date: 27/06/2026 20:19:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `district_id` int NULL DEFAULT NULL,
                            `district_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `province_id` int NULL DEFAULT NULL,
                            `province_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `ward_id` int NULL DEFAULT NULL,
                            `ward_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `street_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `is_default` tinyint(1) NULL DEFAULT 1,
                            `user_id` int NOT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `fk_address_user`(`user_id` ASC) USING BTREE,
                            CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of address
-- ----------------------------

-- ----------------------------
-- Table structure for brand
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand`  (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `image_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                          `description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of brand
-- ----------------------------
INSERT INTO `brand` VALUES (1, 'Nike', NULL, 'Thương hiệu đồ thể thao hàng đầu thế gới với slogan Just Do It');
INSERT INTO `brand` VALUES (2, 'Adidas', NULL, 'Nhà sản xuất dụng cụ thể thao đến từ Đức');
INSERT INTO `brand` VALUES (3, 'Vans', NULL, 'Thương hiệu giày trượt ván và thời trang đường phố');
INSERT INTO `brand` VALUES (4, 'Converse', NULL, 'Biểu tượng văn hóa đại chúng với dùng giày Chuck Taylor');
INSERT INTO `brand` VALUES (5, 'Puma', NULL, 'Thương hiệu thời gian thể thao với logo chú báo sư tử đặc trưng');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `user_id` int NOT NULL,
                         PRIMARY KEY (`id`) USING BTREE,
                         UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
                         CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (1, 1);
INSERT INTO `cart` VALUES (2, 2);

-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item`  (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `quantity` int NOT NULL DEFAULT 1,
                              `cart_id` int NOT NULL,
                              `product_variant_id` int NOT NULL,
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `fk_cart_item_cart`(`cart_id` ASC) USING BTREE,
                              INDEX `fk_cart_item_variant`(`product_variant_id` ASC) USING BTREE,
                              CONSTRAINT `fk_cart_item_cart` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                              CONSTRAINT `fk_cart_item_variant` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart_item
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                             `slug` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                             `description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
                             `parent_id` int NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `slug`(`slug` ASC) USING BTREE,
                             INDEX `fk_category_parent`(`parent_id` ASC) USING BTREE,
                             CONSTRAINT `fk_category_parent` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for image
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image`  (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `image_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `is_primary` tinyint(1) NOT NULL DEFAULT 0,
                          `product_id` int NULL DEFAULT NULL,
                          `product_variant_id` int NULL DEFAULT NULL,
                          PRIMARY KEY (`id`) USING BTREE,
                          INDEX `fk_image_product`(`product_id` ASC) USING BTREE,
                          INDEX `fk_image_variant`(`product_variant_id` ASC) USING BTREE,
                          CONSTRAINT `fk_image_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                          CONSTRAINT `fk_image_variant` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of image
-- ----------------------------
INSERT INTO `image` VALUES (1, 'af1_white.jpg', 1, 1, 1);
INSERT INTO `image` VALUES (2, 'af1_white.jpg', 0, 1, 2);
INSERT INTO `image` VALUES (3, 'af1_black.jpg', 0, 1, 3);
INSERT INTO `image` VALUES (4, 'pegasus_black_white.jpg', 1, 2, 4);
INSERT INTO `image` VALUES (5, 'pegasus_black_white.jpg', 0, 2, 5);
INSERT INTO `image` VALUES (6, 'pegasus_blue.jpg', 0, 2, 6);
INSERT INTO `image` VALUES (7, 'stansmith_white_green.jpg', 1, 3, 7);
INSERT INTO `image` VALUES (8, 'stansmith_white_green.jpg', 0, 3, 8);
INSERT INTO `image` VALUES (9, 'stansmith_white_navy.jpg', 0, 3, 9);
INSERT INTO `image` VALUES (10, 'ultraboost_black.jpg', 1, 4, 10);
INSERT INTO `image` VALUES (11, 'ultraboost_black.jpg', 0, 4, 11);
INSERT INTO `image` VALUES (12, 'ultraboost_white.jpg', 0, 4, 12);
INSERT INTO `image` VALUES (13, 'oldskool_black_white.jpg', 1, 5, 13);
INSERT INTO `image` VALUES (14, 'oldskool_black_white.jpg', 0, 5, 14);
INSERT INTO `image` VALUES (15, 'oldskool_navy.jpg', 0, 5, 15);
INSERT INTO `image` VALUES (16, 'slipon_checkerboard.jpg', 1, 6, 16);
INSERT INTO `image` VALUES (17, 'slipon_checkerboard.jpg', 0, 6, 17);
INSERT INTO `image` VALUES (18, 'slipon_red_check.jpg', 0, 6, 18);
INSERT INTO `image` VALUES (19, 'chuck_black.jpg', 1, 7, 19);
INSERT INTO `image` VALUES (20, 'chuck_black.jpg', 0, 7, 20);
INSERT INTO `image` VALUES (21, 'chuck_white.jpg', 0, 7, 21);
INSERT INTO `image` VALUES (22, 'runstar_black.jpg', 1, 8, 22);
INSERT INTO `image` VALUES (23, 'runstar_black.jpg', 0, 8, 23);
INSERT INTO `image` VALUES (24, 'runstar_white.jpg', 0, 8, 24);
INSERT INTO `image` VALUES (25, 'suede_black_white.jpg', 1, 9, 25);
INSERT INTO `image` VALUES (26, 'suede_black_white.jpg', 0, 9, 26);
INSERT INTO `image` VALUES (27, 'suede_red_white.jpg', 0, 9, 27);
INSERT INTO `image` VALUES (28, 'rsx_multicolor.jpg', 1, 10, 28);
INSERT INTO `image` VALUES (29, 'rsx_multicolor.jpg', 0, 10, 29);
INSERT INTO `image` VALUES (30, 'rsx_black_red.jpg', 0, 10, 30);

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                               `variant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                               `quantity` int NOT NULL,
                               `unit_price` decimal(18, 2) NOT NULL,
                               `subtotal` decimal(18, 2) NOT NULL,
                               `order_id` int NOT NULL,
                               `product_variant_id` int NULL DEFAULT NULL,
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `fk_order_item_order`(`order_id` ASC) USING BTREE,
                               INDEX `fk_order_item_variant`(`product_variant_id` ASC) USING BTREE,
                               CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                               CONSTRAINT `fk_order_item_variant` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_item
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `subtotal_amount` decimal(18, 2) NOT NULL,
                           `discount_amount` decimal(18, 2) NULL DEFAULT 0.00,
                           `shipping_fee` decimal(18, 2) NULL DEFAULT 0.00,
                           `final_amount` decimal(18, 2) NOT NULL,
                           `note` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
                           `shipping_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                           `shipping_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                           `shipping_province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                           `shipping_district` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                           `shipping_ward` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                           `shipping_street` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                           `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                           `user_id` int NOT NULL,
                           `voucher_id` int NULL DEFAULT NULL,
                           `order_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `expected_date` datetime NULL DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `fk_order_user`(`user_id` ASC) USING BTREE,
                           INDEX `fk_order_voucher`(`voucher_id` ASC) USING BTREE,
                           CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                           CONSTRAINT `FKrx5vk9ur428660yp19hw98nr2` FOREIGN KEY (`voucher_id`) REFERENCES `voucher` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `amount` decimal(18, 2) NOT NULL,
                            `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `paid_at` datetime NULL DEFAULT NULL,
                            `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `payment_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `transaction_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `order_id` int NOT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE INDEX `order_id`(`order_id` ASC) USING BTREE,
                            CONSTRAINT `fk_payment_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of payment
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                            `description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
                            `attribute` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
                            `base_price` decimal(18, 2) NOT NULL,
                            `discount_rate` decimal(5, 2) NULL DEFAULT 0.00,
                            `gender` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `product_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
                            `is_featured` tinyint(1) NOT NULL DEFAULT 0,
                            `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                            `brand_id` int NULL DEFAULT NULL,
                            `category_id` int NULL DEFAULT NULL,
                            `price` decimal(18, 2) NOT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `fk_product_brand`(`brand_id` ASC) USING BTREE,
                            INDEX `fk_product_category`(`category_id` ASC) USING BTREE,
                            CONSTRAINT `fk_product_brand` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                            CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 'Nike Air Force 1 07', 'Giày sneaker cổ điển form dáng huyền thoại', '{\"chất_liệu\": \"Da tổng hợp cao cấp mềm mại, bền đẹp và dễ vệ sinh\", \"phong_cách\": \"Phong cách thường ngày năng động, phù hợp nhiều hoàn cảnh sử dụng\"}', 2900000.00, 0.00, 'UNISEX', NULL, 0, 1, '2026-06-25 02:30:36', 1, NULL, 2900000.00);
INSERT INTO `product` VALUES (2, 'Nike Air Zoom Pegasus 40', 'Giày chạy bộ êm ái, thoáng khí.', '{\"chất_liệu\": \"Vải lưới thoáng khí nhẹ nhàng, hỗ trợ lưu thông không khí tốt\", \"phong_cách\": \"Phong cách thể thao chạy bộ, tối ưu cho vận động và tập luyện\"}', 3500000.00, 0.10, 'MEN', NULL, 0, 1, '2026-06-25 02:30:36', 1, NULL, 3150000.00);
INSERT INTO `product` VALUES (3, 'Adidas Stan Smith', 'Giày tennis phong cách tối giản.', '{\"chất_liệu\": \"Da thuần chay thân thiện với môi trường, mềm mại và bền bỉ\", \"phong_cách\": \"Phong cách thường ngày hiện đại, dễ phối với nhiều loại trang phục\"}', 2600000.00, 0.00, 'UNISEX', NULL, 0, 1, '2026-06-25 02:30:36', 2, NULL, 2600000.00);
INSERT INTO `product` VALUES (4, 'Adidas Ultraboost Light', 'Giày chạy bộ công nghệ đệm Boost nhẹ nhất.', '{\"chất_liệu\": \"Vải dệt Primeknit co giãn linh hoạt, ôm chân và thoáng khí\", \"phong_cách\": \"Phong cách chạy bộ chuyên dụng, mang lại cảm giác nhẹ và thoải mái\"}', 5000000.00, 0.20, 'WOMEN', NULL, 0, 0, '2026-06-25 02:30:36', 2, NULL, 4000000.00);
INSERT INTO `product` VALUES (5, 'Vans Old Skool', 'Giày trượt ván mũi lộn, sọc jazz kinh điển.', '{\"chất_liệu\": \"Sự kết hợp giữa vải canvas và da lộn, tạo độ bền và tính thẩm mỹ cao\", \"phong_cách\": \"Phong cách trượt ván cá tính, phù hợp với giới trẻ năng động\"}', 1900000.00, 0.10, 'UNISEX', NULL, 0, 1, '2026-06-25 02:30:36', 3, NULL, 1900000.00);
INSERT INTO `product` VALUES (6, 'Vans Slip-On Checkerboard', 'Giày lười họa tiết caro.', '{\"chất_liệu\": \"Vải canvas chắc chắn, thoáng mát và dễ bảo quản\", \"phong_cách\": \"Phong cách thường ngày trẻ trung, phù hợp cho các hoạt động hằng ngày\"}', 1700000.00, 0.10, 'UNISEX', NULL, 0, 0, '2026-06-25 02:30:36', 3, NULL, 1530000.00);
INSERT INTO `product` VALUES (7, 'Converse Chuck Taylor All Star', 'Giày cổ cao vải canvas truyền thống.', '{\"chất_liệu\": \"Vải canvas bền bỉ với khả năng chống mài mòn tốt\", \"phong_cách\": \"Phong cách thường ngày đơn giản, dễ dàng phối hợp với nhiều trang phục\"}', 1500000.00, 0.00, 'UNISEX', NULL, 0, 1, '2026-06-25 02:30:36', 4, NULL, 1500000.00);
INSERT INTO `product` VALUES (8, 'Converse Run Star Hike', 'Giày đế độn phong cách cá tính.', '{\"chất_liệu\": \"Vải canvas chất lượng cao, mang lại cảm giác thoải mái khi sử dụng\", \"phong_cách\": \"Phong cách đế dày thời trang, tạo điểm nhấn nổi bật và cá tính\"}', 2800000.00, 0.10, 'WOMEN', NULL, 0, 1, '2026-06-25 02:30:36', 4, NULL, 2520000.00);
INSERT INTO `product` VALUES (9, 'Puma Suede Classic', 'Giày da lộn phong cách retro.', '{\"chất_liệu\": \"Da lộn mềm mại với bề mặt sang trọng và tinh tế\", \"phong_cách\": \"Phong cách thường ngày thanh lịch, phù hợp cho nhiều dịp khác nhau\"}', 2200000.00, 0.00, 'MEN', NULL, 0, 0, '2026-06-25 02:30:36', 5, NULL, 2200000.00);
INSERT INTO `product` VALUES (10, 'Puma RS-X Toys', 'Giày sneaker phom dáng chunky hầm hố.', '{\"chất_liệu\": \"Kết hợp giữa vải lưới thoáng khí và da bền chắc, tăng độ ổn định khi mang\", \"phong_cách\": \"Phong cách đế dày hiện đại, mang lại vẻ ngoài nổi bật và thời thượng\"}', 3000000.00, 0.30, 'UNISEX', NULL, 0, 1, '2026-06-25 02:30:36', 5, NULL, 2100000.00);

-- ----------------------------
-- Table structure for product_variant
-- ----------------------------
DROP TABLE IF EXISTS `product_variant`;
CREATE TABLE `product_variant`  (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `color` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                    `size` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                    `stock_quantity` int NOT NULL DEFAULT 0,
                                    `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                                    `product_id` int NOT NULL,
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `fk_variant_product`(`product_id` ASC) USING BTREE,
                                    CONSTRAINT `fk_variant_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_variant
-- ----------------------------
INSERT INTO `product_variant` VALUES (1, 'White', '40', 50, '2026-06-25 02:33:58', 1);
INSERT INTO `product_variant` VALUES (2, 'White', '41', 45, '2026-06-25 02:33:58', 1);
INSERT INTO `product_variant` VALUES (3, 'Black', '40', 20, '2026-06-25 02:33:58', 1);
INSERT INTO `product_variant` VALUES (4, 'Black/White', '42', 30, '2026-06-25 02:33:58', 2);
INSERT INTO `product_variant` VALUES (5, 'Black/White', '43', 25, '2026-06-25 02:33:58', 2);
INSERT INTO `product_variant` VALUES (6, 'Blue', '42', 15, '2026-06-25 02:33:58', 2);
INSERT INTO `product_variant` VALUES (7, 'White/Green', '39', 40, '2026-06-25 02:33:58', 3);
INSERT INTO `product_variant` VALUES (8, 'White/Green', '40', 60, '2026-06-25 02:33:58', 3);
INSERT INTO `product_variant` VALUES (9, 'White/Navy', '39', 20, '2026-06-25 02:33:58', 3);
INSERT INTO `product_variant` VALUES (10, 'Core Black', '37', 15, '2026-06-25 02:33:58', 4);
INSERT INTO `product_variant` VALUES (11, 'Core Black', '38', 20, '2026-06-25 02:33:58', 4);
INSERT INTO `product_variant` VALUES (12, 'Cloud White', '38', 10, '2026-06-25 02:33:58', 4);
INSERT INTO `product_variant` VALUES (13, 'Black/White', '40', 100, '2026-06-25 02:33:58', 5);
INSERT INTO `product_variant` VALUES (14, 'Black/White', '41', 120, '2026-06-25 02:33:58', 5);
INSERT INTO `product_variant` VALUES (15, 'Navy', '40', 40, '2026-06-25 02:33:58', 5);
INSERT INTO `product_variant` VALUES (16, 'Black/White Check', '39', 55, '2026-06-25 02:33:58', 6);
INSERT INTO `product_variant` VALUES (17, 'Black/White Check', '40', 65, '2026-06-25 02:33:58', 6);
INSERT INTO `product_variant` VALUES (18, 'Red Check', '39', 15, '2026-06-25 02:33:58', 6);
INSERT INTO `product_variant` VALUES (19, 'Black', '41', 80, '2026-06-25 02:33:58', 7);
INSERT INTO `product_variant` VALUES (20, 'Black', '42', 75, '2026-06-25 02:33:58', 7);
INSERT INTO `product_variant` VALUES (21, 'Optical White', '41', 50, '2026-06-25 02:33:58', 7);
INSERT INTO `product_variant` VALUES (22, 'Black', '36', 30, '2026-06-25 02:33:58', 8);
INSERT INTO `product_variant` VALUES (23, 'Black', '37', 45, '2026-06-25 02:33:58', 8);
INSERT INTO `product_variant` VALUES (24, 'White', '37', 25, '2026-06-25 02:33:58', 8);
INSERT INTO `product_variant` VALUES (25, 'Black/White', '42', 20, '2026-06-25 02:33:58', 9);
INSERT INTO `product_variant` VALUES (26, 'Black/White', '43', 15, '2026-06-25 02:33:58', 9);
INSERT INTO `product_variant` VALUES (27, 'Red/White', '42', 10, '2026-06-25 02:33:58', 9);
INSERT INTO `product_variant` VALUES (28, 'Multicolor', '40', 35, '2026-06-25 02:33:58', 10);
INSERT INTO `product_variant` VALUES (29, 'Multicolor', '41', 40, '2026-06-25 02:33:58', 10);
INSERT INTO `product_variant` VALUES (30, 'Black/Red', '41', 20, '2026-06-25 02:33:58', 10);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `comment` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
                           `star` int NULL DEFAULT NULL,
                           `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                           `user_id` int NOT NULL,
                           `product_id` int NOT NULL,
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE INDEX `uk_review_user_product`(`user_id` ASC, `product_id` ASC) USING BTREE,
                           INDEX `fk_review_product`(`product_id` ASC) USING BTREE,
                           CONSTRAINT `fk_review_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                           CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                           CONSTRAINT `chk_review_star` CHECK (`star` between 1 and 5)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of review
-- ----------------------------

-- ----------------------------
-- Table structure for user_voucher
-- ----------------------------
DROP TABLE IF EXISTS `user_voucher`;
CREATE TABLE `user_voucher`  (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `user_id` int NOT NULL,
                                 `voucher_id` int NOT NULL,
                                 `order_id` int NULL DEFAULT NULL,
                                 `is_used` tinyint(1) NOT NULL DEFAULT 0,
                                 `used_at` datetime NULL DEFAULT NULL,
                                 `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `fk_user_voucher_user`(`user_id` ASC) USING BTREE,
                                 INDEX `fk_user_voucher_voucher`(`voucher_id` ASC) USING BTREE,
                                 INDEX `fk_user_voucher_order`(`order_id` ASC) USING BTREE,
                                 CONSTRAINT `fk_user_voucher_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                 CONSTRAINT `fk_user_voucher_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                 CONSTRAINT `fk_user_voucher_voucher` FOREIGN KEY (`voucher_id`) REFERENCES `voucher` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_voucher
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `firebase_uid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                          `role` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `avatar` tinyblob NULL,
                          `is_active` tinyint(1) NOT NULL DEFAULT 1,
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `firebase_uid`(`firebase_uid` ASC) USING BTREE,
                          UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'hinh', 'nguyen', '06rh7Saj9edYtF1G0VxZEDxjjHd2', 'thiethinh789@gmail.com', '0866501452', 'USER', '2026-06-27 00:29:30', NULL, 1);
INSERT INTO `users` VALUES (2, 'hinh', 'thiet', 'grRpWND2GKe3lMnMQ1p3YmgMYr33', 'hicybaby00@gmail.com', '0866501452', 'USER', '2026-06-27 01:02:13', NULL, 1);

-- ----------------------------
-- Table structure for voucher
-- ----------------------------
DROP TABLE IF EXISTS `voucher`;
CREATE TABLE `voucher`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                            `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `discount_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                            `discount_value` decimal(18, 2) NOT NULL,
                            `expire_at` datetime NOT NULL,
                            `is_active` tinyint(1) NOT NULL DEFAULT 1,
                            `max_discount_amount` decimal(18, 2) NULL DEFAULT NULL,
                            `min_order_amount` decimal(18, 2) NULL DEFAULT NULL,
                            `start_at` datetime NOT NULL,
                            `usage_limit` int NULL DEFAULT 0,
                            `used_count` int NULL DEFAULT 0,
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE INDEX `code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of voucher
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
