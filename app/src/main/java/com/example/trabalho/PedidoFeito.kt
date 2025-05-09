package com.example.trabalho

enum class PedidoStatus {
    PENDENTE, CONCLUIDO, CANCELADO
}

data class PedidoFeito(
    val codigo: String,
    val valor: Double,
    val status: PedidoStatus
)

