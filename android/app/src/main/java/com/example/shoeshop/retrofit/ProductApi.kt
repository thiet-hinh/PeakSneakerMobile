package com.example.shoeshop.api

import com.example.shoeshop.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path // Thêm import Path
import retrofit2.http.Query

interface ProductApi {
    @GET("api/product")
    fun getProducts(
        @Query("keyword") keyword: String? = null, // Thêm dòng này
        @Query("gender") gender: String? = null,
        @Query("brandId") brandId: Int? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("size") size: String? = null
    ): Call<List<Product>>

    @GET("api/product/featured")
    fun getFeaturedProducts(): Call<List<Product>>

    @GET("api/product/{id}")
    fun getProductById(@Path("id") id: Int): Call<Product>
}