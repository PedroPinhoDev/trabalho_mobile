package com.example.trabalho.model.entities

import org.junit.Assert.*
import org.junit.Test

class ProdutoPedidoPureTest {

    private val original = ProdutoPedido(
        id = 7L,
        imagePath = "/path",
        imagemResId = 99,
        descricao = "Item",
        preco = 12.34,
        detalhes = "Detalhes",
        selecionado = true
    )

    @Test fun `componentN e copy`() {
        val (id, path, resId, desc, price, det, sel) = original
        assertEquals(7L, id)
        assertEquals("/path", path)
        assertEquals(99, resId)
        assertEquals("Item", desc)
        assertEquals(12.34, price, 0.0)
        assertEquals("Detalhes", det)
        assertTrue(sel)

        val copy = original.copy(selecionado = false)
        assertFalse(copy.selecionado)
        assertEquals(original.id, copy.id)
    }

    @Test fun `equals e hashCode`() {
        val o2 = original.copy()
        assertTrue(original == o2)
        assertEquals(original.hashCode(), o2.hashCode())
    }

    @Test fun `toString inclui campos`() {
        val s = original.toString()
        listOf("id=7", "imagePath=/path", "imagemResId=99", "descricao=Item", "preco=12.34", "detalhes=Detalhes").forEach {
            assertTrue(s.contains(it))
        }
    }

    @Test fun `describeContents sempre zero`() {
        assertEquals(0, original.describeContents())
    }
}
