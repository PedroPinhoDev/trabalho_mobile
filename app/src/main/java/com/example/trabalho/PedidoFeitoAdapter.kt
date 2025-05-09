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
    private val listaPedidos: ArrayList<ProdutoPedido>
) : RecyclerView.Adapter<PedidoFeitoAdapter.PedidoFeitoViewHolder>() {

    inner class PedidoFeitoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusIndicator: View = view.findViewById(R.id.statusIndicator)
        val codigoPedido: TextView = view.findViewById(R.id.codigoPedido)
        val valorPedido: TextView = view.findViewById(R.id.valorPedido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoFeitoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido_feito, parent, false) // O layout do item pedido
        return PedidoFeitoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoFeitoViewHolder, position: Int) {
        val pedido = listaPedidos[position]

        holder.codigoPedido.text = "P-${position + 1}"  // Aqui estou assumindo que o código do pedido seja "P-" seguido do índice + 1, mas você pode modificar conforme sua lógica

        // Define o valor do pedido
        holder.valorPedido.text = "R$ %.2f".format(pedido.preco)
        
        // Defina a cor do status (se desejado) ou qualquer outro efeito
        if (pedido.selecionado) {
            holder.statusIndicator.setBackgroundColor(Color.GREEN) // Exemplo de status 'confirmado'
        } else {
            holder.statusIndicator.setBackgroundColor(Color.RED) // Exemplo de status 'pendente'
        }
    }

    override fun getItemCount(): Int = listaPedidos.size
}

