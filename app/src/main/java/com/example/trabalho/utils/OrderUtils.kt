package com.example.trabalho.utils

import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.model.entities.ProdutoPedido

object OrderUtils {

    fun validateSelectedProducts(produtos: List<ProdutoPedido>): String? {
        return if (produtos.none { it.selecionado }) {
            "Nenhum item selecionado"
        } else {
            null
        }
    }

    fun createOrder(produtos: List<ProdutoPedido>, codigo: String = ""): Pedido {
        val selecionados = produtos.filter { it.selecionado }
        val total = selecionados.sumOf { it.preco }
        return Pedido(
            id = null,
            codigo = codigo,
            produtos = selecionados,
            total = total,
            status = "Aberto"
        )
    }

    fun clearSelection(produtos: MutableList<ProdutoPedido>) {
        produtos.forEach { it.selecionado = false }
    }
}