package com.example.trabalho.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.R
import com.example.trabalho.model.entities.ProdutoPedido

class ProdutoDetalheAdapter(
    private val produtos: List<ProdutoPedido>
) : RecyclerView.Adapter<ProdutoDetalheAdapter.ProdutoViewHolder>() {

    inner class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView   = view.findViewById(R.id.nomeProduto)
        val preco: TextView  = view.findViewById(R.id.precoProduto)
        val imagem: ImageView= view.findViewById(R.id.imagemProduto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto_detalhe, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = produtos[position]

        holder.nome.text  = produto.descricao
        holder.preco.text = "R$ %.2f".format(produto.preco)

        // **Aqui também**: se vier path, carrega URI; senão o resource
        if (produto.imagePath != null) {
            holder.imagem.setImageURI(Uri.parse(produto.imagePath))
        } else {
            holder.imagem.setImageResource(produto.imagemResId)
        }
    }

    override fun getItemCount(): Int = produtos.size
}
