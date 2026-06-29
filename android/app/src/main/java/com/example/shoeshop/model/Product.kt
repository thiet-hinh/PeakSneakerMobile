package com.example.shoeshop.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("productName") val productName: String?, // Nhận từ DTO
    @SerializedName("name") val rawName: String?,            // Nhận từ Entity gốc
    @SerializedName("brandName") val brandNameDto: String?,  // Nhận từ DTO
    @SerializedName("brand") val brandEntity: BrandEntity?,  // Nhận từ Entity gốc
    @SerializedName("basePrice") val basePrice: Double,
    @SerializedName("price") val price: Double,
    @SerializedName("discountRate") val discountRate: Double,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("averageRating") val averageRating: Double?,
    @SerializedName("soldQuantity") val soldQuantity: Long?
) {
    // Các thuộc tính thông minh bổ trợ cho Adapter hiển thị mà không cần sửa code cũ
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

// Lớp bổ trợ đọc thực thể thương hiệu lồng trong Entity Product
data class BrandEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)