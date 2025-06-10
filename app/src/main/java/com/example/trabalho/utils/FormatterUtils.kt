package com.example.trabalho.utils

import java.text.NumberFormat
import java.util.Locale

object FormatterUtils {
    private val localeBR = Locale("pt", "BR")
    private val currencyFormat = NumberFormat.getCurrencyInstance(localeBR)

    fun formatCurrency(value: String): String {
        val cleanString = value.replace(Regex("\\D"), "")
        return if (cleanString.isNotEmpty()) {
            val parsed = cleanString.toDouble() / 100
            currencyFormat.format(parsed).replace("\u00A0", " ") // Substitui espaço não quebrável por espaço regular
        } else {
            ""
        }
    }

    fun parseCurrencyToDouble(value: String): Double? {
        val raw = value.replace(Regex("[R$\\s.]"), "").replace(",", ".")
        return raw.toDoubleOrNull()
    }

    fun validateProductInput(
        descricao: String,
        valor: String,
        detalhes: String,
        imagemResId: Int
    ): String? {
        return when {
            descricao.isEmpty() -> "A descrição não pode estar vazia"
            valor.isEmpty() -> "O valor não pode estar vazio"
            parseCurrencyToDouble(valor)?.let { it <= 0.0 } == true -> "O valor deve ser maior que zero"
            detalhes.isEmpty() -> "Os detalhes não podem estar vazios"
            imagemResId == -1 -> "Selecione uma imagem"
            else -> null
        }
    }
}