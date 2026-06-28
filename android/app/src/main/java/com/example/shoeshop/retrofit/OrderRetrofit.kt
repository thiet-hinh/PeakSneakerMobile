package com.example.shoeshop.retrofit

import com.example.shoeshop.api.OrderApi

object OrderRetrofit {
    val api: OrderApi by lazy {
        BaseRetrofit.retrofit.create(OrderApi::class.java)
    }
}