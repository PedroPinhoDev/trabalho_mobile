package com.example.trabalho.model.utils

import com.example.trabalho.model.entities.Produto
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.R
import com.example.trabalho.model.network.NetworkProduct
import com.example.trabalho.model.network.NetworkOrder
import com.example.trabalho.model.network.NetworkOrderRequest

// Mapeia Produto do app para o modelo de rede
fun Produto.toNetworkProduct(): NetworkProduct {
    return NetworkProduct(
        id = this.id,
        name = this.descricao,
        description = this.detalhes,
        price = this.valor
    )
}

// Mapeia NetworkProduct (resposta da API) para Produto do app
fun NetworkProduct.toProduto(imagemResId: Int): Produto {
    return Produto(
        id = this.id,
        descricao = this.name,
        valor = this.price,
        detalhes = this.description,
        imagemResId = imagemResId
    )
}

// Mapeia ProdutoPedido do app para NetworkProduct (caso precise criar produtos)
fun ProdutoPedido.toNetworkProduct(): NetworkProduct {
    return NetworkProduct(
        id = this.id,
        name = this.descricao,
        description = this.detalhes,
        price = this.preco
    )
}

// Mapeia NetworkProduct (dentro de pedido) para ProdutoPedido do app
fun NetworkProduct.toProdutoPedido(imagemResId: Int): ProdutoPedido {
    return ProdutoPedido(
        id = this.id ?: throw IllegalArgumentException("NetworkProduct.id é nulo"),
        imagemResId = imagemResId,
        descricao = this.name,
        preco = this.price,
        detalhes = this.description,
        selecionado = false
    )
}

// Mapeia Pedido do app para DTO de rede para criação/atualização
fun Pedido.toNetworkOrderRequest(): NetworkOrderRequest {
    return NetworkOrderRequest(
        status = this.status,
        productIds = this.produtos.map { it.id }
    )
}

// Mapeia NetworkOrder (resposta da API) para Pedido do app
fun NetworkOrder.toPedido(codigo: String, imagemResIds: List<Int>): Pedido {
    val produtos = this.products.mapIndexed { index, networkProduct ->
        networkProduct.toProdutoPedido(
            imagemResIds.getOrElse(index) { R.drawable.adicionar_imagem }
        )
    }
    return Pedido(
        id = this.id,
        codigo = codigo,
        produtos = produtos,
        total = produtos.sumOf { it.preco },
        status = this.status
    )
}
