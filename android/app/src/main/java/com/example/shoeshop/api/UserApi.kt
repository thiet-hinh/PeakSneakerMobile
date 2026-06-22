package com.example.shoeshop.api

import com.example.shoeshop.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("api/user/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>
}