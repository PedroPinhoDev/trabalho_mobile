package com.example.trabalho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ListasPedidosActivity : AppCompatActivity() {

    private lateinit var recyclerViewPedidos: RecyclerView
    private lateinit var adapter: PedidoFeitoAdapter
    private lateinit var textViewVazio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listas_pedidos)

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos)
        textViewVazio = findViewById(R.id.textViewVazio)

        adapter = PedidoFeitoAdapter(PedidoRepository.pedidos) { pedido, _ ->
            val intent = Intent(this, DetalhesPedidoActivity::class.java)
            intent.putExtra(DetalhesPedidoActivity.EXTRA_PEDIDO_ID, pedido.id)
            startActivity(intent)  // não precisa de startActivityForResult
        }

        recyclerViewPedidos.layoutManager = LinearLayoutManager(this)
        recyclerViewPedidos.adapter = adapter

        lifecycleScope.launch {
            PedidoRepository.carregarPedidos()
            adapter.notifyDataSetChanged()
            textViewVazio.visibility = if (PedidoRepository.pedidos.isEmpty()) View.VISIBLE else View.GONE
        }

        findViewById<ImageView>(R.id.home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.carrinho).setOnClickListener {
            startActivity(Intent(this, ItemOrderActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            lifecycleScope.launch {
                PedidoRepository.carregarPedidos()
                adapter.notifyDataSetChanged()
                textViewVazio.visibility = if (PedidoRepository.pedidos.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}