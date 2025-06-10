package com.example.trabalho.model.entities

import org.junit.Assert.*
import org.junit.Test

class PedidoTest {

    @Test
    fun `status padrão deve ser Aberto`() {
        val pedido = Pedido(
            id = 42,
            codigo = "ABC123",
            produtos = emptyList(),
            total = 0.0
        )
        assertEquals("Aberto", pedido.status)
    }

    @Test
    fun `equals e hashCode devem ser baseados em todos os campos`() {
        val p1 = Pedido(1, "C1", listOf(), 10.0, "Pago")
        val p2 = Pedido(1, "C1", listOf(), 10.0, "Pago")
        assertTrue(p1 == p2)
        assertEquals(p1.hashCode(), p2.hashCode())
    }

    @Test
    fun `component functions e copy funcionam corretamente`() {
        val orig = Pedido(5, "X", listOf(), 99.9)
        // destructuring
        val (id, codigo, produtos, total, status) = orig
        assertEquals(5L, id)
        assertEquals("X", codigo)
        assertTrue(produtos.isEmpty())
        assertEquals(99.9, total, 0.0)
        assertEquals("Aberto", status)

        // copy altera apenas o campo passado
        val copia = orig.copy(status = "Fechado")
        assertEquals("Fechado", copia.status)
        assertEquals(orig.id, copia.id)
        assertEquals(orig.codigo, copia.codigo)
    }

    @Test
    fun `status pode ser mutado`() {
        val p = Pedido(2, "ZZ", listOf(), 1.0)
        p.status = "Entregue"
        assertEquals("Entregue", p.status)
    }
}
