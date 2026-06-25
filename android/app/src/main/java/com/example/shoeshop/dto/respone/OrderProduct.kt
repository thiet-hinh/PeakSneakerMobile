package com.example.shoeshop.dto.respone

data class OrderProduct(
    val imageUrl: String,
    val brand: String,
    val productName: String,
    val size: String,
    val color: String,
    val quantity: Int,
    val price: Double
)