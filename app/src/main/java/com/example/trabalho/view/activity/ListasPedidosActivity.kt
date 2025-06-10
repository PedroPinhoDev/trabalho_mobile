package com.example.trabalho.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.R
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.repository.PedidoRepository
import com.example.trabalho.repository.ProdutoRepository
import com.example.trabalho.view.adapter.PedidoFeitoAdapter
import kotlinx.coroutines.launch

class ListasPedidosActivity : AppCompatActivity() {

    private lateinit var recyclerViewPedidos: RecyclerView
    private lateinit var adapter: PedidoFeitoAdapter
    private lateinit var textViewVazio: TextView

    companion object {
        private const val REQUEST_CODE_DETALHE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listas_pedidos)

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos)
        textViewVazio       = findViewById(R.id.textViewVazio)

        // Adapter de pedidos já criados
        adapter = PedidoFeitoAdapter(PedidoRepository.pedidos) { pedido, position ->
            val intent = Intent(this, DetalhesPedidoActivity::class.java)
            intent.putExtra(DetalhesPedidoActivity.EXTRA_PEDIDO_ID, pedido.id)
            startActivityForResult(intent, REQUEST_CODE_DETALHE)
        }

        // Botão carrinho: cria lista de ProdutoPedido incluindo imagePath
        findViewById<ImageView>(R.id.carrinho).setOnClickListener {
            val listaPedido = ProdutoRepository.produtos.map { produto ->
                ProdutoPedido(
                    id          = produto.id!!,
                    imagePath   = produto.imagePath,        // <— aqui
                    imagemResId = produto.imagemResId,
                    descricao   = produto.descricao,
                    preco       = produto.valor,
                    detalhes    = produto.detalhes
                )
            }
            Intent(this, ItemOrderActivity::class.java).also { intent ->
                intent.putParcelableArrayListExtra("produtos", ArrayList(listaPedido))
                startActivity(intent)
            }
        }

        recyclerViewPedidos.layoutManager = LinearLayoutManager(this)
        recyclerViewPedidos.adapter        = adapter

        lifecycleScope.launch {
            PedidoRepository.carregarPedidos()
            adapter.notifyDataSetChanged()
            textViewVazio.visibility =
                if (PedidoRepository.pedidos.isEmpty()) View.VISIBLE else View.GONE
        }

        findViewById<ImageView>(R.id.home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETALHE && resultCode == RESULT_OK && data != null) {
            val pedidoId  = data.getLongExtra("pedidoId", -1L)
            val novoStatus= data.getStringExtra("novoStatus")

            if (pedidoId != -1L && novoStatus != null) {
                val idx = PedidoRepository.pedidos.indexOfFirst { it.id == pedidoId }
                if (idx != -1) {
                    PedidoRepository.pedidos[idx].status = novoStatus
                    adapter.notifyItemChanged(idx)
                }
            }
        }
    }
}
