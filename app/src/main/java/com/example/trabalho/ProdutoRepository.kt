package com.example.trabalho

import Produto
import ProdutoPedido
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ProdutoRepository {
    val produtos = mutableListOf<Produto>()
}