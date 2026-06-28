package com.example.shoeshop.dto.respone

data class OrderItemDetailResponse(
    val productName: String,
    val brand: String,
    val color: String,
    val size: String,
    val quantity: Int,
    val price: Double,
    val imageUrl: String?
)