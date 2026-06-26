package com.example.shoeshop.retrofit

import com.example.shoeshop.api.UserApi

object UserRetrofit {
    val api: UserApi by lazy {
        BaseRetrofit.retrofit.create(UserApi::class.java)
    }
}