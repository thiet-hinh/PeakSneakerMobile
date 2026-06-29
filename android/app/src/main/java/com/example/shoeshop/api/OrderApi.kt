package com.example.shoeshop.api

import com.example.shoeshop.dto.request.PlaceOrderRequest
import com.example.shoeshop.dto.respone.OrderDetailResponse
import com.example.shoeshop.dto.respone.OrderResponse
import com.example.shoeshop.dto.respone.PlaceOrderResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface OrderApi {
    @GET("api/order/user/{userId}")
    suspend fun getOrdersByUserAndStatus(
        @Path("userId") userId: Int,
        @Query("status") status: String?
    ): List<OrderResponse>

    @GET("api/order/{orderId}")
    suspend fun getOrderDetail(
        @Path("orderId") orderId: Int
    ): OrderDetailResponse

    @PATCH("api/order/{orderId}/status")
    suspend fun updateOrderStatus(
        @Path("orderId") orderId: Int,
        @Query("status") status: String
    ): Response<ResponseBody>

    @POST("api/order/place-order")
    suspend fun placeOrder(
        @Body request: PlaceOrderRequest
    ): Response<PlaceOrderResponse>
}
