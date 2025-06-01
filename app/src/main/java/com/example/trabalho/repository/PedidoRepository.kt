package com.example.trabalho.repository

import com.example.trabalho.R
import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.model.utils.toNetworkOrderRequest
import com.example.trabalho.model.utils.toPedido
import com.example.trabalho.repository.ProdutoRepository
import com.example.trabalho.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PedidoRepository {

    private val apiService = ApiClient.apiService
    val pedidos = mutableListOf<Pedido>()

    /**
     * Carrega todos os pedidos, usando o ID de cada pedido como código.
     * Agora mapeia cada produto ao seu imagemResId armazenado em ProdutoRepository.imageMap.
     */
    suspend fun carregarPedidos() = withContext(Dispatchers.IO) {
        pedidos.clear()
        val networkOrders = apiService.getOrders()
        networkOrders.forEach { order ->
            val codigo = "P-${order.id}"
            val imagens = order.products.map { np ->
                np.id?.let { pid ->
                    ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
                } ?: R.drawable.adicionar_imagem
            }
            pedidos.add(order.toPedido(codigo, imagens))
        }
    }

    /**
     * Cria um pedido no backend e adiciona à lista, usando o ID retornado como código.
     * Também tenta recuperar a imagem de cada produto pelo imageMap.
     */
    suspend fun criarPedido(pedido: Pedido) = withContext(Dispatchers.IO) {
        val request = pedido.toNetworkOrderRequest()
        val created = apiService.createOrder(request)
        val codigo = "P-${created.id}"
        val imagens = created.products.map { np ->
            np.id?.let { pid ->
                ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
            } ?: R.drawable.adicionar_imagem
        }
        pedidos.add(created.toPedido(codigo, imagens))
    }

    /**
     * Cria um pedido e retorna o objeto Pedido resultante, com código baseado no ID.
     * Inclui o mapeamento de imagem via imageMap.
     */
    suspend fun criarPedidoRetornando(pedido: Pedido): Pedido = withContext(Dispatchers.IO) {
        val request = pedido.toNetworkOrderRequest()
        val created = apiService.createOrder(request)
        val codigo = "P-${created.id}"
        val imagens = created.products.map { np ->
            np.id?.let { pid ->
                ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
            } ?: R.drawable.adicionar_imagem
        }
        val novoPedido = created.toPedido(codigo, imagens)
        pedidos.add(novoPedido)
        novoPedido
    }

    /**
     * Atualiza um pedido existente, usando o mesmo ID como código.
     * Atualiza também a lista local de pedidos com o imagemResId correto.
     */
    suspend fun atualizarPedido(pedido: Pedido) = withContext(Dispatchers.IO) {
        val request = pedido.toNetworkOrderRequest()
        val updated = apiService.updateOrder(pedido.id!!, request)
        val idx = pedidos.indexOfFirst { it.id == pedido.id }
        if (idx != -1) {
            val codigo = "P-${updated.id}"
            val imagens = updated.products.map { np ->
                np.id?.let { pid ->
                    ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
                } ?: R.drawable.adicionar_imagem
            }
            pedidos[idx] = updated.toPedido(codigo, imagens)
        }
    }

    /**
     * Exclui um pedido (implementação opcional dependendo da API).
     */
    suspend fun excluirPedido(index: Int): Boolean {
        // TODO: implementar exclusão no backend se necessário
        return false
    }

    /**
     * Obtém um pedido específico pelo ID, usando ID como código.
     * Agora recupera cada imagem de produto pelo imageMap.
     */
    suspend fun buscarPedidoPorId(id: Long): Pedido = withContext(Dispatchers.IO) {
        val networkOrder = apiService.getOrderById(id)
        val codigo = "P-$id"
        val imagens = networkOrder.products.map { np ->
            np.id?.let { pid ->
                ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
            } ?: R.drawable.adicionar_imagem
        }
        networkOrder.toPedido(codigo, imagens)
    }
}
