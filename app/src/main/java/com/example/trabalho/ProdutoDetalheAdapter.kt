package com.example.trabalho

import Produto
import ProdutoPedido
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdutoDetalheAdapter(private val produtos: List<ProdutoPedido>) :
    RecyclerView.Adapter<ProdutoDetalheAdapter.ProdutoViewHolder>() {

    inner class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.nomeProduto)
        val preco: TextView = view.findViewById(R.id.precoProduto)
        val imagem: ImageView = view.findViewById(R.id.imagemProduto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto_detalhe, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = produtos[position]
        holder.nome.text = produto.descricao
        holder.preco.text = "R$ %.2f".format(produto.preco)
        holder.imagem.setImageResource(produto.imagemResId)
    }

    override fun getItemCount() = produtos.size
}

