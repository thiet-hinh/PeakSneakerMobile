package com.example.shoeshop.retrofit

import com.example.shoeshop.api.ProductApi

object ProductRetrofit {
    val api: ProductApi by lazy {
        BaseRetrofit.retrofit.create(ProductApi::class.java)
    }
}