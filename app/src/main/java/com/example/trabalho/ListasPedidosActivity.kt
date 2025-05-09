package com.example.trabalho

import ProdutoPedido
import android.content.Context
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listas_pedidos)

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos)

        // Recebe os pedidos confirmados da SharedPreferences
        val pedidosRecebidos = obterPedidosDePreferencias()

        // Verificar se os pedidos foram recebidos corretamente
        if (pedidosRecebidos.isNotEmpty()) {
            listaPedidosFeitos.addAll(pedidosRecebidos)
        } else {
            // Caso não tenha recebido nenhum pedido, mostre uma mensagem
            Toast.makeText(this, "Nenhum pedido encontrado", Toast.LENGTH_SHORT).show()
        }

        // Configurar o RecyclerView e o Adapter
        adapter = PedidoFeitoAdapter(listaPedidosFeitos)
        recyclerViewPedidos.layoutManager = LinearLayoutManager(this)
        recyclerViewPedidos.adapter = adapter
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

