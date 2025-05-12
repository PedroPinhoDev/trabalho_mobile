package com.example.trabalho

import Produto
import ProdutoPedido
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetalhesPedidoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalhes_pedidos)

        val codigoPedido = intent.getStringExtra("codigo") ?: "Pedido"
        val pedidoJson = intent.getStringExtra("pedidoSelecionado")
        val pedido = Gson().fromJson(pedidoJson, Pedido::class.java)
        val produtos = pedido.produtos

        val titulo = findViewById<TextView>(R.id.textCodigoPedido)
        titulo.text = codigoPedido

        // Configura o RecyclerView para exibir a lista de produtos
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProdutoDetalheAdapter(produtos)

        // Calcula o valor total
        val total = produtos.sumOf { it.preco }

        // Exibe o valor total formatado
        val totalView = findViewById<TextView>(R.id.textTotal)
        totalView.text = "R$ %.2f".format(total)

        val statusButton = findViewById<Button>(R.id.botaoStatus)
        statusButton.setOnClickListener {
            // Criar o PopupMenu
            val popupMenu = PopupMenu(this, it)
            menuInflater.inflate(R.menu.status_menu, popupMenu.menu)  // Referência do menu XML

            // Mostrar o menu
            popupMenu.show()

            // Lidar com as opções do menu
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.statusAberto -> {
                        // Alterar status para "Aberto"
                        alterarStatus(pedido, "aberto")
                        true
                    }
                    R.id.statusFechado -> {
                        // Alterar status para "Fechado"
                        alterarStatus(pedido, "fechado")
                        true
                    }
                    R.id.statusCancelado -> {
                        // Alterar status para "Cancelado"
                        alterarStatus(pedido, "cancelado")
                        true
                    }
                    else -> false
                }
            }
        }
    }

    // Função para alterar o status
    private fun alterarStatus(pedido: Pedido, status: String) {
        pedido.status = status
        val intent = Intent()
        intent.putExtra("pedidoAlterado", Gson().toJson(pedido))
        setResult(RESULT_OK, intent)
        finish()
        // Atualizar o RecyclerView ou fazer outras ações conforme necessário
        // Por exemplo, atualizar a cor do indicador de status
    }
}

