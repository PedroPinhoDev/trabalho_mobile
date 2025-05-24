package com.example.trabalho.model.network

data class NetworkOrderRequest(
    val status: String,
    val productIds: List<Long>
)
