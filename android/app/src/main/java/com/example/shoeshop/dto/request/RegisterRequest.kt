package com.example.shoeshop.dto.request

data class RegisterRequest(
    val firebaseUid: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val phone: String
)