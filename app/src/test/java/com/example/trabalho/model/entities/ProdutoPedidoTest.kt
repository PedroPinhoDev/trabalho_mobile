package com.example.trabalho.model.entities

import org.junit.Assert.*
import org.junit.Test

class ProdutoPedidoTest {

    @Test
    fun `default selecionado deve ser false`() {
        val p = ProdutoPedido(
            id = 1L,
            imagePath = "path",
            imagemResId = 10,
            descricao = "Desc",
            preco = 5.0,
            detalhes = "Det"
        )
        assertFalse(p.selecionado)
    }

    @Test
    fun `equals e hashCode baseados em todos os campos`() {
        val a = ProdutoPedido(2L, null, 20, "D", 3.5, "X", selecionado = true)
        val b = ProdutoPedido(2L, null, 20, "D", 3.5, "X", selecionado = true)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `describeContents retorna zero`() {
        val p = ProdutoPedido(3L, null, 30, "Desc", 7.7, "Det")
        assertEquals(0, p.describeContents())
    }

    @Test
    fun `CREATOR newArray cria array do tamanho especificado com elementos nulos`() {
        val arr = ProdutoPedido.CREATOR.newArray(5)
        assertEquals(5, arr.size)
        assertTrue(arr.all { it == null })
    }

    @Test
    fun `toString inclui campos principais`() {
        val p = ProdutoPedido(4L, "/img", 40, "ABC", 9.9, "Detalhes", selecionado = false)
        val s = p.toString()
        assertTrue(s.contains("id=4"))
        assertTrue(s.contains("imagePath=/img"))
        assertTrue(s.contains("imagemResId=40"))
        assertTrue(s.contains("descricao=ABC"))
        assertTrue(s.contains("preco=9.9"))
        assertTrue(s.contains("detalhes=Detalhes"))
        assertTrue(s.contains("selecionado=false"))
    }
}
