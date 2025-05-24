package com.example.trabalho.service

import com.example.trabalho.model.network.NetworkOrder
import com.example.trabalho.model.network.NetworkOrderRequest
import com.example.trabalho.model.network.NetworkProduct
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<NetworkProduct>

    @GET("orders")
    suspend fun getOrders(): List<NetworkOrder>

    // *** NOVO: buscar um pedido específico ***
    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") id: Long): NetworkOrder

    @POST("products")
    suspend fun createProduct(@Body product: NetworkProduct): NetworkProduct

    @POST("orders")
    suspend fun createOrder(@Body orderRequest: NetworkOrderRequest): NetworkOrder

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Body product: NetworkProduct
    ): NetworkProduct

    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Long,
        @Body orderRequest: NetworkOrderRequest
    ): NetworkOrder

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>
}
