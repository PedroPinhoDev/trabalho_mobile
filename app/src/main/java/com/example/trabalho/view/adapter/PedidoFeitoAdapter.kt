package com.example.trabalho.view.adapter

import android.graphics.Color
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
        val setaIndicativa: ImageView = view.findViewById(R.id.setaIndicativa)

        init {
            setaIndicativa.setOnClickListener {
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
        when (pedido.status.lowercase()) {
            "aberto" -> holder.statusIndicator.setBackgroundColor(Color.GRAY)
            "fechado" -> holder.statusIndicator.setBackgroundColor(Color.GREEN)
            "cancelado" -> holder.statusIndicator.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount(): Int = listaPedidos.size
}