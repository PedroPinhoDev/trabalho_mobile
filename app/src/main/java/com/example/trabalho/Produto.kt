import java.io.Serializable

data class Produto(
    val descricao: String,
    val valor: Double,
    val detalhes: String,
    val imagemResId: Int
) : Serializable
