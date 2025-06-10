package com.example.trabalho

import android.view.View
import android.widget.TextView
import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.view.adapter.PedidoFeitoAdapter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever

class PedidoFeitoAdapterTest {

    private lateinit var adapter: PedidoFeitoAdapter
    private lateinit var pedidos: List<Pedido>
    private lateinit var clickListener: (Pedido, Int) -> Unit

    @Before
    fun setup() {
        pedidos = listOf(
            Pedido(
                id = 1L,
                codigo = "P-1",
                produtos = emptyList(),
                total = 59.99,
                status = "Aberto"
            ),
            Pedido(
                id = 2L,
                codigo = "P-2",
                produtos = emptyList(),
                total = 79.99,
                status = "Fechado"
            )
        )

        clickListener = { _, _ -> }
        adapter = PedidoFeitoAdapter(pedidos, clickListener)
    }

    @Test
    fun `testa getItemCount retorna tamanho correto da lista`() {
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun `testa onBindViewHolder configura views corretamente para status Aberto`() {
        val holder = mock<PedidoFeitoAdapter.PedidoFeitoViewHolder>().apply {
            whenever(codigoPedido).thenReturn(mock(TextView::class.java))
            whenever(valorPedido).thenReturn(mock(TextView::class.java))
            whenever(statusIndicator).thenReturn(mock(View::class.java))
        }

        adapter.onBindViewHolder(holder, 0)

        verify(holder.codigoPedido).text = "P-1"
        verify(holder.valorPedido).text = "R$ 59,99"
        // Não verificamos o GradientDrawable em testes unitários
    }

    @Test
    fun `testa onBindViewHolder configura views corretamente para status Fechado`() {
        val holder = mock<PedidoFeitoAdapter.PedidoFeitoViewHolder>().apply {
            whenever(codigoPedido).thenReturn(mock(TextView::class.java))
            whenever(valorPedido).thenReturn(mock(TextView::class.java))
            whenever(statusIndicator).thenReturn(mock(View::class.java))
        }

        adapter.onBindViewHolder(holder, 1)

        verify(holder.codigoPedido).text = "P-2"
        verify(holder.valorPedido).text = "R$ 79,99"
        // Não verificamos o GradientDrawable em testes unitários
    }

    @Test
    fun `testa clickListener chamado ao clicar no item`() {
        val itemView = mock<View>()
        var clickListener: View.OnClickListener? = null
        doAnswer { invocation ->
            clickListener = invocation.getArgument(0)
            null
        }.`when`(itemView).setOnClickListener(any())

        val holder = mock<PedidoFeitoAdapter.PedidoFeitoViewHolder>().apply {
            whenever(itemView).thenReturn(itemView)
            whenever(codigoPedido).thenReturn(mock(TextView::class.java))
            whenever(valorPedido).thenReturn(mock(TextView::class.java))
            whenever(statusIndicator).thenReturn(mock(View::class.java))
        }

        var isCalled = false
        val testClickListener: (Pedido, Int) -> Unit = { _, _ -> isCalled = true }
        adapter = PedidoFeitoAdapter(pedidos, testClickListener)

        adapter.onBindViewHolder(holder, 0)
        clickListener?.onClick(itemView)

        assertTrue(isCalled)
    }
}