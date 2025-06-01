package com.example.trabalho.view.adapter

import android.R.attr.background
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.R
import com.example.trabalho.model.entities.Pedido

class PedidoFeitoAdapter(
    private val listaPedidos: List<Pedido>,
    private val clickListener: (Pedido, Int) -> Unit
) : RecyclerView.Adapter<PedidoFeitoAdapter.PedidoFeitoViewHolder>() {

    inner class PedidoFeitoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusIndicator: View = view.findViewById(R.id.statusIndicator)
        val codigoPedido: TextView = view.findViewById(R.id.codigoPedido)
        val valorPedido: TextView = view.findViewById(R.id.valorPedido)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickListener(listaPedidos[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoFeitoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido_feito, parent, false)
        return PedidoFeitoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoFeitoViewHolder, position: Int) {
        val pedido = listaPedidos[position]

        holder.codigoPedido.text = pedido.codigo
        holder.valorPedido.text = "R$ %.2f".format(pedido.total)
        val cor = when (pedido.status.lowercase()) {
            "aberto" -> Color.GRAY
            "fechado" -> Color.GREEN
            "cancelado" -> Color.RED
            else -> Color.GRAY
        }

        // Cria um drawable circular com a cor correta e aplica como fundo
        val circleDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(cor)
            // Opcional: tamanho em pixels, converta dp para px se quiser
            // setSize(20, 20)
        }
        holder.statusIndicator.background = circleDrawable
    }

    override fun getItemCount(): Int = listaPedidos.size
}