package com.example.trabalho

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import com.example.trabalho.utils.FormatterUtils

class FormatterUtilsTest {

    @Test
    fun `testa formatacao de moeda com valor valido`() {
        // Arrange
        val input = "12345"

        // Act
        val result = FormatterUtils.formatCurrency(input)

        // Assert
        assertEquals("R$ 123,45", result)
    }

    @Test
    fun `testa formatacao de moeda com valor vazio`() {
        // Arrange
        val input = ""

        // Act
        val result = FormatterUtils.formatCurrency(input)

        // Assert
        assertEquals("", result)
    }

    @Test
    fun `testa parsing de moeda para double`() {
        // Arrange
        val input = "R$ 123,45"

        // Act
        val result = FormatterUtils.parseCurrencyToDouble(input)

        // Assert
        if (result != null) {
            assertEquals(123.45, result, 0.01)
        }
    }

    @Test
    fun `testa parsing de moeda com valor invalido`() {
        // Arrange
        val input = "R$ abc"

        // Act
        val result = FormatterUtils.parseCurrencyToDouble(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `testa validacao de entrada com todos os campos preenchidos corretamente`() {
        // Arrange
        val descricao = "Camisa"
        val valor = "R$ 59,99"
        val detalhes = "Camisa de algodão"
        val imagemResId = R.drawable.shirt_image

        // Act
        val result = FormatterUtils.validateProductInput(descricao, valor, detalhes, imagemResId)

        // Assert
        assertNull(result)
    }

    @Test
    fun `testa validacao de entrada com descricao vazia`() {
        // Arrange
        val descricao = ""
        val valor = "R$ 59,99"
        val detalhes = "Camisa de algodão"
        val imagemResId = R.drawable.shirt_image

        // Act
        val result = FormatterUtils.validateProductInput(descricao, valor, detalhes, imagemResId)

        // Assert
        assertEquals("A descrição não pode estar vazia", result)
    }

    @Test
    fun `testa validacao de entrada com valor invalido`() {
        // Arrange
        val descricao = "Camisa"
        val valor = "R$ 0,00"
        val detalhes = "Camisa de algodão"
        val imagemResId = R.drawable.shirt_image

        // Act
        val result = FormatterUtils.validateProductInput(descricao, valor, detalhes, imagemResId)

        // Assert
        assertEquals("O valor deve ser maior que zero", result)
    }

    @Test
    fun `testa validacao de entrada com imagem nao selecionada`() {
        // Arrange
        val descricao = "Camisa"
        val valor = "R$ 59,99"
        val detalhes = "Camisa de algodão"
        val imagemResId = -1

        // Act
        val result = FormatterUtils.validateProductInput(descricao, valor, detalhes, imagemResId)

        // Assert
        assertEquals("Selecione uma imagem", result)
    }
}