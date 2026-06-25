package com.example.shoeshop.model

data class Cart(
    val name: String,
    val size: String,
    val price: Long,
    var quantity: Int,
    var isChecked: Boolean = true
)