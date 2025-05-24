package com.example.trabalho

import java.io.Serializable

data class Produto(
    val id: Long? = null,
    val descricao: String,
    val valor: Double,
    val detalhes: String,
    val imagemResId: Int
) : Serializable