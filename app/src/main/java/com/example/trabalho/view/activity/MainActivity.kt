package com.example.trabalho.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trabalho.R
import com.example.trabalho.model.entities.Produto
import com.example.trabalho.model.entities.ProdutoPedido
import com.example.trabalho.repository.ProdutoRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_ADD = 1
        const val REQUEST_CODE_EDIT = 2
    }

    private lateinit var listaLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inicializa o repositório (carrega SharedPreferences)
        ProdutoRepository.init(this)

        setContentView(R.layout.activity_main)
        listaLayout = findViewById(R.id.listaProdutosLayout)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        val textViewSemProdutos = findViewById<TextView>(R.id.textViewSemProdutos)

        fabAdd.setOnClickListener {
            startActivityForResult(
                Intent(this, ItemDetailsActivity::class.java),
                REQUEST_CODE_ADD
            )
        }

        findViewById<ImageView>(R.id.carrinho).setOnClickListener {
            val listaPedido = ProdutoRepository.produtos.map { produto ->
                ProdutoPedido(
                    id          = produto.id!!,
                    imagePath   = produto.imagePath,
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

        findViewById<ImageView>(R.id.historico).setOnClickListener {
            startActivity(Intent(this, ListasPedidosActivity::class.java))
        }

        lifecycleScope.launch {
            ProdutoRepository.carregarProdutos()
            atualizarLista()
            textViewSemProdutos.visibility =
                if (ProdutoRepository.produtos.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val produto = data?.getSerializableExtra("produto") as? Produto
        val index   = data?.getIntExtra("index", -1) ?: -1

        if (resultCode == RESULT_OK && produto != null) {
            lifecycleScope.launch {
                if (requestCode == REQUEST_CODE_ADD) {
                    ProdutoRepository.adicionarProduto(this@MainActivity, produto)
                } else if (requestCode == REQUEST_CODE_EDIT && index >= 0) {
                    ProdutoRepository.atualizarProduto(index, produto)
                }
                atualizarLista()
            }
        } else if (resultCode == RESULT_FIRST_USER && index >= 0) {
            lifecycleScope.launch {
                ProdutoRepository.excluirProduto(index)
                atualizarLista()
            }
        }
    }

    private fun atualizarLista() {
        listaLayout.removeAllViews()
        ProdutoRepository.produtos.forEachIndexed { i, produto ->
            val card = layoutInflater.inflate(R.layout.item_produto, null)
            card.findViewById<TextView>(R.id.descriptionTextView).text = produto.descricao
            card.findViewById<TextView>(R.id.priceTextView).text =
                "R$ %.2f".format(produto.valor)
            card.findViewById<TextView>(R.id.detailsTextView).text = produto.detalhes

            val imageView = card.findViewById<ImageView>(R.id.itemImageView)
            produto.imagePath?.let { imageView.setImageURI(Uri.parse(it)) }
                ?: imageView.setImageResource(produto.imagemResId)

            card.setOnClickListener {
                Intent(this, ItemDetailsActivity::class.java).also {
                    it.putExtra("produto", produto)
                    it.putExtra("index", i)
                    startActivityForResult(it, REQUEST_CODE_EDIT)
                }
            }
            listaLayout.addView(card)
        }
    }
}
