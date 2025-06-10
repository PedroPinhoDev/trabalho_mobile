//package com.example.trabalho.view.activity
//
//import android.app.Activity.RESULT_OK
//import android.content.Intent
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.test.core.app.ApplicationProvider
//import com.example.trabalho.R
//import com.example.trabalho.model.entities.Pedido
//import com.example.trabalho.model.entities.ProdutoPedido
//import com.example.trabalho.repository.PedidoRepository
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.mockkObject
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.Robolectric
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.Shadows.shadowOf
//import org.robolectric.shadows.ShadowPopupMenu
//import org.robolectric.shadows.ShadowToast
//
//@RunWith(RobolectricTestRunner::class)
//class DetalhesPedidoActivityTest {
//
//    private lateinit var pedidoMock: Pedido
//
//    @Before
//    fun setup() {
//        mockkObject(PedidoRepository)
//        pedidoMock = Pedido(
//            id = 1L,
//            codigo = "P-1",
//            status = "Aberto",
//            total = 100.0,
//            produtos = listOf(ProdutoPedido(10L, null, R.drawable.shirt_image, "Camisa", 100.0, "Algodão"))
//        )
//    }
//
//    @Test
//    fun `com ID de pedido válido, busca e exibe os detalhes`() = runBlocking {
//        // Given: O repositório retornará um pedido válido
//        coEvery { PedidoRepository.buscarPedidoPorId(1L) } returns pedidoMock
//
//        // When: A activity é criada com um ID válido na intent
//        val intent = Intent(ApplicationProvider.getApplicationContext(), DetalhesPedidoActivity::class.java).apply {
//            putExtra(DetalhesPedidoActivity.EXTRA_PEDIDO_ID, 1L)
//        }
//        val scenario = Robolectric.buildActivity(DetalhesPedidoActivity::class.java, intent).create().resume().visible()
//        val activity = scenario.get()
//
//        // Then: O repositório foi chamado para buscar o pedido
//        coVerify { PedidoRepository.buscarPedidoPorId(1L) }
//
//        // And: A UI exibe as informações corretas
//        val codigoTextView = activity.findViewById<TextView>(R.id.textCodigoPedido)
//        val totalTextView = activity.findViewById<TextView>(R.id.textTotal)
//        val statusButton = activity.findViewById<Button>(R.id.botaoStatus)
//
//        assertEquals("P-1", codigoTextView.text)
//        assertEquals("R$ 100,00", totalTextView.text) // Verifica o formato da moeda
//        assertEquals("Status: Aberto", statusButton.text)
//    }
//
//    @Test
//    fun `com ID de pedido inválido, exibe Toast e finaliza`() {
//        // When: A activity é criada com um ID inválido
//        val intent = Intent(ApplicationProvider.getApplicationContext(), DetalhesPedidoActivity::class.java).apply {
//            putExtra(DetalhesPedidoActivity.EXTRA_PEDIDO_ID, -1L)
//        }
//        val scenario = Robolectric.buildActivity(DetalhesPedidoActivity::class.java, intent).create()
//
//        // Then: A activity é finalizada
//        assertTrue(scenario.get().isFinishing)
//        // And: Um Toast de erro é exibido
//        assertEquals("ID de pedido inválido", ShadowToast.getTextOfLatestToast())
//    }
//
//    @Test
//    fun `clicar no botão de status e selecionar uma opção atualiza a UI e o repositório`() = runBlocking {
//        // Given: A activity está rodando com um pedido válido
//        coEvery { PedidoRepository.buscarPedidoPorId(1L) } returns pedidoMock
//        coEvery { PedidoRepository.atualizarPedido(any()) } returns Unit // Mock da chamada de atualização
//
//        val intent = Intent(ApplicationProvider.getApplicationContext(), DetalhesPedidoActivity::class.java).apply {
//            putExtra(DetalhesPedidoActivity.EXTRA_PEDIDO_ID, 1L)
//        }
//        val scenario = Robolectric.buildActivity(DetalhesPedidoActivity::class.java, intent).create().resume().visible()
//        val activity = scenario.get()
//
//        // When: O botão de status é clicado e uma opção do menu é selecionada
//        val statusButton = activity.findViewById<Button>(R.id.botaoStatus)
//        statusButton.performClick()
//
//        val latestPopupMenu = ShadowPopupMenu.getLatestPopupMenu()
//        shadowOf(latestPopupMenu).getOnMenuItemClickListener()
//            .onMenuItemClick(latestPopupMenu.menu.findItem(R.id.statusCancelado))
//
//        // Then: O repositório é chamado para atualizar o pedido com o novo status
//        coVerify { PedidoRepository.atualizarPedido(pedidoMock.copy(status = "Cancelado")) }
//
//        // And: O texto do botão na UI é atualizado
//        assertEquals("Status: Cancelado", statusButton.text)
//
//        // And: Um resultado é definido para a activity anterior
//        val result = shadowOf(activity).resultIntent
//        assertEquals(RESULT_OK, shadowOf(activity).resultCode)
//        assertEquals(true, result.getBooleanExtra("statusAlterado", false))
//        assertEquals("Cancelado", result.getStringExtra("novoStatus"))
//    }
//}
