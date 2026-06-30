package com.example.shoeshop.model

import com.google.gson.annotations.SerializedName

data class ProductVariant(
    @SerializedName("id") val id: Int,
    @SerializedName("color") val color: String?,
    @SerializedName("size") val size: String?,
    @SerializedName("stockQuantity") val stockQuantity: Int
)