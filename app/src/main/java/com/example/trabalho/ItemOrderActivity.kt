package com.example.trabalho

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ItemOrderActivity : AppCompatActivity() {

    private lateinit var popup: Button
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_order)

        popup = findViewById(R.id.btnConfirmarPedido)

        popup.setOnClickListener {
            dialog = Dialog(this)
            dialog.setContentView(R.layout.activity_popup_confirmar_pedido)
            dialog.setCancelable(true)
            dialog.show()

            // Fecha o dialog após 3 segundos (3000 milissegundos)
            android.os.Handler().postDelayed({
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }, 3000)
        }
    }
}
