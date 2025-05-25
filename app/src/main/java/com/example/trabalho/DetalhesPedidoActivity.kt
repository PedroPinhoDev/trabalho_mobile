package com.example.trabalho

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.R
import com.example.trabalho.utils.toPedido
import kotlinx.coroutines.launch

class DetalhesPedidoActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PEDIDO_ID = "pedidoId"
    }

    private val TAG = "DetalhesPedido"
    private var pedidoId: Long = -1
    private lateinit var recyclerView: RecyclerView
    // adapter, views etc.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalhes_pedidos)

        val pedidoId = intent.getLongExtra(EXTRA_PEDIDO_ID, -1L)
        if (pedidoId < 0) {
            Toast.makeText(this, "ID de pedido inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                val pedido = PedidoRepository.buscarPedidoPorId(pedidoId)
                mostraPedido(pedido)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao buscar pedido $pedidoId", e)
                Toast.makeText(this@DetalhesPedidoActivity,
                    "Falha ao carregar pedido", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }



    private fun mostraPedido(pedido: Pedido) {
        // Preenche título, total, lista de produtos etc.
        findViewById<TextView>(R.id.textCodigoPedido).text = pedido.codigo
        // …
        recyclerView.adapter = ProdutoDetalheAdapter(pedido.produtos)
        findViewById<TextView>(R.id.textTotal).text = "R$ %.2f".format(pedido.total)
        // configurar botão de status, menu etc.
    }
}
