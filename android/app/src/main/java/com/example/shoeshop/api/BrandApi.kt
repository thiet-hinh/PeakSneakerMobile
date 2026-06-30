package com.example.shoeshop.api

import com.example.shoeshop.model.Brand
import retrofit2.Call
import retrofit2.http.GET

interface BrandApi {
    @GET("api/brand")
    fun getBrands(): Call<List<Brand>>
}