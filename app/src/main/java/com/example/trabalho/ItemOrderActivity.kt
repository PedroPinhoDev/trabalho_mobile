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
    private val listaPedidosFeitos: ArrayList<ProdutoPedido> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_order)

        carregarPedidosFeitos()

        orderNumber = 1
        salvarNumeroPedido(orderNumber)

        val homeIcon = findViewById<ImageView>(R.id.home)
        homeIcon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val historicoIcon = findViewById<ImageView>(R.id.historico)
        historicoIcon.setOnClickListener {
                // Navega para a ListasPedidosActivity ao clicar no ícone de histórico
                val intent = Intent(this, ListasPedidosActivity::class.java)
                startActivity(intent)
        }


        recyclerView = findViewById(R.id.recyclerViewProdutos)
        listaProdutos = intent.getParcelableArrayListExtra<ProdutoPedido>("produtos") ?: arrayListOf()

        val textViewSemPedidos = findViewById<TextView>(R.id.textViewSemPedidos)

        if (listaPedidosFeitos.isNotEmpty()) {
            adapterFeitos = PedidoFeitoAdapter(listaPedidosFeitos){ pedido, position ->
                // Exemplo de ação ao clicar em um pedido feito
                val intent = Intent(this, DetalhesPedidoActivity::class.java)
                intent.putExtra("pedidoSelecionado", Gson().toJson(pedido))
                startActivity(intent)
            }
            recyclerView.adapter = adapterFeitos
            textViewSemPedidos.visibility = View.GONE
        } else {
            // Caso contrário, mostre a lista de produtos com PedidoAdapter
            adapter = PedidoAdapter(listaProdutos) { produto, isChecked ->
                produto.selecionado = isChecked
            }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            // Verifique se há produtos selecionados para mostrar ou não a mensagem
            if (recyclerView.adapter?.itemCount == 0) {
                textViewSemPedidos.visibility = View.VISIBLE
            } else {
                textViewSemPedidos.visibility = View.GONE
            }
        }

        val btnConfirmarPedido: MaterialButton = findViewById(R.id.btnConfirmarPedido)
        btnConfirmarPedido.setOnClickListener {
            if (nenhumItemSelecionado()) {
                mostrarAlerta("Nenhum item selecionado!")
            } else {
                confirmarPedido()
            }
        }
    }

    private fun confirmarPedido() {
        val mensagem = "Pedido $orderNumber confirmado com sucesso!"
        val builder = AlertDialog.Builder(this)
        builder.setMessage(mensagem)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()

                salvarPedidoFeito()
                salvarPedidosEmPreferencias()

                // Limpar os itens selecionados na lista de produtos
                listaProdutos.forEach { it.selecionado = false }

                // Notificar o adaptador para atualizar o RecyclerView
                adapter.notifyDataSetChanged()

                // Incrementar o número do pedido para o próximo
                orderNumber++
                salvarNumeroPedido(orderNumber)
            }
            .setCancelable(false)
        builder.create().show()
    }

    private fun salvarPedidoFeito() {
        val produtosSelecionados = listaProdutos.filter { it.selecionado }
        if (produtosSelecionados.isNotEmpty()) {
            listaPedidosFeitos.addAll(produtosSelecionados) // Adiciona os produtos selecionados diretamente
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

    override fun onStop() {
        super.onStop()
        salvarNumeroPedido(1)
        listaPedidosFeitos.clear()

        salvarPedidosEmPreferencias()
    }

    private fun carregarPedidosFeitos() {
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("pedidosFeitos", null)
        val type = object : TypeToken<ArrayList<ProdutoPedido>>() {}.type
        val pedidosSalvos: ArrayList<ProdutoPedido>? = gson.fromJson(json, type)

        // Garantir que a lista foi carregada corretamente
        if (pedidosSalvos != null) {
            listaPedidosFeitos.clear()
            listaPedidosFeitos.addAll(pedidosSalvos)
        }
    }

}

