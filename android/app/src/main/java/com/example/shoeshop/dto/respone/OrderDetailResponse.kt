package com.example.shoeshop.dto.respone

data class OrderDetailResponse(
    val id: Int,
    val subtotal: Double,
    val discountAmount : Double,
    val shippingFee: Double,
    val finalAmount: Double,
    val shippingName: String,
    val shippingPhone: String,
    val shippingAddress: String,
    val status: String,
    val orderDate: String,
    val items: List<OrderItemDetailResponse>
)