package com.example.trabalho.repository

import com.example.trabalho.R
import com.example.trabalho.model.entities.Produto
import com.example.trabalho.model.network.NetworkProduct
import com.example.trabalho.service.ApiClient
import com.example.trabalho.service.ApiService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProdutoRepositoryTest {

    private val serviceMock: ApiService = mockk()

    @Before
    fun setup() {
        // injeta o mock no ApiClient
        mockkObject(ApiClient)
        every { ApiClient.apiService } returns serviceMock

        // limpa estado
        ProdutoRepository.produtos.clear()
        ProdutoRepository.imageMap.clear()
        ProdutoRepository.imagePathMap.clear()
    }


}
