package com.example.trabalho

import Produto
import ProdutoPedido
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListasPedidosActivity : AppCompatActivity() {

    private lateinit var recyclerViewPedidos: RecyclerView
    private lateinit var adapter: PedidoFeitoAdapter
    private val listaPedidosFeitos: ArrayList<Pedido> = arrayListOf()
    private lateinit var textViewVazio: TextView
    private lateinit var detalhesLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listas_pedidos)

        detalhesLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val pedidoJson = result.data?.getStringExtra("pedidoAlterado")
                val pedidoAlterado = Gson().fromJson(pedidoJson, Pedido::class.java)
                for (i in listaPedidosFeitos.indices) {
                    if (listaPedidosFeitos[i].codigo == pedidoAlterado.codigo) {
                        listaPedidosFeitos[i] = pedidoAlterado
                        break
                    }
                }
                adapter.notifyDataSetChanged()

                // Salvar a lista atualizada de pedidos em SharedPreferences
                salvarPedidosEmPreferencias()
            }
        }

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
            val pedidoJson = Gson().toJson(pedido)
            intent.putExtra("pedidoSelecionado", Gson().toJson(pedido))
            intent.putExtra("codigo", "P-${posicao + 1}")
            Log.d("ListasPedidos", "Pedido enviado: $pedidoJson")

            detalhesLauncher.launch(intent)
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

    private fun obterPedidosDePreferencias(): ArrayList<Pedido> {
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("pedidosFeitos", null)

        return if (json != null) {
            val type = object : TypeToken<ArrayList<Pedido>>() {}.type
            gson.fromJson(json, type) ?: arrayListOf()
        } else {
            arrayListOf()
        }
    }

    private fun salvarPedidosEmPreferencias() {
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(listaPedidosFeitos)
        editor.putString("pedidosFeitos", json)
        editor.apply()
    }


    override fun onResume() {
        super.onResume()
        // Recarregar os pedidos da SharedPreferences
        listaPedidosFeitos.clear()
        listaPedidosFeitos.addAll(obterPedidosDePreferencias())
        adapter.notifyDataSetChanged() // Atualizar a lista exibida
    }

    private fun carregarPedidos() {
        val sharedPreferences = getSharedPreferences("pedidos", MODE_PRIVATE)
        val json = sharedPreferences.getString("pedidosFeitos", null)
        val gson = Gson()
        val pedidosSalvos = if (json != null) {
            gson.fromJson<ArrayList<Pedido>>(json, object : TypeToken<ArrayList<Pedido>>() {}.type)
        } else arrayListOf()

        listaPedidosFeitos.clear()
        listaPedidosFeitos.addAll(pedidosSalvos)
        adapter.notifyDataSetChanged()
    }
}

