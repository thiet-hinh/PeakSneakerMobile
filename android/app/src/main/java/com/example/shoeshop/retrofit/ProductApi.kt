package com.example.shoeshop.api

import com.example.shoeshop.dto.respone.CartItemResponse
import com.example.shoeshop.model.CartRequest
import com.example.shoeshop.model.Product
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ProductApi {
    @GET("api/product")
    fun getProducts(
        @Query("keyword") keyword: String? = null,
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

    @POST("api/cart/add")
    fun addToCart(@Body request: CartRequest): Call<ResponseBody>

    @GET("api/cart/{userId}")
    fun getCart(@Path("userId") userId: Int): Call<List<CartItemResponse>>

    @PATCH("api/cart/items/{itemId}")
    fun updateQuantity(
        @Path("itemId") itemId: Int,
        @Query("quantity") quantity: Int
    ): Call<CartItemResponse>

    @DELETE("api/cart/items/{itemId}")
    fun removeCartItem(@Path("itemId") itemId: Int): Call<ResponseBody>

    @DELETE("api/cart/user/{userId}")
    fun clearCart(@Path("userId") userId: Int): Call<ResponseBody>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/"

        val productApi: ProductApi by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductApi::class.java)
        }
    }
}