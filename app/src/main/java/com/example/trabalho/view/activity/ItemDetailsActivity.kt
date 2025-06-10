package com.example.trabalho.view.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.trabalho.R
import com.example.trabalho.model.entities.Produto
import java.io.File
import java.text.NumberFormat
import java.util.Locale

class ItemDetailsActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 100
        private const val REQUEST_CODE_STORAGE_PERMISSION = 200
    }

    private var selectedImageResId = R.drawable.adicionar_imagem
    private var selectedImageUri: Uri? = null
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

        // Formatação de moeda brasileira
        val localeBR = Locale("pt", "BR")
        val currencyFormat = NumberFormat.getCurrencyInstance(localeBR)
        var current = ""
        editValor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.toString() == current) return
                editValor.removeTextChangedListener(this)
                val clean = s.toString().replace("\\D+".toRegex(), "")
                if (clean.isNotEmpty()) {
                    val parsed = clean.toDouble()
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

        // Se for edição, carrega dados existentes
        val produtoEdit = intent.getSerializableExtra("produto") as? Produto
        produtoIndex    = intent.getIntExtra("index", -1)
        if (produtoEdit != null) {
            editDescricao.setText(produtoEdit.descricao)
            editValor.setText(currencyFormat.format(produtoEdit.valor))
            editDetalhes.setText(produtoEdit.detalhes)
            produtoId = produtoEdit.id
            if (produtoEdit.imagePath != null) {
                selectedImageUri = Uri.parse(produtoEdit.imagePath)
                imageSelectButton.setImageURI(selectedImageUri)
            } else {
                selectedImageResId = produtoEdit.imagemResId
                imageSelectButton.setImageResource(selectedImageResId)
            }
            tituloTextView.text = "Edição Produto"
            btnDelete.visibility = View.VISIBLE
        } else {
            btnDelete.visibility = View.GONE
            imageSelectButton.setImageResource(selectedImageResId)
        }

        // Escolha de imagem: Galeria ou recurso interno
        imageSelectButton.setOnClickListener {
            val options = arrayOf("Galeria", "Camisa", "Bermuda", "Jeans", "Chinelo")
            val resourceIds = arrayOf(-1,
                R.drawable.shirt_image,
                R.drawable.bermuda_image,
                R.drawable.jeans_image,
                R.drawable.chinelo_image
            )
            AlertDialog.Builder(this)
                .setTitle("Escolha uma imagem")
                .setItems(options) { _, which ->
                    if (which == 0) requestStoragePermissionOrPick()
                    else {
                        selectedImageResId = resourceIds[which]
                        selectedImageUri = null
                        imageSelectButton.setImageResource(selectedImageResId)
                    }
                }
                .show()
        }

        // Botão Salvar
        btnSave.setOnClickListener {
            val desc    = editDescricao.text.toString().trim()
            val valText = editValor.text.toString().trim()
            val det     = editDetalhes.text.toString().trim()
            if (desc.isEmpty() || valText.isEmpty() || det.isEmpty()) {
                Toast.makeText(this, "Preencha todas as informações", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val raw = valText.replace("[R$\\s.]".toRegex(), "").replace(",", ".")
            val valor = raw.toDoubleOrNull()
            if (valor == null || valor <= 0.0) {
                Toast.makeText(this, "Valor deve ser maior que zero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val produto = Produto(
                id          = produtoId,
                descricao   = desc,
                valor       = valor,
                detalhes    = det,
                imagePath   = selectedImageUri?.toString(),
                imagemResId = selectedImageResId
            )

            AlertDialog.Builder(this)
                .setMessage("Produto salvo com sucesso!")
                .setPositiveButton("OK") { dialogInterface: DialogInterface, _ ->
                    dialogInterface.dismiss()
                    setResult(RESULT_OK, Intent().apply {
                        putExtra("produto", produto)
                        putExtra("index", produtoIndex)
                    })
                    finish()
                }
                .setCancelable(false)
                .create()
                .show()
        }

        // Botão Excluir
        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Excluir produto?")
                .setMessage("Deseja realmente excluir este produto?")
                .setPositiveButton("Sim") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    setResult(RESULT_FIRST_USER, Intent().apply {
                        putExtra("index", produtoIndex)
                    })
                    finish()
                }
                .setNegativeButton("Não") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .create()
                .show()
        }

        // Botão Voltar
        btnBack.setOnClickListener { finish() }
    }

    private fun requestStoragePermissionOrPick() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13+ precisa de READ_MEDIA_IMAGES
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        REQUEST_CODE_STORAGE_PERMISSION
                    )
                } else {
                    pickImageFromGallery()
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                // Android 6–12 usa READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE_STORAGE_PERMISSION
                    )
                } else {
                    pickImageFromGallery()
                }
            }
            else -> {
                // Android 5.1 e anteriores não precisam de permissão em runtime
                pickImageFromGallery()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            pickImageFromGallery()
        } else {
            Toast.makeText(this,
                "Permissão negada para acessar a galeria",
                Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("DEPRECATION")
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                val filename = "imagem_${System.currentTimeMillis()}.jpg"
                contentResolver.openInputStream(uri)?.use { input ->
                    openFileOutput(filename, MODE_PRIVATE).use { output ->
                        input.copyTo(output)
                    }
                }
                selectedImageUri = Uri.fromFile(File(filesDir, filename))
                findViewById<ImageView>(R.id.imageSelectButton)
                    .setImageURI(selectedImageUri)
                selectedImageResId = R.drawable.adicionar_imagem
            }
        }
    }
}
