package com.example.trabalho.model.network

import com.google.gson.annotations.SerializedName

data class NetworkProduct(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double
)