package com.example.shoeshop.dto.respone

data class PlaceOrderResponse(
    val orderCode: String?,
    val estimatedDeliveryDate: String?,
    val paymentMethod: String?,
    val message: String?
)
