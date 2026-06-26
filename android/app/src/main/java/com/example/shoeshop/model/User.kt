package com.example.shoeshop.model

import java.time.LocalDateTime

data class User(
    val id: Int,
    val firebaseUid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val role: String,
    val isActive: Boolean,
    val createdAt: String
)