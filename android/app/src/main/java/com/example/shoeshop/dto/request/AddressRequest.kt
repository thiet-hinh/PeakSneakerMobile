package com.example.shoeshop.dto.request

data class AddressRequest(
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