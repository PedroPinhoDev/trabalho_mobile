package com.example.trabalho

import PedidoAdapter
import ProdutoPedido
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemOrderActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PedidoAdapter
    private lateinit var adapterFeitos: PedidoFeitoAdapter
    private var orderNumber = 1
    private var listaProdutos: ArrayList<ProdutoPedido> = arrayListOf()
    private val listaPedidosFeitos: ArrayList<Pedido> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_order)

        val modo = intent.getStringExtra("modo") ?: "historico"

        recyclerView = findViewById(R.id.recyclerViewProdutos)
        val textViewSemPedidos = findViewById<TextView>(R.id.textViewSemPedidos)
        val btnConfirmarPedido: MaterialButton = findViewById(R.id.btnConfirmarPedido)

        if (modo == "pedido") {
            // Mostrar produtos para selecionar e montar pedido
            listaProdutos = intent.getParcelableArrayListExtra("produtos") ?: arrayListOf()

            adapter = PedidoAdapter(listaProdutos) { produto, isChecked ->
                produto.selecionado = isChecked
            }

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            textViewSemPedidos.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE

            orderNumber = recuperarNumeroPedido()

            btnConfirmarPedido.setOnClickListener {
                if (nenhumItemSelecionado()) {
                    mostrarAlerta("Nenhum item selecionado!")
                } else {
                    confirmarPedido()
                }
            }

        } else {
            // Mostrar pedidos feitos
            carregarPedidosFeitos()

            adapterFeitos = PedidoFeitoAdapter(listaPedidosFeitos) { pedido, position ->
                val intent = Intent(this, DetalhesPedidoActivity::class.java)
                intent.putExtra("pedidoSelecionado", Gson().toJson(pedido))
                startActivity(intent)
            }

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapterFeitos

            textViewSemPedidos.visibility =
                if (listaPedidosFeitos.isEmpty()) View.VISIBLE else View.GONE

            // Oculta botão de confirmar, pois não faz sentido aqui
            btnConfirmarPedido.visibility = View.GONE
        }

        // Ícones de navegação
        findViewById<ImageView>(R.id.home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageView>(R.id.historico).setOnClickListener {
            startActivity(Intent(this, ListasPedidosActivity::class.java))
        }
    }


    private fun confirmarPedido() {
        val mensagem = "Pedido $orderNumber confirmado com sucesso!"
        val builder = AlertDialog.Builder(this)
        builder.setMessage(mensagem)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()

                // Salva o pedido feito
                salvarPedidoFeito()

                // Limpa os itens selecionados na lista de produtos
                listaProdutos.forEach { it.selecionado = false }

                // Notifica o adaptador para atualizar o RecyclerView
                adapter.notifyDataSetChanged()

                // Incrementa o número do pedido para o próximo
                orderNumber++
                salvarNumeroPedido(orderNumber)

                // Não redireciona para a tela de histórico aqui
                // Remova ou comente a linha abaixo:
                // val intent = Intent(this, ListasPedidosActivity::class.java)
                // startActivity(intent)
            }
            .setCancelable(false)
        builder.create().show()
    }


    private fun salvarPedidoFeito() {
        val produtosSelecionados = listaProdutos.filter { it.selecionado }
        if (produtosSelecionados.isNotEmpty()) {
            val total = produtosSelecionados.sumOf { it.preco }
            val codigoPedido = "P-${orderNumber++}"  // código único baseado no tempo

            val pedido = Pedido(codigoPedido, produtosSelecionados, total)

            // Carregar pedidos existentes
            val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
            val gson = Gson()
            val pedidosExistentesJson = sharedPreferences.getString("pedidosFeitos", null)
            val type = object : TypeToken<MutableList<Pedido>>() {}.type
            val pedidosSalvos: MutableList<Pedido> = gson.fromJson(pedidosExistentesJson, type) ?: mutableListOf()

            // Adicionar o novo pedido
            pedidosSalvos.add(pedido)

            // Salvar novamente os pedidos no SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("pedidosFeitos", gson.toJson(pedidosSalvos))
            editor.apply()

            Log.d("Pedidos", "Pedido salvo com sucesso: ${gson.toJson(pedido)}")
        }
    }




    fun nenhumItemSelecionado(): Boolean {
        return listaProdutos.none { it.selecionado }
    }

    private fun mostrarAlerta(mensagem: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(mensagem)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun salvarPedidosEmPreferencias() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(listaPedidosFeitos)
        editor.putString("pedidosFeitos", json)
        editor.apply()
    }

    private fun recuperarNumeroPedido(): Int {
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("orderNumber", 1) // 1 é o valor inicial
    }

    private fun salvarNumeroPedido(orderNumber: Int) {
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("orderNumber", orderNumber)
        editor.apply()
    }

//    override fun onStop() {
//        super.onStop()
//        salvarNumeroPedido(1)
////        listaPedidosFeitos.clear()
////
////        salvarPedidosEmPreferencias()
//    }

    private fun carregarPedidosFeitos() {
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("pedidosFeitos", null)
        val type = object : TypeToken<ArrayList<Pedido>>() {}.type
        val pedidosSalvos: ArrayList<Pedido>? = gson.fromJson(json, type)

        if (pedidosSalvos != null) {
            listaPedidosFeitos.clear()
            listaPedidosFeitos.addAll(pedidosSalvos)
        }
    }

}

