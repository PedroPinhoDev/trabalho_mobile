package com.example.trabalho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import kotlin.collections.filter

class ItemOrderActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PedidoAdapter
    private var listaProdutos: ArrayList<ProdutoPedido> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_order)

        val modo = intent.getStringExtra("modo") ?: "pedido"

        recyclerView = findViewById(R.id.recyclerViewProdutos)
        val textViewSemPedidos = findViewById<TextView>(R.id.textViewSemPedidos)
        val btnConfirmarPedido = findViewById<MaterialButton>(R.id.btnConfirmarPedido)

        if (modo == "pedido") {
            listaProdutos = intent.getParcelableArrayListExtra("produtos") ?: arrayListOf()
            adapter = PedidoAdapter(listaProdutos) { produto, isChecked ->
                produto.selecionado = isChecked
            }
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            textViewSemPedidos.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE

            btnConfirmarPedido.setOnClickListener {
                if (nenhumItemSelecionado()) {
                    mostrarAlerta("Nenhum item selecionado!")
                } else {
                    confirmarPedido()
                }
            }
        } else {
            btnConfirmarPedido.visibility = View.GONE
            // Modo histórico será tratado em ListasPedidosActivity
        }

        findViewById<ImageView>(R.id.home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageView>(R.id.historico).setOnClickListener {
            startActivity(Intent(this, ListasPedidosActivity::class.java))
        }
    }

    private fun confirmarPedido() {
        val produtosSelecionados = listaProdutos.filter { it.selecionado }
        if (produtosSelecionados.isNotEmpty()) {
            val total = produtosSelecionados.sumOf { it.preco }
            val codigo = "P-${System.currentTimeMillis()}" // Código temporário
            val pedido = Pedido(codigo = codigo, produtos = produtosSelecionados, total = total)

            lifecycleScope.launch {
                PedidoRepository.criarPedido(pedido)
                mostrarAlerta("Pedido confirmado com sucesso!")
                listaProdutos.forEach { it.selecionado = false }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun nenhumItemSelecionado(): Boolean {
        return listaProdutos.none { it.selecionado }
    }

    private fun mostrarAlerta(mensagem: String) {
        AlertDialog.Builder(this)
            .setMessage(mensagem)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}