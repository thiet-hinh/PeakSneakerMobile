package com.example.shoeshop.api

import com.example.shoeshop.dto.request.RegisterRequest
import com.example.shoeshop.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @POST("api/user/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>

    @GET("api/user/{uid}")
    suspend fun getUserByFireBaseUid(@Path("uid") uid: String): Response<User>
}