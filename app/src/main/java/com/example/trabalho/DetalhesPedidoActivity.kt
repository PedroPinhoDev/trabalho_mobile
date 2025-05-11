package com.example.trabalho

import Produto
import ProdutoPedido
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class DetalhesPedidoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalhes_pedidos)

        val codigoPedido = intent.getStringExtra("codigo") ?: "Pedido"
        val pedido = intent.getParcelableExtra<ProdutoPedido>("pedidoSelecionado")
        val produtos = intent.getSerializableExtra("produtos") as ArrayList<Produto>

        val titulo = findViewById<TextView>(R.id.textCodigoPedido)
        titulo.text = codigoPedido

        // Configura o RecyclerView para exibir a lista de produtos
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProdutoDetalheAdapter(produtos)

        // Calcula o valor total
        val total = produtos.sumOf { it.valor }

        // Exibe o valor total formatado
        val totalView = findViewById<TextView>(R.id.textTotal)
        totalView.text = "R$ %.2f".format(total)
    }
}

