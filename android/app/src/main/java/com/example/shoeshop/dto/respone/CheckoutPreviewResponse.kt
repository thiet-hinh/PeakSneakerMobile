package com.example.shoeshop.dto.respone

data class CheckoutPreviewResponse(
    val shippingAddress: ShippingAddressCheckout?,
    val items: List<CheckoutCartItem>
)
