package com.example.shoeshop.model

data class CartRequest(
    val userId: Int,
    val productId: Int,
    val color: String,
    val size: String,
    val quantity: Int = 1
)