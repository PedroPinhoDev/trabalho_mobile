package com.example.trabalho.model.network

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class NetworkOrderRequestTest {

    private val gson = Gson()

    @Test
    fun `equals e hashCode devem considerar status e productIds`() {
        val r1 = NetworkOrderRequest(status = "Novo", productIds = listOf(1, 2, 3))
        val r2 = NetworkOrderRequest(status = "Novo", productIds = listOf(1, 2, 3))
        assertTrue(r1 == r2)
        assertEquals(r1.hashCode(), r2.hashCode())
    }

    @Test
    fun `toString inclui status e lista de ids`() {
        val req = NetworkOrderRequest(status = "OK", productIds = listOf(10, 20))
        val s = req.toString()
        assertTrue(s.contains("status=OK"))
        assertTrue(s.contains("productIds=[10, 20]"))
    }

    @Test
    fun `serializar e desserializar com Gson preserva os campos`() {
        val original = NetworkOrderRequest(status = "Fechado", productIds = listOf(7, 8, 9))
        val json = gson.toJson(original)
        val back = gson.fromJson(json, NetworkOrderRequest::class.java)
        assertEquals(original, back)
    }
}
