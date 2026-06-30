package com.example.shoeshop.model

import java.io.Serializable

data class Cart(
    val cartItemId: Int,       // 💡 THÊM TRƯỜNG NÀY: ID của item trong giỏ hàng dưới DB
    val productId: Int,        // ID của sản phẩm
    val variantId: Int,        // ID của biến thể (Màu/Size)
    val name: String,
    val size: String,
    val color: String,         // Có thể thêm màu nếu muốn hiển thị thêm
    val price: Double,
    var quantity: Int,
    val stockQuantity: Int,
    val image: String?,// 💡 THÊM TRƯỜNG NÀY: Để chặn không cho bấm nút "+" quá số lượng trong kho
    var isChecked: Boolean = false
) : Serializable