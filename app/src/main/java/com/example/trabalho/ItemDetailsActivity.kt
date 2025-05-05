package com.example.trabalho

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        // Encontrar os botões pelo ID
        val btnSave = findViewById<ImageButton>(R.id.btnSave)
        val btnDelete = findViewById<ImageButton>(R.id.btnDelete)

        // Configurar o clique para o botão de Salvar
        btnSave.setOnClickListener {
            // Exibir o Toast ou Popup de sucesso
            showSaveSuccessDialog()
        }

        // Configurar o clique para o botão de Deletar
        btnDelete.setOnClickListener {
            // Exibir o Toast ou Popup de confirmação
            showDeleteSuccessDialog()
        }
    }

    // Função para mostrar o Popup de sucesso para salvar
    private fun showSaveSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Produto salvo com sucesso!")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    // Função para mostrar o Popup de sucesso para deletar
    private fun showDeleteSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Produto deletado com sucesso!")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
}
