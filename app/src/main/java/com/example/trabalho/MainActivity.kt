package com.example.trabalho

import Produto
import ProdutoPedido
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_ADD = 1
        const val REQUEST_CODE_EDIT = 2
    }


    private lateinit var listaLayout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("pedidosFeitos") // Remove os pedidos feitos
        editor.remove("orderNumber")   // Zera a contagem dos pedidos, se quiser
        editor.apply()
        setContentView(R.layout.activity_main)


        carregarProdutos()

        listaLayout = findViewById(R.id.listaProdutosLayout)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        val textViewSemProdutos = findViewById<TextView>(R.id.textViewSemProdutos)

        if (listaLayout.childCount == 0) {
            textViewSemProdutos.visibility = View.VISIBLE
        } else {
            textViewSemProdutos.visibility = View.GONE
        }

        fabAdd.setOnClickListener {
            val intent = Intent(this, ItemDetailsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD)

        }
        val iconCarrinho = findViewById<ImageView>(R.id.carrinho)
        iconCarrinho.setOnClickListener {
            val listaProdutoPedido = ProdutoRepository.produtos.map { produto ->
                ProdutoPedido(
                    imagemResId = produto.imagemResId,
                    descricao = produto.descricao,
                    preco = produto.valor,
                    detalhes = produto.detalhes,
                    selecionado = false
                )
            }

            val intent = Intent(this, ItemOrderActivity::class.java)
            intent.putParcelableArrayListExtra("produtos", ArrayList(listaProdutoPedido))
            intent.putExtra("modo", "pedido")
            startActivity(intent)
        }
        val iconHistorico = findViewById<ImageView>(R.id.historico)
        iconHistorico.setOnClickListener {
            val intent = Intent(this, ListasPedidosActivity::class.java)
            startActivity(intent)
        }
        atualizarLista()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_FIRST_USER) {
            val index = data?.getIntExtra("index", -1) ?: -1
            if (index >= 0 && index < ProdutoRepository.produtos.size) {
                ProdutoRepository.produtos.removeAt(index)
                salvarProdutos()
            }
            atualizarLista()
            return
        }

        if (resultCode == RESULT_OK) {
            val produto = data?.getSerializableExtra("produto") as? Produto
            val index = data?.getIntExtra("index", -1) ?: -1

            if (requestCode == REQUEST_CODE_ADD && produto != null) {
                ProdutoRepository.produtos.add(produto)
            } else if (requestCode == REQUEST_CODE_EDIT && produto != null && index >= 0) {
                ProdutoRepository.produtos[index] = produto
            }
            salvarProdutos()
            atualizarLista()
        }
    }

    private fun atualizarLista() {
        listaLayout.removeAllViews()
        ProdutoRepository.produtos.forEachIndexed { index, produto ->
            val card = layoutInflater.inflate(R.layout.item_produto, null)

            val descricao = card.findViewById<TextView>(R.id.descriptionTextView)
            val valor = card.findViewById<TextView>(R.id.priceTextView)
            val detalhes = card.findViewById<TextView>(R.id.detailsTextView)
            val imagem = card.findViewById<ImageView>(R.id.itemImageView)

            descricao.text = produto.descricao
            valor.text = "R$ ${produto.valor}"
            detalhes.text = produto.detalhes
            imagem.setImageResource(produto.imagemResId)

            // Clique para editar
            card.setOnClickListener {
                val intent = Intent(this, ItemDetailsActivity::class.java)
                intent.putExtra("produto", produto)
                intent.putExtra("index", index)
                startActivityForResult(intent, REQUEST_CODE_EDIT)
            }

            listaLayout.addView(card)
        }
    }


    private fun salvarProdutos() {
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val gson = Gson()
        val produtosJson = gson.toJson(ProdutoRepository.produtos)
        val editor = sharedPreferences.edit()
        editor.putString("produtos", produtosJson)
        editor.apply()
    }

    private fun carregarProdutos() {
        val sharedPreferences = getSharedPreferences("pedidos", Context.MODE_PRIVATE)
        val gson = Gson()
        val produtosJson = sharedPreferences.getString("produtos", "[]")
        val type = object : TypeToken<List<Produto>>() {}.type
        val produtos: List<Produto> = gson.fromJson(produtosJson, type)
        ProdutoRepository.produtos.clear()
        ProdutoRepository.produtos.addAll(produtos)
    }

    override fun onResume() {
        super.onResume()
        atualizarLista()
    }
}
