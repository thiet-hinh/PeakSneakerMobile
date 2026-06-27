package com.example.shoeshop.model

data class Address(
    val id: Int,
    val provinceId: Int,
    val provinceName: String,
    val districtId: Int,
    val districtName: String,
    val wardId: Int,
    val wardName: String,
    val streetDetail: String,
    val userName: String,
    val phone: String
)