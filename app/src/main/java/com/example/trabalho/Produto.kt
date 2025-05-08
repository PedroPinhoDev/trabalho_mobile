package com.example.trabalho
import java.io.Serializable

data class Produto (
    val descricao: String,
    val valor: String,
    val detalhes: String,
    val ImagemResId: Int
) : Serializable
