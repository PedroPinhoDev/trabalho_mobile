package com.example.trabalho

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ItemDetailsActivity : AppCompatActivity() {

    private var selectedImageResId = R.drawable.shirt_image
    private var produtoIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)


        val imageSelectButton = findViewById<ImageView>(R.id.imageSelectButton)
        val editDescricao = findViewById<EditText>(R.id.editDescricao)
        val editValor = findViewById<EditText>(R.id.editValor)
        val editDetalhes = findViewById<EditText>(R.id.editDetalhes)
        val btnSave = findViewById<ImageButton>(R.id.btnSave)
        val btnDelete = findViewById<ImageButton>(R.id.btnDelete)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // Se for edição
        val produtoEdit = intent.getSerializableExtra("produto") as? Produto
        produtoIndex = intent.getIntExtra("index", -1)

        if (produtoEdit != null) {
            editDescricao.setText(produtoEdit.descricao)
            editValor.setText(produtoEdit.valor)
            editDetalhes.setText(produtoEdit.detalhes)
            selectedImageResId = produtoEdit.ImagemResId
            imageSelectButton.setImageResource(selectedImageResId)
        }

        imageSelectButton.setOnClickListener {
            val images = arrayOf("Camisa", "Bermuda", "Jeans", "Chinelo")
            val imageIds = arrayOf(R.drawable.shirt_image, R.drawable.bermuda_image, R.drawable.jeans_image, R.drawable.chinelo_image)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Escolha uma imagem")
            builder.setItems(images) { _, which ->
                selectedImageResId = imageIds[which]
                imageSelectButton.setImageResource(selectedImageResId)
            }
            builder.show()
        }

        btnSave.setOnClickListener {
            val descricao = editDescricao.text.toString().trim()
            val valor = editValor.text.toString().trim()
            val detalhes = editDetalhes.text.toString().trim()

            if (descricao.isEmpty() || valor.isEmpty() || detalhes.isEmpty()) {
                Toast.makeText(this, "Preencha todas as informações do cadastro", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmação")
            builder.setMessage("Deseja adicionar esse produto na lista de produtos?")

            builder.setPositiveButton("Sim") { dialog, _ ->
                val produto = Produto(descricao, valor, detalhes, selectedImageResId)

                val intent = Intent()
                intent.putExtra("produto", produto)
                intent.putExtra("index", produtoIndex)
                setResult(RESULT_OK, intent)
                dialog.dismiss()
                finish()
            }

            builder.setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }

        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Excluir produto")
            builder.setMessage("Deseja realmente excluir este produto?")
            builder.setPositiveButton("Sim") { dialog, _ ->
                val intent = Intent()
                intent.putExtra("index", produtoIndex)
                setResult(RESULT_FIRST_USER, intent) // RESULT_FIRST_USER = código customizado
                dialog.dismiss()
                finish()
            }
            builder.setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showDeleteSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Produto deletado com sucesso!")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}
