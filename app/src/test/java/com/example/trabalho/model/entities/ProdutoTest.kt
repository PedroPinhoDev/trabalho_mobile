package com.example.trabalho.model.entities

import org.junit.Assert.*
import org.junit.Test

class ProdutoTest {

    @Test
    fun `equals e toString incluem campos`() {
        val a = Produto(1, "Descr", 5.0, "Det", null, 123)
        val b = Produto(1, "Descr", 5.0, "Det", null, 123)
        assertTrue(a == b)
        val s = a.toString()
        assertTrue(s.contains("id=1"))
        assertTrue(s.contains("descricao=Descr"))
        assertTrue(s.contains("valor=5.0"))
    }

    @Test
    fun `component functions e copy funcionam corretamente`() {
        val orig = Produto(10, "D", 2.2, "X", "/img", 321)
        // destructuring
        val (id, descricao, valor, detalhes, imagePath, imagemResId) = orig
        assertEquals(10L, id)
        assertEquals("D", descricao)
        assertEquals(2.2, valor, 0.0)
        assertEquals("X", detalhes)
        assertEquals("/img", imagePath)
        assertEquals(321, imagemResId)

        // copy altera só o campo necessário
        val copia = orig.copy(valor = 9.9)
        assertEquals(9.9, copia.valor, 0.0)
        assertEquals(orig.descricao, copia.descricao)
    }
}
