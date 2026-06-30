package com.example.shoeshop.dto.respone

import java.io.Serializable

data class CartItemResponse(
    val cartItemId: Int,
    val productId: Int,
    val variantId: Int,
    val productName: String,
    val imageUrl: String,
    val color: String,
    val size: String,
    val price: Double,
    var quantity: Int,
    val stockQuantity: Int,
    var isChecked: Boolean = false
) : Serializable