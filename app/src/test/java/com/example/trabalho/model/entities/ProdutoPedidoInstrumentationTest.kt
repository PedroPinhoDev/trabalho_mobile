//package com.example.trabalho.model.entities
//
//import android.os.Parcel
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import org.junit.Assert.*
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class ProdutoPedidoInstrumentationTest {
//
//    private val original = ProdutoPedido(
//        id = 7L,
//        imagePath = "/path",
//        imagemResId = 99,
//        descricao = "Item",
//        preco = 12.34,
//        detalhes = "Detalhes",
//        selecionado = true
//    )
//
//    @Test fun parcelable_roundTrip() {
//        val p = Parcel.obtain().apply {
//            original.writeToParcel(this, 0)
//            setDataPosition(0)
//        }
//        val recreated = ProdutoPedido.CREATOR.createFromParcel(p)
//        assertEquals(original, recreated)
//        p.recycle()
//    }
//
//    @Test fun newArray_size() {
//        val arr = ProdutoPedido.CREATOR.newArray(3)
//        assertNotNull(arr)
//        assertEquals(3, arr.size)
//    }
//}
