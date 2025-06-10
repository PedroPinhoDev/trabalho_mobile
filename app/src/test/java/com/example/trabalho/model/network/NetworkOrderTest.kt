package com.example.trabalho.model.network

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class NetworkOrderTest {

    private val gson = Gson()

    @Test
    fun `id padrão deve ser null`() {
        val o = NetworkOrder(status = "Novo", products = emptyList())
        assertNull(o.id)
    }

    @Test
    fun `equals e hashCode considerando todos os campos`() {
        val p = NetworkProduct(id = 1, name = "X", description = "Y", price = 2.0)
        val o1 = NetworkOrder(id = 10, status = "A", products = listOf(p))
        val o2 = NetworkOrder(id = 10, status = "A", products = listOf(p))
        assertTrue(o1 == o2)
        assertEquals(o1.hashCode(), o2.hashCode())
    }

    @Test
    fun `desserializar Json aninhado corretamente`() {
        val json = """
            {
              "id": 3,
              "status": "Enviado",
              "products": [
                { "id": 5, "name": "Item", "description": "Desc", "price": 12.0 }
              ]
            }
        """.trimIndent()
        val order = gson.fromJson(json, NetworkOrder::class.java)
        assertEquals(3L, order.id)
        assertEquals("Enviado", order.status)
        assertEquals(1, order.products.size)
        val prod = order.products[0]
        assertEquals(5L, prod.id)
        assertEquals("Item", prod.name)
        assertEquals("Desc", prod.description)
        assertEquals(12.0, prod.price, 0.0)
    }

    @Test
    fun `toString inclui id, status e products`() {
        val p = NetworkProduct(id = 7, name = "Z", description = "D", price = 3.5)
        val o = NetworkOrder(id = 8, status = "X", products = listOf(p))
        val s = o.toString()
        assertTrue(s.contains("id=8"))
        assertTrue(s.contains("status=X"))
        assertTrue(s.contains("products=["))
    }
}
