package com.example.trabalho.model.utils

import com.example.trabalho.R
import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.model.entities.Produto
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.model.network.NetworkOrder
import com.example.trabalho.model.network.NetworkOrderRequest
import com.example.trabalho.model.network.NetworkProduct
import org.junit.Assert.*
import org.junit.Test

class ModelConverterTest {

    @Test
    fun `Produto toNetworkProduct mapeia todos os campos`() {
        val produto = Produto(
            id = 11L,
            descricao = "Desc",
            valor = 7.5,
            detalhes = "Det",
            imagePath = "/img.png",
            imagemResId = 999
        )

        val net = produto.toNetworkProduct()
        assertEquals(11L, net.id)
        assertEquals("Desc", net.name)
        assertEquals("Det", net.description)
        assertEquals(7.5, net.price, 0.0)
    }

    @Test
    fun `NetworkProduct toProduto preserva imagePath e imagemResId`() {
        val net = NetworkProduct(
            id = 22L,
            name = "N",
            description = "D",
            price = 3.3
        )

        val prod = net.toProduto(imagemResId = 123, imagePath = "/path")
        assertEquals(22L, prod.id)
        assertEquals("N", prod.descricao)
        assertEquals("D", prod.detalhes)
        assertEquals(3.3, prod.valor, 0.0)
        assertEquals("/path", prod.imagePath)
        assertEquals(123, prod.imagemResId)
    }

    @Test
    fun `NetworkProduct toProduto sem imagePath funciona corretamente`() {
        val net = NetworkProduct(id = 33L, name = "X", description = "Y", price = 4.4)
        val prod = net.toProduto(imagemResId = 456)
        assertNull(prod.imagePath)
        assertEquals(456, prod.imagemResId)
    }

    @Test
    fun `ProdutoPedido toNetworkProduct mapeia corretamente`() {
        val pedidoItem = ProdutoPedido(
            id = 3L,
            imagePath = "/p",
            imagemResId = 321,
            descricao = "It",
            preco = 2.2,
            detalhes = "Dt",
            selecionado = true
        )
        val net = pedidoItem.toNetworkProduct()
        assertEquals(3L, net.id)
        assertEquals("It", net.name)
        assertEquals("Dt", net.description)
        assertEquals(2.2, net.price, 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `NetworkProduct toProdutoPedido com id null lança IllegalArgumentException`() {
        NetworkProduct(id = null, name = "A", description = "B", price = 1.1)
            .toProdutoPedido(imagemResId = 111)
    }

    @Test
    fun `NetworkProduct toProdutoPedido mapeia corretamente`() {
        val net = NetworkProduct(id = 5L, name = "Nm", description = "Ds", price = 6.6)
        val pp = net.toProdutoPedido(imagemResId = 777)
        assertEquals(5L, pp.id)
        assertNull(pp.imagePath)
        assertEquals(777, pp.imagemResId)
        assertEquals("Nm", pp.descricao)
        assertEquals(6.6, pp.preco, 0.0)
        assertEquals("Ds", pp.detalhes)
        assertFalse(pp.selecionado)
    }

    @Test
    fun `Pedido toNetworkOrderRequest inclui status e productIds`() {
        val item = ProdutoPedido(9L, null, 0, "D", 1.0, "X", false)
        val pedido = Pedido(id = 99L, codigo = "C", produtos = listOf(item), total = 1.0, status = "S")
        val req = pedido.toNetworkOrderRequest()
        assertEquals("S", req.status)
        assertEquals(listOf(9L), req.productIds)
    }

    @Test
    fun `NetworkOrder toPedido mapeia para Pedido com imageResIds`() {
        val netProd = NetworkProduct(id = 7L, name = "Nm7", description = "Dt7", price = 2.2)
        val netOrder = NetworkOrder(id = 88L, status = "OK", products = listOf(netProd))

        val ped = netOrder.toPedido(codigo = "X", imagemResIds = listOf(444))
        assertEquals(88L, ped.id)
        assertEquals("X", ped.codigo)
        assertEquals("OK", ped.status)

        val pp = ped.produtos.single()
        assertEquals(7L, pp.id)
        assertNull(pp.imagePath)
        assertEquals(444, pp.imagemResId)
        assertEquals("Nm7", pp.descricao)
        assertEquals(2.2, pp.preco, 0.0)
        assertEquals("Dt7", pp.detalhes)
        assertFalse(pp.selecionado)

        assertEquals(2.2, ped.total, 0.0)
    }

    @Test
    fun `NetworkOrder toPedido com lista vazia de imageResIds usa fallback`() {
        val netProd = NetworkProduct(id = 8L, name = "Nm8", description = "Dt8", price = 3.3)
        val netOrder = NetworkOrder(id = 99L, status = "ST", products = listOf(netProd))

        val ped = netOrder.toPedido(codigo = "Y", imagemResIds = emptyList())
        val pp = ped.produtos.single()
        assertEquals(R.drawable.adicionar_imagem, pp.imagemResId)
    }
}
