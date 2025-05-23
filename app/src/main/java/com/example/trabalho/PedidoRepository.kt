package com.example.trabalho

import com.example.trabalho.service.ApiClient
import com.example.trabalho.utils.toNetworkOrderRequest
import com.example.trabalho.utils.toPedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PedidoRepository {
    private val apiService = ApiClient.apiService
    val pedidos = mutableListOf<Pedido>()

    suspend fun carregarPedidos() {
        withContext(Dispatchers.IO) {
            val networkOrders = apiService.getOrders()
            pedidos.clear()
            networkOrders.forEachIndexed { index, order ->
                val codigo = "P-${index + 1}"
                val imagens = order.products.map { R.drawable.adicionar_imagem }
                pedidos.add(order.toPedido(codigo, imagens))
            }
        }
    }

    suspend fun criarPedido(pedido: Pedido) {
        withContext(Dispatchers.IO) {
            val request = pedido.toNetworkOrderRequest()
            val created = apiService.createOrder(request)
            val codigo  = "P-${pedidos.size + 1}"
            val imagens = created.products.map { R.drawable.adicionar_imagem }
            pedidos.add(created.toPedido(codigo, imagens))
        }
    }

    suspend fun atualizarPedido(pedido: Pedido) {
        withContext(Dispatchers.IO) {
            val request = pedido.toNetworkOrderRequest()
            val updated = apiService.updateOrder(pedido.id!!, request)
            val idx = pedidos.indexOfFirst { it.id == pedido.id }
            if (idx != -1) {
                val codigo  = "P-${idx + 1}"
                val imagens = updated.products.map { R.drawable.adicionar_imagem }
                pedidos[idx] = updated.toPedido(codigo, imagens)
            }
        }
    }

    suspend fun excluirPedido(index: Int): Boolean {
        // implementação conforme sua necessidade
        TODO("Implementar exclusão se for necessária")
    }

    /**
     * Busca um único pedido pelo ID diretamente da API
     */
    suspend fun buscarPedidoPorId(id: Long): Pedido = withContext(Dispatchers.IO) {
        val networkOrder = apiService.getOrderById(id)
        val codigo = "P-$id"
        val imagens = networkOrder.products.map { R.drawable.adicionar_imagem }
        networkOrder.toPedido(codigo, imagens)
    }
}
