import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.R

class PedidoAdapter(
    private val listaProdutos: List<ProdutoPedido>,
    private val onItemCheckedChange: (produto: ProdutoPedido, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.ProdutoViewHolder>() {

    inner class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagem: ImageView = view.findViewById(R.id.itemImageView)
        val descricao: TextView = view.findViewById(R.id.descriptionTextView)
        val preco: TextView = view.findViewById(R.id.priceTextView)
        val detalhes: TextView = view.findViewById(R.id.detailsTextView)
        val checkBox: CheckBox = view.findViewById(R.id.itemCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProdutos[position]

        holder.imagem.setImageResource(produto.imagemResId)
        holder.descricao.text = produto.descricao
        holder.preco.text = "R$ %.2f".format(produto.preco)
        holder.detalhes.text = produto.detalhes
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = produto.selecionado

        // Atualiza estado ao clicar no checkbox
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onItemCheckedChange(produto, isChecked)
        }
    }

    override fun getItemCount(): Int = listaProdutos.size

    fun getProdutos(): List<ProdutoPedido> = listaProdutos
}
