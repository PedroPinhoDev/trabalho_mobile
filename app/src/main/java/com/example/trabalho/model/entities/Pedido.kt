package com.example.trabalho.model.entities

import com.example.trabalho.model.entities.ProdutoPedido
import java.io.Serializable

data class Pedido(
    val id: Long? = null,
    val codigo: String,
    val produtos: List<ProdutoPedido>,
    val total: Double,
    var status: String = "Aberto"
) : Serializable