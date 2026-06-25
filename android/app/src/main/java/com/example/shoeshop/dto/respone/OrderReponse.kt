package com.example.shoeshop.dto.respone

import com.example.shoeshop.enums.OrderStatus

data class OrderReponse(
    val orderCode: String,
    val orderDate: String,
    val price: String,
    val status: OrderStatus
)