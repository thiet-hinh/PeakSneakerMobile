package com.example.shoeshop.dto.respone

data class UserProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String
)