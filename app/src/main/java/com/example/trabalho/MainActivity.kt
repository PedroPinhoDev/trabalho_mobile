package com.example.trabalho

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_ADD = 1
        const val REQUEST_CODE_EDIT = 2
    }

    private val produtos = mutableListOf<Produto>()
    private lateinit var listaLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listaLayout = findViewById(R.id.listaProdutosLayout)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        fabAdd.setOnClickListener {
            val intent = Intent(this, ItemDetailsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_FIRST_USER) {
            val index = data?.getIntExtra("index", -1) ?: -1
            if (index >= 0 && index < produtos.size) {
                produtos.removeAt(index)
            }
            atualizarLista()
            return
        }

        if (resultCode == RESULT_OK) {
            val produto = data?.getSerializableExtra("produto") as? Produto
            val index = data?.getIntExtra("index", -1) ?: -1

            if (requestCode == REQUEST_CODE_ADD && produto != null) {
                produtos.add(produto)
            } else if (requestCode == REQUEST_CODE_EDIT && produto != null && index >= 0) {
                produtos[index] = produto
            }

            atualizarLista()
        }
    }

    private fun atualizarLista() {
        listaLayout.removeAllViews()
        produtos.forEachIndexed { index, produto ->
            val card = layoutInflater.inflate(R.layout.item_produto, null)

            val descricao = card.findViewById<TextView>(R.id.descriptionTextView)
            val valor = card.findViewById<TextView>(R.id.priceTextView)
            val detalhes = card.findViewById<TextView>(R.id.detailsTextView)
            val imagem = card.findViewById<ImageView>(R.id.itemImageView)

            descricao.text = produto.descricao
            valor.text = "R$ ${produto.valor}"
            detalhes.text = produto.detalhes
            imagem.setImageResource(produto.ImagemResId)

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
}