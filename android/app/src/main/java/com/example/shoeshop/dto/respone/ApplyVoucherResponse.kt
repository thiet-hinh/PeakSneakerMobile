package com.example.shoeshop.dto.respone

data class ApplyVoucherResponse(
    val success: Boolean,
    val code : String?,
    val discountAmount: Double,
    val message: String
)
