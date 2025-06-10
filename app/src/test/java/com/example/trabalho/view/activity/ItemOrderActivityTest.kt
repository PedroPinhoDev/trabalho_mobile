//package com.example.trabalho.view.activity
//
//import android.content.Intent
//import androidx.appcompat.app.AlertDialog
//import androidx.recyclerview.widget.RecyclerView
//import androidx.test.core.app.ApplicationProvider
//import com.example.trabalho.R
//import com.example.trabalho.model.entities.Pedido
//import com.example.trabalho.model.entities.ProdutoPedido
//import com.example.trabalho.repository.PedidoRepository
//import com.google.android.material.button.MaterialButton
//import io.mockk.coEvery
//import io.mockk.mockkObject
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertNotNull
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.Robolectric
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.Shadows.shadowOf
//import org.robolectric.shadows.ShadowAlertDialog
//
//@RunWith(RobolectricTestRunner::class)
//class ItemOrderActivityTest {
//
//    @Before
//    fun setup() {
//        mockkObject(PedidoRepository)
//    }
//
//    @Test
//    fun `com produtos na intent, exibe a lista no RecyclerView`() {
//        // Given: Uma lista de produtos para passar na intent
//        val produtos = arrayListOf(
//            ProdutoPedido(1L, null, R.drawable.shirt_image, "Camisa", 50.0, "Algodão")
//        )
//        val intent = Intent(ApplicationProvider.getApplicationContext(), ItemOrderActivity::class.java).apply {
//            putParcelableArrayListExtra("produtos", produtos)
//        }
//
//        // When: A activity é criada
//        val scenario = Robolectric.buildActivity(ItemOrderActivity::class.java, intent).create().resume().visible()
//        val activity = scenario.get()
//
//        // Then: O RecyclerView tem o número correto de itens
//        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerViewProdutos)
//        assertNotNull(recyclerView.adapter)
//        assertEquals(1, recyclerView.adapter?.itemCount)
//    }
//
//    @Test
//    fun `clicar em confirmar sem itens selecionados exibe um alerta`() {
//        // Given: Uma lista de produtos, mas nenhum selecionado
//        val produtos = arrayListOf(
//            ProdutoPedido(1L, null, R.drawable.shirt_image, "Camisa", 50.0, "Algodão", selecionado = false)
//        )
//        val intent = Intent(ApplicationProvider.getApplicationContext(), ItemOrderActivity::class.java).apply {
//            putParcelableArrayListExtra("produtos", produtos)
//        }
//        val scenario = Robolectric.buildActivity(ItemOrderActivity::class.java, intent).create().resume().visible()
//        val activity = scenario.get()
//
//        // When: O botão de confirmar é clicado
//        activity.findViewById<MaterialButton>(R.id.btnConfirmarPedido).performClick()
//
//        // Then: Um AlertDialog é exibido com a mensagem correta
//        val latestDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
//        val shadow = shadowOf(latestDialog)
//        assertEquals("Nenhum item selecionado!", shadow.message)
//    }
//
//    @Test
//    fun `clicar em confirmar com itens selecionados cria o pedido`() = runBlocking {
//        // Given: Um item selecionado na lista
//        val produtos = arrayListOf(
//            ProdutoPedido(1L, null, R.drawable.shirt_image, "Camisa", 50.0, "Algodão", selecionado = true)
//        )
//        // E o repositório está mockado para retornar o pedido criado
//        val pedidoCriado = Pedido(id = 100L, codigo = "P-100", produtos = emptyList(), total = 50.0)
//        coEvery { PedidoRepository.criarPedidoRetornando(any()) } returns pedidoCriado
//
//        val intent = Intent(ApplicationProvider.getApplicationContext(), ItemOrderActivity::class.java).apply {
//            putParcelableArrayListExtra("produtos", produtos)
//        }
//        val scenario = Robolectric.buildActivity(ItemOrderActivity::class.java, intent).create().resume().visible()
//        val activity = scenario.get()
//
//        // When: O botão de confirmar é clicado
//        activity.findViewById<MaterialButton>(R.id.btnConfirmarPedido).performClick()
//
//        // Then: Um AlertDialog de sucesso é exibido
//        val latestDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
//        val shadow = shadowOf(latestDialog)
//        assertEquals("Pedido P-100 confirmado com sucesso!", shadow.message)
//    }
//}