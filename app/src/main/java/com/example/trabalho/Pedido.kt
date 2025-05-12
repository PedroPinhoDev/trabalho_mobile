package com.example.trabalho

import ProdutoPedido
import java.io.Serializable

data class Pedido (
    val codigo: String,
    val produtos: List<ProdutoPedido>,
    val total: Double,
    var status: String = "Aberto"
    ) : Serializable
