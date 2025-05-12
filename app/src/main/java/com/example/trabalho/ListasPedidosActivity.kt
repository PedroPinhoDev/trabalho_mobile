package com.example.trabalho

import Produto
import ProdutoPedido
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListasPedidosActivity : AppCompatActivity() {

    private lateinit var recyclerViewPedidos: RecyclerView
    private lateinit var adapter: PedidoFeitoAdapter
    private var listaPedidosFeitos: ArrayList<ProdutoPedido> = arrayListOf()
    private lateinit var textViewVazio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listas_pedidos)

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos)
        textViewVazio = findViewById(R.id.textViewVazio)

        // Recebe os pedidos confirmados da SharedPreferences
        val pedidosRecebidos = obterPedidosDePreferencias()

        // Verificar se os pedidos foram recebidos corretamente
        if (pedidosRecebidos.isNotEmpty()) {
            listaPedidosFeitos.addAll(pedidosRecebidos)
            textViewVazio.visibility = View.GONE
        } else {
            // Caso não tenha recebido nenhum pedido, mostre uma mensagem
            textViewVazio.visibility = View.VISIBLE
        }


        // Configurar o RecyclerView e o Adapter
        adapter = PedidoFeitoAdapter(listaPedidosFeitos) { pedido, posicao ->
            val intent = Intent(this, DetalhesPedidoActivity::class.java)
            intent.putExtra("pedidoSelecionado", Gson().toJson(pedido))
            intent.putExtra("codigo", "P-${posicao + 1}")
            startActivity(intent)
        }
        recyclerViewPedidos.layoutManager = LinearLayoutManager(this)
        recyclerViewPedidos.adapter = adapter


        val homeIcon = findViewById<ImageView>(R.id.home)
        val carrinhoIcon = findViewById<ImageView>(R.id.carrinho)

        homeIcon.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        carrinhoIcon.setOnClickListener {
            startActivity(Intent(this, ItemOrderActivity::class.java))
            finish()
        }
    }

    private fun obterPedidosDePreferencias(): ArrayList<ProdutoPedido> {
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("pedidosFeitos", null)
        val type = object : TypeToken<ArrayList<ProdutoPedido>>() {}.type

        val lista = gson.fromJson<ArrayList<ProdutoPedido>>(json, type)

        return lista ?: ArrayList() // Retorna uma lista vazia se não houver pedidos
    }

}

