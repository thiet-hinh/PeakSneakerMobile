package com.example.shoeshop.dto.respone

data class AddressItem(
    val id: String,
    val name: String
) {
    override fun toString(): String = name
}