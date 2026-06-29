package com.example.shoeshop.dto.respone

import java.math.BigDecimal

data class OrderResponse(
    val orderCode: String,
    val orderDate: String,
    val price: BigDecimal,
    val status: String
)
