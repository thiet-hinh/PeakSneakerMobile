package com.example.shoeshop.dto.request

import com.example.shoeshop.enums.DeliveryMethod
import com.example.shoeshop.enums.PaymentMethod
data class PlaceOrderRequest(
    val userId: Int,
    val selectedCartItemIds: List<Int>,
    val voucherCode: String,
    val paymentMethod: PaymentMethod,
    val deliveryMethod: DeliveryMethod
)
