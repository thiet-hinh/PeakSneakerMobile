package com.example.shoeshop.retrofit

import com.example.shoeshop.api.AddressApi

object AddressRetrofit {
    val api : AddressApi by lazy {
        BaseRetrofit.retrofit.create(AddressApi:: class.java)
    }
}