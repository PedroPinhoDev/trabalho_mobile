package com.example.trabalho

import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.model.entities.Produto
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.model.network.NetworkOrder
import com.example.trabalho.model.network.NetworkProduct
import com.example.trabalho.model.utils.toNetworkOrderRequest
import com.example.trabalho.model.utils.toNetworkProduct
import com.example.trabalho.model.utils.toPedido
import com.example.trabalho.model.utils.toProduto
import com.example.trabalho.model.utils.toProdutoPedido
import org.junit.Assert.assertEquals
import org.junit.Test

class ModelConverterTest {

    @Test
    fun `testa conversao de Produto para NetworkProduct`() {
        // Arrange
        val produto = Produto(
            id = 1L,
            descricao = "Camisa",
            valor = 59.99,
            detalhes = "Camisa de algodão",
            imagemResId = R.drawable.shirt_image
        )

        // Act
        val networkProduct = produto.toNetworkProduct()

        // Assert
        assertEquals(produto.id, networkProduct.id)
        assertEquals(produto.descricao, networkProduct.name)
        assertEquals(produto.valor, networkProduct.price, 0.01)
        assertEquals(produto.detalhes, networkProduct.description)
    }

    @Test
    fun `testa conversao de NetworkProduct para Produto`() {
        // Arrange
        val networkProduct = NetworkProduct(
            id = 2L,
            name = "Bermuda",
            price = 79.99,
            description = "Bermuda jeans"
        )
        val imagemResId = R.drawable.bermuda_image

        // Act
        val produto = networkProduct.toProduto(imagemResId)

        // Assert
        assertEquals(networkProduct.id, produto.id)
        assertEquals(networkProduct.name, produto.descricao)
        assertEquals(networkProduct.price, produto.valor, 0.01)
        assertEquals(networkProduct.description, produto.detalhes)
        assertEquals(imagemResId, produto.imagemResId)
    }

    @Test
    fun `testa conversao de ProdutoPedido para NetworkProduct`() {
        // Arrange
        val produtoPedido = ProdutoPedido(
            id = 3L,
            imagemResId = R.drawable.jeans_image,
            descricao = "Jeans",
            preco = 99.99,
            detalhes = "Calça jeans slim"
        )

        // Act
        val networkProduct = produtoPedido.toNetworkProduct()

        // Assert
        assertEquals(produtoPedido.id, networkProduct.id)
        assertEquals(produtoPedido.descricao, networkProduct.name)
        assertEquals(produtoPedido.preco, networkProduct.price, 0.01)
        assertEquals(produtoPedido.detalhes, networkProduct.description)
    }

    @Test
    fun `testa conversao de NetworkProduct para ProdutoPedido`() {
        // Arrange
        val networkProduct = NetworkProduct(
            id = 4L,
            name = "Chinelo",
            price = 29.99,
            description = "Chinelo de borracha"
        )
        val imagemResId = R.drawable.chinelo_image

        // Act
        val produtoPedido = networkProduct.toProdutoPedido(imagemResId)

        // Assert
        assertEquals(networkProduct.id, produtoPedido.id)
        assertEquals(networkProduct.name, produtoPedido.descricao)
        assertEquals(networkProduct.price, produtoPedido.preco, 0.01)
        assertEquals(networkProduct.description, produtoPedido.detalhes)
        assertEquals(imagemResId, produtoPedido.imagemResId)
        assertEquals(false, produtoPedido.selecionado)
    }

    @Test
    fun `testa conversao de Pedido para NetworkOrderRequest`() {
        // Arrange
        val pedido = Pedido(
            id = 1L,
            codigo = "P-1",
            produtos = listOf(
                ProdutoPedido(1L, R.drawable.shirt_image, "Camisa", 59.99, "Camisa de algodão"),
                ProdutoPedido(2L, R.drawable.bermuda_image, "Bermuda", 79.99, "Bermuda jeans")
            ),
            total = 139.98,
            status = "Aberto"
        )

        // Act
        val networkOrderRequest = pedido.toNetworkOrderRequest()

        // Assert
        assertEquals(pedido.status, networkOrderRequest.status)
        assertEquals(pedido.produtos.map { it.id }, networkOrderRequest.productIds)
    }

    @Test
    fun `testa conversao de NetworkOrder para Pedido`() {
        // Arrange
        val networkOrder = NetworkOrder(
            id = 1L,
            status = "Fechado",
            products = listOf(
                NetworkProduct(1L, "Camisa", "Camisa de algodão", 59.99),
                NetworkProduct(2L, "Bermuda", "Bermuda jeans", 79.99)
            )
        )
        val codigo = "P-1"
        val imagemResIds = listOf(R.drawable.shirt_image, R.drawable.bermuda_image)

        // Act
        val pedido = networkOrder.toPedido(codigo, imagemResIds)

        // Assert
        assertEquals(networkOrder.id, pedido.id)
        assertEquals(codigo, pedido.codigo)
        assertEquals(networkOrder.status, pedido.status)
        assertEquals(139.98, pedido.total, 0.01)
        assertEquals(2, pedido.produtos.size)
        assertEquals(imagemResIds[0], pedido.produtos[0].imagemResId)
        assertEquals(imagemResIds[1], pedido.produtos[1].imagemResId)
    }
}