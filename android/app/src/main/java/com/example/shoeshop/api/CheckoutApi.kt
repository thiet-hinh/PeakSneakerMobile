package com.example.shoeshop.api

import com.example.shoeshop.dto.request.CheckoutPreviewRequest
import com.example.shoeshop.dto.respone.CheckoutPreviewResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckoutApi {
    @POST("api/checkout/preview/{userId}")
    suspend fun getCheckoutPreview(
        @Path("userId") userId: Int,
        @Body request: CheckoutPreviewRequest
    ): Response<CheckoutPreviewResponse>
}