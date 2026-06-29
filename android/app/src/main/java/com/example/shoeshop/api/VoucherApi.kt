package com.example.shoeshop.api

import com.example.shoeshop.dto.request.ApplyVoucherRequest
import com.example.shoeshop.dto.respone.ApplyVoucherResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VoucherApi {
    @POST("api/voucher/apply")
    suspend fun applyVoucher(
        @Body request: ApplyVoucherRequest
    ): Response<ApplyVoucherResponse>
}