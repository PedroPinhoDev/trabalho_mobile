package com.example.trabalho

import PedidoAdapter
import ProdutoPedido
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class ItemOrderActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PedidoAdapter
    private var orderNumber = 1
    private var listaProdutos: ArrayList<ProdutoPedido> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_order)

        val homeIcon = findViewById<ImageView>(R.id.home)
        homeIcon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // opcional: remove a tela atual da pilha
        }

        recyclerView = findViewById(R.id.recyclerViewProdutos)  // Referência ao RecyclerView

        // Obtenção dos produtos passados pela MainActivity
        listaProdutos = intent.getParcelableArrayListExtra<ProdutoPedido>("produtos") ?: arrayListOf()

        // Configuração do LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)  // Configura como lista vertical

        // Configuração do Adapter
        adapter = PedidoAdapter(listaProdutos) { produto, isChecked ->
            if (isChecked) {
                // Produto foi selecionado
            } else {
                // Produto foi desmarcado
            }


        }


        val btnConfirmarPedido: MaterialButton = findViewById(R.id.btnConfirmarPedido)

        btnConfirmarPedido.setOnClickListener {
            if (nenhumItemSelecionado()) {
                // Mostrar alerta caso nenhum item tenha sido selecionado
                mostrarAlerta("Nenhum item selecionado!")
            } else {
                // Confirmar o pedido
                confirmarPedido()
            }
        }
        // Associando o adapter ao recyclerView
        recyclerView.adapter = adapter
    }

    private fun confirmarPedido() {
        // Criar o texto da mensagem do pedido
        val mensagem = "Pedido $orderNumber confirmado com sucesso!"

        // Criar o AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setMessage(mensagem)
            .setPositiveButton("OK") { dialog, _ ->
                // Fechar o dialog
                dialog.dismiss()
                // Incrementar o número do pedido para o próximo
                orderNumber++
            }

        // Mostrar o pop-up
        builder.create().show()

        for (produto in adapter.getProdutos()) {
            produto.selecionado = false
        }

// Atualizar a UI
        adapter.notifyDataSetChanged()
    }

    fun nenhumItemSelecionado(): Boolean {
        for (produtoPedido in listaProdutos) {
            if (produtoPedido.selecionado) {
                return false
            }
        }
        return true
    }

    private fun mostrarAlerta(mensagem: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(mensagem)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}
