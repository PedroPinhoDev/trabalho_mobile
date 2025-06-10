package com.example.trabalho.repository

import com.example.trabalho.R
import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.model.network.NetworkOrder
import com.example.trabalho.model.network.NetworkOrderRequest
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

class PedidoRepositoryTest {

    private val serviceMock = mockk<ApiService>()

    @Before
    fun setup() {
        // Injeta nosso mock no objeto ApiClient
        mockkObject(ApiClient)
        every { ApiClient.apiService } returns serviceMock

        PedidoRepository.pedidos.clear()
    }

    @Test
    fun `carregarPedidos popula a lista de pedidos`() = runBlocking {
        // Stub da chamada getOrders
        val netOrders = listOf(
            NetworkOrder(
                id = 1L,
                status = "Novo",
                products = listOf(NetworkProduct(101L, "P1", "D1", 10.0))
            )
        )
        coEvery { serviceMock.getOrders() } returns netOrders

        // Executa e verifica
        PedidoRepository.carregarPedidos()
        assertEquals(1, PedidoRepository.pedidos.size)

        val pedido = PedidoRepository.pedidos.first()
        assertEquals(1L, pedido.id)
        assertEquals("P-1", pedido.codigo)
        assertEquals("Novo", pedido.status)

        val pp = pedido.produtos.single()
        assertEquals(101L, pp.id)
        assertEquals(10.0, pp.preco, 0.0)
    }

}
