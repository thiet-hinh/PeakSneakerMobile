package com.example.shoeshop.dto.respone

data class ShippingAddressCheckout(
    val id: Int,
    val receiverName: String,
    val phone: String,
    val address: String
)
