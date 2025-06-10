package com.example.trabalho.model.network

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class NetworkProductTest {

    private val gson = Gson()

    @Test
    fun `id padrão deve ser null`() {
        val p = NetworkProduct(name = "Caneca", description = "Cerâmica", price = 25.0)
        assertNull(p.id)
    }

    @Test
    fun `equals e hashCode baseados em todos os campos`() {
        val a = NetworkProduct(id = 1, name = "X", description = "Y", price = 9.9)
        val b = NetworkProduct(id = 1, name = "X", description = "Y", price = 9.9)
        assertTrue(a == b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `desserializar Json usando SerializedName`() {
        val json = """
            {
              "id": 5,
              "name": "Caneca",
              "description": "Porcelana",
              "price": 30.5
            }
        """.trimIndent()
        val obj = gson.fromJson(json, NetworkProduct::class.java)
        assertEquals(5L, obj.id)
        assertEquals("Caneca", obj.name)
        assertEquals("Porcelana", obj.description)
        assertEquals(30.5, obj.price, 0.0)
    }

    @Test
    fun `toString inclui campos principais`() {
        val p = NetworkProduct(id = 2, name = "A", description = "B", price = 1.0)
        val s = p.toString()
        assertTrue(s.contains("id=2"))
        assertTrue(s.contains("name=A"))
        assertTrue(s.contains("description=B"))
        assertTrue(s.contains("price=1.0"))
    }
}
