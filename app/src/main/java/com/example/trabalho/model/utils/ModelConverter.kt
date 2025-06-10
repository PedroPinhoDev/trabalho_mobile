package com.example.trabalho.model.utils

import com.example.trabalho.R
import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.model.entities.Produto
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.model.network.NetworkOrder
import com.example.trabalho.model.network.NetworkOrderRequest
import com.example.trabalho.model.network.NetworkProduct

// 1) Produto <-> NetworkProduct
fun Produto.toNetworkProduct(): NetworkProduct =
    NetworkProduct(
        id          = this.id,
        name        = this.descricao,
        description = this.detalhes,
        price       = this.valor
    )

/**
 * Constrói um Produto a partir do NetworkProduct, preservando opcionalmente um
 * caminho local (imagePath) ou recurso (imagemResId).
 */
fun NetworkProduct.toProduto(
    imagemResId: Int,
    imagePath: String?
): Produto = Produto(
    id          = this.id,
    descricao   = this.name,
    valor       = this.price,
    detalhes    = this.description,
    imagePath   = imagePath,
    imagemResId = imagemResId
)

/** Sobrecarga para quando não há imagePath */
fun NetworkProduct.toProduto(imagemResId: Int): Produto =
    toProduto(imagemResId = imagemResId, imagePath = null)


// 2) ProdutoPedido <-> NetworkProduct  (normalmente para pedidos locais não mapeamos imagePath)
fun ProdutoPedido.toNetworkProduct(): NetworkProduct =
    NetworkProduct(
        id          = this.id,
        name        = this.descricao,
        description = this.detalhes,
        price       = this.preco
    )

fun NetworkProduct.toProdutoPedido(imagemResId: Int): ProdutoPedido =
    ProdutoPedido(
        id          = this.id ?: throw IllegalArgumentException("NetworkProduct.id é nulo"),
        imagePath   = null,               // pedidos da API não trazem path local
        imagemResId = imagemResId,
        descricao   = this.name,
        preco       = this.price,
        detalhes    = this.description,
        selecionado = false
    )


// 3) Pedido <-> NetworkOrder
fun Pedido.toNetworkOrderRequest(): NetworkOrderRequest =
    NetworkOrderRequest(
        status     = this.status,
        productIds = this.produtos.map { it.id }
    )

fun NetworkOrder.toPedido(codigo: String, imagemResIds: List<Int>): Pedido {
    val lista = this.products.mapIndexed { idx, np ->
        np.toProdutoPedido(
            imagemResId = imagemResIds.getOrElse(idx) { R.drawable.adicionar_imagem }
        )
    }
    return Pedido(
        id       = this.id,
        codigo   = codigo,
        produtos = lista,
        total    = lista.sumOf { it.preco },
        status   = this.status
    )
}
