package com.example.shoeshop.model

data class Product(
    val id: Int,
    val brandName: String,
    val name: String,
    val rating: String,
    val sold: String,
    val price: String,
    val imageResId: Int,
    val originalPrice: String = "",
    val discount: String = ""
)