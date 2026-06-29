package com.example.shoeshop.dto.respone

data class CheckoutCartItem(
    val cartItemId: Int,
    val variantId: Int,
    val imageUrl: String,
    val brand: String,
    val productName: String,
    val color: String,
    val size: String,
    val quantity: Int,
    val price: Double
)
