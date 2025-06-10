//package com.example.trabalho.view.activity
//
//import android.app.Activity.RESULT_OK
//import android.content.Intent
//import android.view.View
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.core.app.ApplicationProvider.getApplicationContext
//import com.example.trabalho.R
//import com.example.trabalho.model.entities.Produto
//import com.example.trabalho.repository.ProdutoRepository
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import io.mockk.coVerify
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
//import org.robolectric.annotation.Config
//
//@RunWith(RobolectricTestRunner::class)
//@Config(sdk = [Config.OLDEST_SDK]) // Usar um SDK consistente para os testes
//class MainActivityTest {
//
//    @Before
//    fun setup() {
//        // Mocka o singleton para controlar seu comportamento nos testes
//        mockkObject(ProdutoRepository)
//    }
//
//    @Test
//    fun `ao criar, carrega produtos e atualiza a UI`() = runBlocking {
//        // Given: Uma lista de produtos que o repositório deve retornar
//        val produtosMock = listOf(Produto(1L, "Camisa", 50.0, "Algodão", null, R.drawable.shirt_image))
//        ProdutoRepository.produtos.addAll(produtosMock)
//
//        // When: A activity é criada
//        val scenario = Robolectric.buildActivity(MainActivity::class.java).create().resume().visible()
//        val activity = scenario.get()
//
//        // Then: O método para carregar produtos foi chamado
//        coVerify { ProdutoRepository.carregarProdutos() }
//
//        // And: A UI reflete a lista de produtos
//        val listaLayout = activity.findViewById<LinearLayout>(R.id.listaProdutosLayout)
//        assertEquals(1, listaLayout.childCount)
//        val cardView = listaLayout.getChildAt(0)
//        val descTextView = cardView.findViewById<TextView>(R.id.descriptionTextView)
//        assertEquals("Camisa", descTextView.text.toString())
//    }
//
//    @Test
//    fun `quando a lista de produtos está vazia, exibe mensagem de texto`() = runBlocking {
//        // Given: Uma lista vazia no repositório
//        ProdutoRepository.produtos.clear()
//
//        // When: A activity é criada
//        val scenario = Robolectric.buildActivity(MainActivity::class.java).create().resume().visible()
//        val activity = scenario.get()
//
//        // Then: O texto "Sem Produtos" deve estar visível
//        val textViewSemProdutos = activity.findViewById<TextView>(R.id.textViewSemProdutos)
//        assertEquals(View.VISIBLE, textViewSemProdutos.visibility)
//    }
//
//    @Test
//    fun `clicar no FAB abre a ItemDetailsActivity para adicionar`() {
//        // Given: A activity está rodando
//        val controller = Robolectric.buildActivity(MainActivity::class.java).create().resume().visible()
//        val activity = controller.get()
//
//        // When: O botão de adicionar é clicado
//        activity.findViewById<FloatingActionButton>(R.id.fabAdd).performClick()
//
//        // Then: Uma intent para ItemDetailsActivity é iniciada
//        val expectedIntent = Intent(activity, ItemDetailsActivity::class.java)
//        val actualIntent = shadowOf(getApplicationContext()).nextStartedActivity
//        assertNotNull(actualIntent)
//        assertEquals(expectedIntent.component, actualIntent.component)
//    }
//
//    @Test
//    fun `ao retornar da tela de edição com sucesso, a lista é atualizada`() = runBlocking {
//        // Given: A activity está rodando
//        val controller = Robolectric.buildActivity(MainActivity::class.java).create().resume()
//        val activity = controller.get()
//
//        // When: onActivityResult é chamado com um resultado de edição
//        val produtoEditado = Produto(1L, "Camisa Editada", 55.0, "Poliéster", null, R.drawable.shirt_image)
//        val resultIntent = Intent().apply {
//            putExtra("produto", produtoEditado)
//            putExtra("index", 0)
//        }
//        activity.onActivityResult(MainActivity.REQUEST_CODE_EDIT, RESULT_OK, resultIntent)
//
//        // Then: O método de atualizar do repositório é chamado
//        coVerify { ProdutoRepository.atualizarProduto(0, produtoEditado) }
//    }
//}