package com.example.trabalho.service

import org.junit.Assert.*
import org.junit.Test

class ApiClientTest {

    @Test
    fun `apiService não é nulo`() {
        assertNotNull("apiService deve ser instanciado", ApiClient.apiService)
    }

    @Test
    fun `apiService é singleton`() {
        val a = ApiClient.apiService
        val b = ApiClient.apiService
        assertSame("apiService deve retornar sempre a mesma instância", a, b)
    }
}
