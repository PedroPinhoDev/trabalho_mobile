import android.os.Parcel
import android.os.Parcelable

data class ProdutoPedido(
    val imagemResId: Int,
    val descricao: String,
    val preco: Double,
    val detalhes: String,
    var selecionado: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        imagemResId = parcel.readInt(),
        descricao = parcel.readString() ?: "",
        preco = parcel.readDouble(),
        detalhes = parcel.readString() ?: "",
        selecionado = parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imagemResId)
        parcel.writeString(descricao)
        parcel.writeDouble(preco)
        parcel.writeString(detalhes)
        parcel.writeByte(if (selecionado) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProdutoPedido> {
        override fun createFromParcel(parcel: Parcel): ProdutoPedido {
            return ProdutoPedido(parcel)
        }

        override fun newArray(size: Int): Array<ProdutoPedido?> {
            return arrayOfNulls(size)
        }
    }
}
