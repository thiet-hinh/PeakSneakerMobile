package com.example.shoeshop.enums

enum class OrderStatus(val status: String) {
    PROCESSING("Đang xử lý"),
    SHIPPING("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Đã hủy")
}