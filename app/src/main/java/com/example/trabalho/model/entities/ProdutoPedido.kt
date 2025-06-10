package com.example.trabalho.model.entities

import android.os.Parcel
import android.os.Parcelable

data class ProdutoPedido(
    val id: Long,
    val imagePath: String?,    // <-- novo campo
    val imagemResId: Int,
    val descricao: String,
    val preco: Double,
    val detalhes: String,
    var selecionado: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),              // id
        parcel.readString(),            // imagePath
        parcel.readInt(),               // imagemResId
        parcel.readString() ?: "",      // descricao
        parcel.readDouble(),            // preco
        parcel.readString() ?: "",      // detalhes
        parcel.readByte() != 0.toByte() // selecionado
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(imagePath)
        parcel.writeInt(imagemResId)
        parcel.writeString(descricao)
        parcel.writeDouble(preco)
        parcel.writeString(detalhes)
        parcel.writeByte(if (selecionado) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ProdutoPedido> {
        override fun createFromParcel(parcel: Parcel): ProdutoPedido = ProdutoPedido(parcel)
        override fun newArray(size: Int): Array<ProdutoPedido?> = arrayOfNulls(size)
    }
}
