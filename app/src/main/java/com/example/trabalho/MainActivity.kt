package com.example.trabalho

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val priceTextView: TextView = findViewById(R.id.priceTextView)
        val itemImageView: ImageView = findViewById(R.id.itemImageView)

        fabAdd.setOnClickListener {
            val intent = Intent(this, ItemDetailsActivity::class.java)
            startActivity(intent)
        }

        // Exemplo de alteração de texto (opcional)
//        descriptionTextView.text = "Camiseta Estilosa"
//        priceTextView.text = "R$ 350,00"
//        itemImageView.setImageResource(R.drawable.shirt_image)
    }
}