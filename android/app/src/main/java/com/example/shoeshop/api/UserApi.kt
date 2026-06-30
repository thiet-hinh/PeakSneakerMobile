package com.example.shoeshop.api

import com.example.shoeshop.dto.request.RegisterRequest
import com.example.shoeshop.dto.request.UpdateProfileRequest
import com.example.shoeshop.dto.respone.UserProfile
import com.example.shoeshop.model.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Streaming

interface UserApi {

    @POST("api/user/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<User>

    @GET("api/user/{uid}")
    suspend fun getUserByFireBaseUid(@Path("uid") uid: String): Response<User>

    @GET("api/user/{userId}/profile")
    suspend fun getProfile(
        @Path("userId") userId: Int
    ): Response<UserProfile>

    @PUT("api/user/{userId}/profile")
    suspend fun updateProfile(
        @Path("userId") userId: Int,
        @Body request: UpdateProfileRequest
    ): Response<UserProfile>

    @GET("api/user/{userId}/avatar")
    @Streaming
    suspend fun getAvatar(
        @Path("userId") userId: Int
    ): Response<ResponseBody>

    @PUT("api/user/{userId}/avatar")
    suspend fun updateAvatar(
        @Path("userId") userId: Int,
        @Body avatar: RequestBody
    ): Response<Unit>

}