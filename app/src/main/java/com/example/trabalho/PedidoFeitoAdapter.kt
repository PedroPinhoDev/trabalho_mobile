package com.example.trabalho

import ProdutoPedido
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView

class PedidoFeitoAdapter(
    private val listaPedidos: ArrayList<ProdutoPedido>,
    private val clickListener: (ProdutoPedido, Int) -> Unit
) : RecyclerView.Adapter<PedidoFeitoAdapter.PedidoFeitoViewHolder>() {

    inner class PedidoFeitoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusIndicator: View = view.findViewById(R.id.statusIndicator)
        val codigoPedido: TextView = view.findViewById(R.id.codigoPedido)
        val valorPedido: TextView = view.findViewById(R.id.valorPedido)

        init {
            view.setOnClickListener {
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

        // Exibe o código do pedido, como 'P-1', 'P-2', etc.
        holder.codigoPedido.text = "P-${position + 1}"

        // Exibe o valor do pedido com formatação monetária
        holder.valorPedido.text = "R$ %.2f".format(pedido.preco)

        // Define a cor do status do pedido (verde para confirmado, vermelho para pendente)
        if (pedido.selecionado) {
            holder.statusIndicator.setBackgroundColor(Color.GREEN) // Status 'confirmado'
        } else {
            holder.statusIndicator.setBackgroundColor(Color.RED) // Status 'pendente'
        }
    }

    override fun getItemCount(): Int = listaPedidos.size
}


