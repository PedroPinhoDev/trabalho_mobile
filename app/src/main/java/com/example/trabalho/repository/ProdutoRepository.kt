package com.example.trabalho.repository

import com.example.trabalho.R
import com.example.trabalho.model.entities.Produto
import com.example.trabalho.model.utils.toNetworkProduct
import com.example.trabalho.model.utils.toProduto
import com.example.trabalho.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ProdutoRepository {
    private val apiService = ApiClient.apiService

    // Guarda o placeholder ou a escolha do usuário por ID
    private val imageMap = mutableMapOf<Long, Int>()
    val produtos = mutableListOf<Produto>()

    /** Carrega todos os produtos do servidor, reaproveitando imagens locais */
    suspend fun carregarProdutos() {
        withContext(Dispatchers.IO) {
            val networkProducts = apiService.getProducts()

            // Reconstrói a lista sem perder imagens existentes
            produtos.clear()
            networkProducts.forEach { np ->
                val imgRes = np.id?.let { imageMap[it] } ?: R.drawable.adicionar_imagem
                produtos.add(np.toProduto(imgRes))
            }
        }
    }

    /** Adiciona um produto novo e registra sua imagem no mapa */
    suspend fun adicionarProduto(produto: Produto) {
        withContext(Dispatchers.IO) {
            val networkProduct = produto.toNetworkProduct()
            val created = apiService.createProduct(networkProduct)

            // O backend retorna o ID, agora pode mapear a imagem
            created.id?.let { imageMap[it] = produto.imagemResId }
            produtos.add(created.toProduto(produto.imagemResId))
        }
    }

    /** Atualiza um produto existente e atualiza a imagem no mapa */
    suspend fun atualizarProduto(index: Int, produto: Produto) {
        withContext(Dispatchers.IO) {
            val old = produtos[index]
            val id = old.id!!

            val networkProduct = produto.toNetworkProduct().copy(id = id)
            val updated = apiService.updateProduct(id, networkProduct)

            // registra possível nova imagem
            imageMap[id] = produto.imagemResId
            produtos[index] = updated.toProduto(produto.imagemResId)
        }
    }

    /** Exclui um produto e remove do mapa */
    suspend fun excluirProduto(index: Int) {
        withContext(Dispatchers.IO) {
            val id = produtos[index].id!!.toInt()
            apiService.deleteProduct(id)
            produtos.removeAt(index)
            imageMap.remove(id.toLong())
        }
    }
}