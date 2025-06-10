package com.example.trabalho.service

import org.junit.Assert.*
import org.junit.Test
import retrofit2.http.*

class ApiServiceTest {

    /** Encontra na interface ApiService o método com este nome e quantidade de parâmetros. */
    private fun findMethod(name: String, paramCount: Int): java.lang.reflect.Method {
        // usa declaredMethods para pegar só os da própria ApiService
        val matches = ApiService::class.java.declaredMethods.filter {
            // CORREÇÃO: Funções suspend têm um parâmetro extra (Continuation)
            it.name == name && it.parameterCount == paramCount + 1
        }
        assertTrue("Método $name/$paramCount não encontrado em ApiService", matches.isNotEmpty())
        return matches.first()
    }

    @Test
    fun `getProducts usa GET products`() {
        val m = findMethod("getProducts", 0)
        val ann = m.getAnnotation(GET::class.java)
        assertNotNull("A anotação @GET não foi encontrada no método getProducts", ann)
        assertEquals("O endpoint para getProducts deve ser 'products'", "products", ann.value)
    }

    @Test
    fun `getOrders usa GET orders`() {
        val m = findMethod("getOrders", 0)
        val ann = m.getAnnotation(GET::class.java)
        assertNotNull("A anotação @GET não foi encontrada no método getOrders", ann)
        assertEquals("O endpoint para getOrders deve ser 'orders'", "orders", ann.value)
    }

    @Test
    fun `getOrderById usa GET orders_{id} e Path id`() {
        val m = findMethod("getOrderById", 1)
        val getAnn = m.getAnnotation(GET::class.java)
        assertNotNull("A anotação @GET não foi encontrada no método getOrderById", getAnn)
        assertEquals("O endpoint para getOrderById deve ser 'orders/{id}'", "orders/{id}", getAnn.value)

        val pathAnn = m.parameters[0].getAnnotation(Path::class.java)
        assertNotNull("A anotação @Path não foi encontrada no parâmetro de getOrderById", pathAnn)
        assertEquals("O valor da anotação @Path deve ser 'id'", "id", pathAnn.value)
    }

    @Test
    fun `createProduct usa POST products e Body`() {
        val m = findMethod("createProduct", 1)
        val postAnn = m.getAnnotation(POST::class.java)
        assertNotNull("A anotação @POST não foi encontrada no método createProduct", postAnn)
        assertEquals("O endpoint para createProduct deve ser 'products'", "products", postAnn.value)
        assertNotNull("A anotação @Body não foi encontrada no parâmetro de createProduct", m.parameters[0].getAnnotation(Body::class.java))
    }

    @Test
    fun `createOrder usa POST orders e Body`() {
        val m = findMethod("createOrder", 1)
        val postAnn = m.getAnnotation(POST::class.java)
        assertNotNull("A anotação @POST não foi encontrada no método createOrder", postAnn)
        assertEquals("O endpoint para createOrder deve ser 'orders'", "orders", postAnn.value)
        assertNotNull("A anotação @Body não foi encontrada no parâmetro de createOrder", m.parameters[0].getAnnotation(Body::class.java))
    }
}