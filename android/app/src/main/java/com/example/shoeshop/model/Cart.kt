package com.example.shoeshop.model

import java.io.Serializable

data class Cart(
    val cartItemId: Int,
    val productId: Int,
    val variantId: Int,
    val name: String,
    val size: String,
    val color: String,
    val price: Double,
    var quantity: Int,
    val stockQuantity: Int,
    val image: String?,
    var isChecked: Boolean = false
) : Serializable