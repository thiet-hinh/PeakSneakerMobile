package com.example.shoeshop.dto.request

data class ApplyVoucherRequest(
    val userId: Int,
    val orderAmount: Double,
    val voucherCode: String
)
