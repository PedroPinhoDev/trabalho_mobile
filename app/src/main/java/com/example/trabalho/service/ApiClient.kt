package com.example.trabalho.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // URL antiga para o emulador Android
    // private const val BASE_URL = "http://10.0.2.2:8080/"

    // ✅ NOVA URL para o servidor no Render
    private const val BASE_URL = "https://comanda-digital.onrender.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}