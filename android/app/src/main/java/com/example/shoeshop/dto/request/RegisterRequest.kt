package com.example.shoeshop.dto.request

data class RegisterRequest(
    val firebaseUid: String,
    val fullName: String,
    val email: String,
    val phone: String
)