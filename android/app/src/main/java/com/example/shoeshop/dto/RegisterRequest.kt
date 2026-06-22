package com.example.shoeshop.dto

data class RegisterRequest(
    val firebaseUid: String,
    val fullName: String,
    val email: String,
    val phone: String
)