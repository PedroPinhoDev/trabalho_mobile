package com.example.trabalho.repository

import com.example.trabalho.R
import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.model.network.NetworkOrderRequest
import com.example.trabalho.model.utils.toNetworkOrderRequest
import com.example.trabalho.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PedidoRepository {

    private val apiService = ApiClient.apiService
    val pedidos = mutableListOf<Pedido>()

    /**
     * Carrega todos os pedidos, usando o ID de cada pedido como código.
     * Agora preenche cada ProdutoPedido com imagePath ou imagemResId.
     */
    suspend fun carregarPedidos() = withContext(Dispatchers.IO) {
        pedidos.clear()
        val networkOrders = apiService.getOrders()
        networkOrders.forEach { order ->
            val codigo = "P-${order.id}"
            // Mapeia cada produto da ordem
            val listaProdutosPedido = order.products.map { np ->
                val pid = np.id ?: throw IllegalArgumentException("NetworkProduct.id é nulo")
                // tenta o path salvo; senão, recurso interno
                val path = ProdutoRepository.imagePathMap[pid]
                val resId = if (path != null) {
                    // se veio da galeria, usamos placeholder no resId
                    R.drawable.adicionar_imagem
                } else {
                    // senão, reutiliza o recurso salvo (ou placeholder)
                    ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
                }
                ProdutoPedido(
                    id          = pid,
                    imagePath   = path,
                    imagemResId = resId,
                    descricao   = np.name,
                    preco       = np.price,
                    detalhes    = np.description,
                    selecionado = false
                )
            }
            val total = listaProdutosPedido.sumOf { it.preco }
            pedidos.add(Pedido(
                id      = order.id,
                codigo  = codigo,
                produtos= listaProdutosPedido,
                total   = total,
                status  = order.status
            ))
        }
    }

    /**
     * Cria um pedido no backend e adiciona à lista, preenchendo imagePath/resId
     */
    suspend fun criarPedido(pedido: Pedido) = withContext(Dispatchers.IO) {
        val request = pedido.toNetworkOrderRequest()
        val created = apiService.createOrder(request)
        val codigo = "P-${created.id}"
        val listaProdutosPedido = created.products.map { np ->
            val pid = np.id ?: throw IllegalArgumentException("NetworkProduct.id é nulo")
            val path = ProdutoRepository.imagePathMap[pid]
            val resId = if (path != null) R.drawable.adicionar_imagem
            else ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
            ProdutoPedido(
                id          = pid,
                imagePath   = path,
                imagemResId = resId,
                descricao   = np.name,
                preco       = np.price,
                detalhes    = np.description,
                selecionado = false
            )
        }
        val total = listaProdutosPedido.sumOf { it.preco }
        pedidos.add(Pedido(
            id      = created.id,
            codigo  = codigo,
            produtos= listaProdutosPedido,
            total   = total,
            status  = created.status
        ))
    }

    /**
     * Cria um pedido e retorna o objeto Pedido resultante, com imagePath/resId
     */
    suspend fun criarPedidoRetornando(pedido: Pedido): Pedido = withContext(Dispatchers.IO) {
        val request = pedido.toNetworkOrderRequest()
        val created = apiService.createOrder(request)
        val codigo = "P-${created.id}"
        val listaProdutosPedido = created.products.map { np ->
            val pid = np.id ?: throw IllegalArgumentException("NetworkProduct.id é nulo")
            val path = ProdutoRepository.imagePathMap[pid]
            val resId = if (path != null) R.drawable.adicionar_imagem
            else ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
            ProdutoPedido(
                id          = pid,
                imagePath   = path,
                imagemResId = resId,
                descricao   = np.name,
                preco       = np.price,
                detalhes    = np.description,
                selecionado = false
            )
        }
        val total = listaProdutosPedido.sumOf { it.preco }
        val novoPedido = Pedido(
            id      = created.id,
            codigo  = codigo,
            produtos= listaProdutosPedido,
            total   = total,
            status  = created.status
        )
        pedidos.add(novoPedido)
        novoPedido
    }

    /**
     * Atualiza um pedido existente e sua imagem, se houver
     */
    suspend fun atualizarPedido(pedido: Pedido) = withContext(Dispatchers.IO) {
        val request = pedido.toNetworkOrderRequest()
        val updated = apiService.updateOrder(pedido.id!!, request)
        val idx = pedidos.indexOfFirst { it.id == pedido.id }
        if (idx != -1) {
            val codigo = "P-${updated.id}"
            val listaProdutosPedido = updated.products.map { np ->
                val pid = np.id ?: throw IllegalArgumentException("NetworkProduct.id é nulo")
                val path = ProdutoRepository.imagePathMap[pid]
                val resId = if (path != null) R.drawable.adicionar_imagem
                else ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
                ProdutoPedido(
                    id          = pid,
                    imagePath   = path,
                    imagemResId = resId,
                    descricao   = np.name,
                    preco       = np.price,
                    detalhes    = np.description,
                    selecionado = false
                )
            }
            val total = listaProdutosPedido.sumOf { it.preco }
            pedidos[idx] = Pedido(
                id      = updated.id,
                codigo  = codigo,
                produtos= listaProdutosPedido,
                total   = total,
                status  = updated.status
            )
        }
    }

    /**
     * Exclui um pedido (não implementado no backend)
     */
    suspend fun excluirPedido(index: Int): Boolean = false

    /**
     * Busca um pedido específico pelo ID, preenchendo imagePath/resId
     */
    suspend fun buscarPedidoPorId(id: Long): Pedido = withContext(Dispatchers.IO) {
        val networkOrder = apiService.getOrderById(id)
        val codigo = "P-$id"
        val listaProdutosPedido = networkOrder.products.map { np ->
            val pid = np.id ?: throw IllegalArgumentException("NetworkProduct.id é nulo")
            val path = ProdutoRepository.imagePathMap[pid]
            val resId = if (path != null) R.drawable.adicionar_imagem
            else ProdutoRepository.imageMap[pid] ?: R.drawable.adicionar_imagem
            ProdutoPedido(
                id          = pid,
                imagePath   = path,
                imagemResId = resId,
                descricao   = np.name,
                preco       = np.price,
                detalhes    = np.description,
                selecionado = false
            )
        }
        val total = listaProdutosPedido.sumOf { it.preco }
        Pedido(id = id, codigo = codigo, produtos = listaProdutosPedido, total = total, status = networkOrder.status)
    }
}
