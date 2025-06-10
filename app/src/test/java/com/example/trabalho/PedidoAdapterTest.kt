package com.example.trabalho

import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.view.adapter.PedidoAdapter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever

class PedidoAdapterTest {

    private lateinit var adapter: PedidoAdapter
    private lateinit var produtos: List<ProdutoPedido>
    private lateinit var onItemCheckedChange: (ProdutoPedido, Boolean) -> Unit

    @Before
    fun setup() {
        produtos = listOf(
            ProdutoPedido(
                id = 1L,
                imagemResId = R.drawable.shirt_image,
                descricao = "Camisa",
                preco = 59.99,
                detalhes = "Camisa de algodão",
                selecionado = false
            )
        )

        // Implementação real para verificar chamadas
        onItemCheckedChange = { _, _ -> }
        adapter = PedidoAdapter(produtos, onItemCheckedChange)
    }

    @Test
    fun `testa getItemCount retorna tamanho correto da lista`() {
        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun `testa onBindViewHolder configura views corretamente`() {
        // Cria um holder real com mocks das views
        val holder = mock<PedidoAdapter.ProdutoViewHolder>().apply {
            whenever(imagem).thenReturn(mock(ImageView::class.java))
            whenever(descricao).thenReturn(mock(TextView::class.java))
            whenever(preco).thenReturn(mock(TextView::class.java))
            whenever(detalhes).thenReturn(mock(TextView::class.java))
            whenever(checkBox).thenReturn(mock(CheckBox::class.java))
        }

        adapter.onBindViewHolder(holder, 0)

        verify(holder.imagem).setImageResource(R.drawable.shirt_image)
        verify(holder.descricao).text = "Camisa"
        verify(holder.preco).text = "R$ 59,99"
        verify(holder.detalhes).text = "Camisa de algodão"
        verify(holder.checkBox).isChecked = false
    }

    @Test
    fun `testa onItemCheckedChange chamado quando checkbox é alterado`() {
        // Configuração do mock do CheckBox
        val checkBox = mock<CheckBox>()
        var listener: CompoundButton.OnCheckedChangeListener? = null
        doAnswer { invocation ->
            listener = invocation.getArgument(0)
            null
        }.`when`(checkBox).setOnCheckedChangeListener(any())

        // Cria um holder real com mocks das views
        val holder = mock<PedidoAdapter.ProdutoViewHolder>().apply {
            whenever(imagem).thenReturn(mock(ImageView::class.java))
            whenever(descricao).thenReturn(mock(TextView::class.java))
            whenever(preco).thenReturn(mock(TextView::class.java))
            whenever(detalhes).thenReturn(mock(TextView::class.java))
            whenever(checkBox).thenReturn(checkBox)
        }

        // Variável para verificar chamada
        var isCalled = false
        val testOnItemCheckedChange: (ProdutoPedido, Boolean) -> Unit = { _, _ -> isCalled = true }
        adapter = PedidoAdapter(produtos, testOnItemCheckedChange)

        adapter.onBindViewHolder(holder, 0)
        listener?.onCheckedChanged(checkBox, true)

        assertTrue(isCalled)
    }
}