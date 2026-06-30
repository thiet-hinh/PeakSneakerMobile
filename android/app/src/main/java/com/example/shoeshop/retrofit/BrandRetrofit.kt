package com.example.shoeshop.retrofit

import com.example.shoeshop.api.BrandApi

object BrandRetrofit {
    val api: BrandApi by lazy {
        BaseRetrofit.retrofit.create(BrandApi::class.java)
    }
}