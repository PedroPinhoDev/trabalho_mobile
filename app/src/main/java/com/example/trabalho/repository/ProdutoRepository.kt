package com.example.trabalho.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.trabalho.R
import com.example.trabalho.model.entities.Produto
import com.example.trabalho.model.utils.toNetworkProduct
import com.example.trabalho.model.utils.toProduto
import com.example.trabalho.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ProdutoRepository {
    private val apiService = ApiClient.apiService

    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "prod_prefs"

    internal val imageMap = mutableMapOf<Long, Int>()
    internal val imagePathMap = mutableMapOf<Long, String>()
    val produtos = mutableListOf<Produto>()

    /**
     * Carrega do SharedPreferences os caminhos de imagem persistidos.
     * Chamar uma única vez (ex.: em MainActivity.onCreate).
     */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.all
            .filterKeys { it.startsWith("imagePath_") }
            .forEach { (key, value) ->
                val rawId = key.removePrefix("imagePath_")
                val id = try { rawId.toLong() } catch (e: Exception) { return@forEach }
                imagePathMap[id] = value as String
            }
    }

    /** Carrega produtos do backend reaproveitando paths ou resources em memória */
    suspend fun carregarProdutos() = withContext(Dispatchers.IO) {
        produtos.clear()
        val networkProducts = apiService.getProducts()
        networkProducts.forEach { np ->
            val id = np.id
            val (path, resId) = if (id != null && imagePathMap.containsKey(id)) {
                imagePathMap[id]!! to R.drawable.adicionar_imagem
            } else {
                null to (id?.let { imageMap[it] } ?: R.drawable.adicionar_imagem)
            }
            produtos.add(np.toProduto(imagePath = path, imagemResId = resId))
        }
    }

    /**
     * Cria no backend, persiste o path em SharedPreferences e mantém em memória.
     */
    suspend fun adicionarProduto(context: Context, produto: Produto) = withContext(Dispatchers.IO) {
        val created = apiService.createProduct(produto.toNetworkProduct())
        created.id?.let { newId ->
            produto.imagePath?.let { path ->
                imagePathMap[newId] = path
                prefs.edit().putString("imagePath_$newId", path).apply()
            } ?: run {
                imageMap[newId] = produto.imagemResId
            }
            produtos.add(created.toProduto(imagePath = produto.imagePath, imagemResId = produto.imagemResId))
        }
    }

    /**
     * Atualiza backend e memória; sincroniza SharedPreferences conforme necessário.
     */
    suspend fun atualizarProduto(index: Int, produto: Produto) = withContext(Dispatchers.IO) {
        val old = produtos[index]
        val id = old.id ?: return@withContext
        val updated = apiService.updateProduct(id, produto.toNetworkProduct().copy(id = id))

        produto.imagePath?.let { path ->
            imagePathMap[id] = path
            imageMap.remove(id)
            prefs.edit().putString("imagePath_$id", path).apply()
        } ?: run {
            imageMap[id] = produto.imagemResId
            imagePathMap.remove(id)
            prefs.edit().remove("imagePath_$id").apply()
        }

        produtos[index] = updated.toProduto(imagePath = produto.imagePath, imagemResId = produto.imagemResId)
    }

    /**
     * Remove do backend, memória e SharedPreferences.
     */
    suspend fun excluirProduto(index: Int) = withContext(Dispatchers.IO) {
        val id = produtos[index].id ?: return@withContext
        apiService.deleteProduct(id.toInt())
        produtos.removeAt(index)
        imageMap.remove(id)
        imagePathMap.remove(id)
        prefs.edit().remove("imagePath_$id").apply()
    }
}
