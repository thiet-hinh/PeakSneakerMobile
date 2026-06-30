package com.example.shoeshop.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("productName") val productName: String?,
    @SerializedName("name") val rawName: String?,
    @SerializedName("brandName") val brandNameDto: String?,
    @SerializedName("brand") val brandEntity: BrandEntity?,
    @SerializedName("basePrice") val basePrice: Double,
    @SerializedName("price") val price: Double,
    @SerializedName("discountRate") val discountRate: Double,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("averageRating") val averageRating: Double?,
    @SerializedName("soldQuantity") val soldQuantity: Long?,
    @SerializedName("variants") val variants: List<ProductVariant>? = listOf()
) {
    val name: String
        get() = productName ?: rawName ?: "Giày thời trang"

    val brandName: String
        get() = brandNameDto ?: brandEntity?.name ?: "Shoe Store"
    val rating: String
        get() = String.format("%.1f", averageRating ?: 5.0)

    val sold: String
        get() = (soldQuantity ?: 0L).toString()

    val discount: Double
        get() = if (discountRate <= 1.0) discountRate * 100 else discountRate
}


data class BrandEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)