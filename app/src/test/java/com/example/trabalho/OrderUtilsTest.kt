package com.example.trabalho

import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.utils.OrderUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OrderUtilsTest {

    @Test
    fun `testa validateSelectedProducts com nenhum produto selecionado`() {
        // Arrange
        val produtos = listOf(
            ProdutoPedido(1L, R.drawable.shirt_image, "Camisa", 59.99, "Camisa de algodão", selecionado = false)
        )

        // Act
        val result = OrderUtils.validateSelectedProducts(produtos)

        // Assert
        assertEquals("Nenhum item selecionado", result)
    }

    @Test
    fun `testa validateSelectedProducts com pelo menos um produto selecionado`() {
        // Arrange
        val produtos = listOf(
            ProdutoPedido(1L, R.drawable.shirt_image, "Camisa", 59.99, "Camisa de algodão", selecionado = true)
        )

        // Act
        val result = OrderUtils.validateSelectedProducts(produtos)

        // Assert
        assertNull(result)
    }

    @Test
    fun `testa createOrder com produtos selecionados`() {
        // Arrange
        val produtos = listOf(
            ProdutoPedido(1L, R.drawable.shirt_image, "Camisa", 59.99, "Camisa de algodão", selecionado = true),
            ProdutoPedido(2L, R.drawable.bermuda_image, "Bermuda", 79.99, "Bermuda jeans", selecionado = false)
        )

        // Act
        val pedido = OrderUtils.createOrder(produtos, codigo = "P-1")

        // Assert
        assertEquals("P-1", pedido.codigo)
        assertEquals(1, pedido.produtos.size)
        assertEquals(59.99, pedido.total, 0.01)
        assertEquals("Aberto", pedido.status)
        assertEquals(produtos[0], pedido.produtos[0])
    }

    @Test
    fun `testa clearSelection limpa todos os produtos selecionados`() {
        // Arrange
        val produtos = mutableListOf(
            ProdutoPedido(1L, R.drawable.shirt_image, "Camisa", 59.99, "Camisa de algodão", selecionado = true),
            ProdutoPedido(2L, R.drawable.bermuda_image, "Bermuda", 79.99, "Bermuda jeans", selecionado = true)
        )

        // Act
        OrderUtils.clearSelection(produtos)

        // Assert
        assertEquals(false, produtos[0].selecionado)
        assertEquals(false, produtos[1].selecionado)
    }
}