package com.example.trabalho.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.R
import com.example.trabalho.model.entities.Pedido
import com.example.trabalho.repository.PedidoRepository
import com.example.trabalho.view.adapter.ProdutoDetalheAdapter
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
        findViewById<TextView>(R.id.textCodigoPedido).text = pedido.codigo
        recyclerView.adapter = ProdutoDetalheAdapter(pedido.produtos)
        findViewById<TextView>(R.id.textTotal).text = "R$ %.2f".format(pedido.total)

        val botaoStatus = findViewById<Button>(R.id.botaoStatus)
        botaoStatus.text = "Status: ${pedido.status}"  // Exibe o status atual

        botaoStatus.setOnClickListener { view ->
            val popup = android.widget.PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.status_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                val novoStatus = when (menuItem.itemId) {
                    R.id.statusAberto -> "Aberto"
                    R.id.statusFechado -> "Fechado"
                    R.id.statusCancelado -> "Cancelado"
                    else -> null
                }

                if (novoStatus != null) {
                    botaoStatus.text = "Status: $novoStatus"
                    atualizarStatusPedido(pedido, novoStatus)

                    // Retorna resultado para a activity que chamou:
                    val intent = intent
                    intent.putExtra("statusAlterado", true)
                    intent.putExtra("novoStatus", novoStatus)
                    setResult(RESULT_OK, intent)

                    Toast.makeText(this, "Status alterado para $novoStatus", Toast.LENGTH_SHORT).show()
                    true
                } else {
                    false
                }
            }

            popup.show()
        }
    }

    private fun atualizarStatusPedido(pedido: Pedido, novoStatus: String) {
        lifecycleScope.launch {
            try {
                val pedidoAtualizado = pedido.copy(status = novoStatus)
                PedidoRepository.atualizarPedido(pedidoAtualizado) // Chama o repositório (já existente)
                // Atualiza a UI com o pedido atualizado
                mostraPedido(pedidoAtualizado)
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesPedidoActivity, "Erro ao atualizar status", Toast.LENGTH_SHORT).show()
            }
        }
    }


}