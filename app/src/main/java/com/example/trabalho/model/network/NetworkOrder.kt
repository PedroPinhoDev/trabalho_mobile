package com.example.trabalho.model.network

import com.google.gson.annotations.SerializedName

data class NetworkOrder(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("status") val status: String,
    @SerializedName("products") val products: List<NetworkProduct>
)