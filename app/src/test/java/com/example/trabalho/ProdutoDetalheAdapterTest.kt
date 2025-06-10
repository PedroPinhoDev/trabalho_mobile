package com.example.trabalho

import android.widget.ImageView
import android.widget.TextView
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.view.adapter.ProdutoDetalheAdapter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class ProdutoDetalheAdapterTest {

    private lateinit var adapter: ProdutoDetalheAdapter
    private lateinit var produtos: List<ProdutoPedido>

    @Before
    fun setup() {
        produtos = listOf(
            ProdutoPedido(
                id = 1L,
                imagemResId = R.drawable.shirt_image,
                descricao = "Camisa",
                preco = 59.99,
                detalhes = "Camisa de algodão"
            )
        )
        adapter = ProdutoDetalheAdapter(produtos)
    }

    @Test
    fun `testa getItemCount retorna tamanho correto da lista`() {
        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun `testa onBindViewHolder configura views corretamente`() {
        val holder = mock<ProdutoDetalheAdapter.ProdutoViewHolder>().apply {
            whenever(nome).thenReturn(mock(TextView::class.java))
            whenever(preco).thenReturn(mock(TextView::class.java))
            whenever(imagem).thenReturn(mock(ImageView::class.java))
        }

        adapter.onBindViewHolder(holder, 0)

        verify(holder.nome).text = "Camisa"
        verify(holder.preco).text = "R$ 59,99"
        verify(holder.imagem).setImageResource(R.drawable.shirt_image)
    }
}