package com.example.shoeshop.dto.respone

import com.example.shoeshop.enums.OrderStatus

data class OrderReponse(
    val image: String,
    val orderCode: String,
    val productName: String,
    val size: String,
    val color: String,
    val orderDate: String,
    val price: String,
    val status: OrderStatus
)