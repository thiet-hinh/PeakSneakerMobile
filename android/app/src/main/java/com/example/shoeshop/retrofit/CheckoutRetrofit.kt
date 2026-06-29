package com.example.shoeshop.retrofit

import com.example.shoeshop.api.CheckoutApi

object CheckoutRetrofit {
    val api : CheckoutApi by lazy {
        BaseRetrofit.retrofit.create(CheckoutApi::class.java)
    }
}