package com.example.trabalho.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalho.R
import com.example.trabalho.model.entities.Produto
import java.text.NumberFormat
import java.util.Locale

class ItemDetailsActivity : AppCompatActivity() {

    private var selectedImageResId = -1
    private var produtoIndex: Int = -1
    private var produtoId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val imageSelectButton = findViewById<ImageView>(R.id.imageSelectButton)
        val editDescricao     = findViewById<EditText>(R.id.editDescricao)
        val editValor         = findViewById<EditText>(R.id.editValor)
        val editDetalhes      = findViewById<EditText>(R.id.editDetalhes)
        val btnSave           = findViewById<ImageButton>(R.id.btnSave)
        val btnDelete         = findViewById<ImageButton>(R.id.btnDelete)
        val btnBack           = findViewById<ImageButton>(R.id.btnBack)
        val tituloTextView    = findViewById<TextView>(R.id.titleTextView)

        // Formatação de moeda brasileira em tempo real
        val localeBR = Locale("pt", "BR")
        val currencyFormat = NumberFormat.getCurrencyInstance(localeBR)
        var current = ""

        editValor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.toString() == current) return
                editValor.removeTextChangedListener(this)

                // Remove tudo que não for dígito
                val cleanString = s.toString().replace(Regex("\\D"), "")
                if (cleanString.isNotEmpty()) {
                    val parsed = cleanString.toDouble()
                    val formatted = currencyFormat.format(parsed / 100)
                    current = formatted
                    editValor.setText(formatted)
                    editValor.setSelection(formatted.length)
                } else {
                    current = ""
                    editValor.setText("")
                }

                editValor.addTextChangedListener(this)
            }
        })

        // Placeholder inicial
        selectedImageResId = R.drawable.adicionar_imagem
        imageSelectButton.setImageResource(selectedImageResId)

        // Se for edição, carrega dados e formata valor
        val produtoEdit = intent.getSerializableExtra("produto") as? Produto
        produtoIndex    = intent.getIntExtra("index", -1)
        if (produtoEdit != null) {
            editDescricao.setText(produtoEdit.descricao)
            val formatted = currencyFormat.format(produtoEdit.valor)
            current = formatted
            editValor.setText(formatted)
            editDetalhes.setText(produtoEdit.detalhes)
            produtoId = produtoEdit.id

            selectedImageResId = produtoEdit.imagemResId
            imageSelectButton.setImageResource(selectedImageResId)

            tituloTextView.text = "Edição Produtos"
            btnDelete.visibility = View.VISIBLE
        } else {
            btnDelete.visibility = View.GONE
        }

        // Escolha de imagem
        imageSelectButton.setOnClickListener {
            val nomes = arrayOf("Camisa","Bermuda","Jeans","Chinelo")
            val ids   = arrayOf(
                R.drawable.shirt_image,
                R.drawable.bermuda_image,
                R.drawable.jeans_image,
                R.drawable.chinelo_image
            )
            AlertDialog.Builder(this)
                .setTitle("Escolha uma imagem")
                .setItems(nomes) { _, which ->
                    selectedImageResId = ids[which]
                    imageSelectButton.setImageResource(selectedImageResId)
                }
                .show()
        }

        // Salvar produto
        btnSave.setOnClickListener {
            val descricao = editDescricao.text.toString().trim()
            val valorText = editValor.text.toString().trim()
            val detalhes  = editDetalhes.text.toString().trim()

            // Validações
            if (descricao.isEmpty() || valorText.isEmpty() || detalhes.isEmpty() || selectedImageResId == -1) {
                Toast.makeText(this, "Preencha todas as informações do cadastro", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Converte valor formatado em número
            val raw = valorText.replace(Regex("[R\$\\s.]"), "").replace(",", ".")
            val valorDouble = raw.toDoubleOrNull()
            if (valorDouble == null || valorDouble <= 0.0) {
                Toast.makeText(this, "O valor do produto deve ser maior que zero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val produto = Produto(
                id = produtoId,
                descricao = descricao,
                valor = valorDouble,
                detalhes = detalhes,
                imagemResId = selectedImageResId
            )

            // Pop-up de sucesso bloqueando até OK
            AlertDialog.Builder(this)
                .setMessage("Produto salvo com sucesso!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    setResult(RESULT_OK, Intent().apply {
                        putExtra("produto", produto)
                        putExtra("index", produtoIndex)
                    })
                    finish()
                }
                .setCancelable(false)
                .show()
        }

        // Excluir produto
        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Excluir produto")
                .setMessage("Deseja realmente excluir este produto?")
                .setPositiveButton("Sim") { dialog, _ ->
                    dialog.dismiss()
                    AlertDialog.Builder(this)
                        .setMessage("Produto deletado com sucesso!")
                        .setPositiveButton("OK") { dlg, _ ->
                            dlg.dismiss()
                            Intent().also { intent ->
                                intent.putExtra("index", produtoIndex)
                                setResult(RESULT_FIRST_USER, intent)
                            }
                            finish()
                        }
                        .setCancelable(false)
                        .show()
                }
                .setNegativeButton("Não") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        // Voltar
        btnBack.setOnClickListener { finish() }
    }
}