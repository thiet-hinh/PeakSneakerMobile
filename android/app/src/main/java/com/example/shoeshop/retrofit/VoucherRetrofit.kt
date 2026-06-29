package com.example.shoeshop.retrofit

import com.example.shoeshop.api.VoucherApi

object VoucherRetrofit {
    val api : VoucherApi by lazy {
        BaseRetrofit.retrofit.create(VoucherApi::class.java)
    }
}