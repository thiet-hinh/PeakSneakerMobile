package com.example.shoeshop.api

import com.example.shoeshop.dto.request.AddressRequest
import com.example.shoeshop.dto.respone.AddressItem
import com.example.shoeshop.model.Address
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AddressApi {
    @GET("api/user/{userId}/address/default")
    suspend fun getDefaultAddress(@Path("userId") userId: String) : Response<Address>

    @POST("api/user/{userId}/address/default")
    suspend fun updateDefaultAddress(
        @Path("userId") userId: Int,
        @Body request: AddressRequest
    ): Response<Address>

    @GET("api/address/ghn/province")
    suspend fun getProvinces(): Response<List<AddressItem>>

    @GET("api/address/ghn/district/{provinceId}")
    suspend fun getDistricts(
        @Path("provinceId") provinceId: Int
    ): Response<List<AddressItem>>

    @GET("api/address/ghn/ward/{districtId}")
    suspend fun getWards(
        @Path("districtId") districtId: Int
    ): Response<List<AddressItem>>
}