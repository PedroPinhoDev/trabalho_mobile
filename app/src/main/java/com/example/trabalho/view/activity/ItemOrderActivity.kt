package com.example.trabalho.view.activity

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
import com.example.trabalho.view.activity.ListasPedidosActivity
import com.example.trabalho.repository.PedidoRepository
import com.example.trabalho.R
import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.view.adapter.PedidoAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ItemOrderActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PedidoAdapter
    private var listaProdutos: ArrayList<ProdutoPedido> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_order)

        recyclerView = findViewById(R.id.recyclerViewProdutos)
        val textViewSemPedidos = findViewById<TextView>(R.id.textViewSemPedidos)
        val btnConfirmarPedido = findViewById<MaterialButton>(R.id.btnConfirmarPedido)

        listaProdutos = intent.getParcelableArrayListExtra("produtos") ?: arrayListOf()
        adapter =
            PedidoAdapter(listaProdutos) { produto, isChecked -> produto.selecionado = isChecked }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        textViewSemPedidos.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE

        btnConfirmarPedido.setOnClickListener {
            if (listaProdutos.none { it.selecionado }) {
                AlertDialog.Builder(this)
                    .setMessage("Nenhum item selecionado!")
                    .setPositiveButton("OK") { dlg, _ -> dlg.dismiss() }
                    .show()
            } else {
                confirmarPedido()
            }
        }

        findViewById<ImageView>(R.id.home).setOnClickListener {
            finish()
        }
        findViewById<ImageView>(R.id.historico).setOnClickListener {
            startActivity(Intent(this, ListasPedidosActivity::class.java))
        }
    }

    private fun confirmarPedido() {
        val selecionados = listaProdutos.filter { it.selecionado }
        val total = selecionados.sumOf { it.preco }
        val pedido = Pedido(codigo = "", produtos = selecionados, total = total)

        lifecycleScope.launch {
            val newPedido = PedidoRepository.criarPedidoRetornando(pedido)
            AlertDialog.Builder(this@ItemOrderActivity)
                .setMessage("Pedido ${newPedido.codigo} confirmado com sucesso!")
                .setPositiveButton("OK") { dlg, _ -> dlg.dismiss() }
                .setCancelable(false)
                .show()

            // Limpa seleção e atualiza lista
            listaProdutos.forEach { it.selecionado = false }
            adapter.notifyDataSetChanged()
        }
    }
}