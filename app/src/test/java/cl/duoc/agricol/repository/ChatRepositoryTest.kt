package cl.duoc.agricol.repository

import cl.duoc.agricol.model.MensajeRemoto
import cl.duoc.agricol.remote.RetrofitClient
import com.google.gson.JsonParser
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class ChatRepositoryTest {
    private lateinit var servidor: MockWebServer
    private lateinit var repository: ChatRepository

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        val api = RetrofitClient.crearChatApi(servidor.url("/api:GOcHn4ic/").toString())
        repository = ChatRepositoryImpl(api)
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun `GET conversacion deja solo las conversaciones del usuario`() = runTest {
        println("INICIO: comprobar GET /conversacion y el filtro por usuario.")
        println("PREPARACION: el servidor respondera con 3 conversaciones; el usuario 1 participa en 2.")
        servidor.enqueue(MockResponse().setBody(
            """[
            {"id":1,"created_at":1794884366031,"usuario1Id":1,"usuario2Id":2,"ultimoMensaje":"Hola","ultimoMensajeTiempo":1794884366031},
            {"id":2,"created_at":1794884366032,"usuario1Id":3,"usuario2Id":4,"ultimoMensaje":"","ultimoMensajeTiempo":0},
            {"id":3,"created_at":1794884366033,"usuario1Id":2,"usuario2Id":1,"ultimoMensaje":"Buenas","ultimoMensajeTiempo":1794884399999}
            ]"""
        ))

        println("ACCION: pedir las conversaciones del usuario 1.")
        val resultado = repository.obtenerConversaciones(1)

        println("COMPROBACION: deben quedar las conversaciones 1 y 3.")
        assertEquals(listOf(1, 3), resultado.map { it.id })
        assertEquals("Hola", resultado.first().ultimoMensaje)
        assertEquals(1794884399999L, resultado.last().ultimoMensajeTiempo)
        val solicitud = servidor.takeRequest()
        assertEquals("GET", solicitud.method)
        assertEquals("/api:GOcHn4ic/conversacion", solicitud.path)
        println("CORRECTO: solo se entregaron las conversaciones del usuario.")
    }

    @Test
    fun `POST conversacion envia los dos usuarios y devuelve la creada`() = runTest {
        println("INICIO: comprobar POST /conversacion.")
        servidor.enqueue(MockResponse().setBody(
            """{"id":7,"created_at":1794884366031,"usuario1Id":1,"usuario2Id":2,"ultimoMensaje":"","ultimoMensajeTiempo":0}"""
        ))

        println("ACCION: crear la conversacion entre los usuarios 1 y 2.")
        val creada = repository.crearConversacion(1, 2)

        println("COMPROBACION: debe devolver el id 7 y haber enviado ambos usuarios.")
        assertEquals(7, creada.id)
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api:GOcHn4ic/conversacion", solicitud.path)
        val cuerpo = JsonParser.parseString(solicitud.body.readUtf8()).asJsonObject
        assertEquals(1, cuerpo.get("usuario1Id").asInt)
        assertEquals(2, cuerpo.get("usuario2Id").asInt)
        println("CORRECTO: la conversacion se creo con los usuarios esperados.")
    }

    @Test
    fun `GET mensaje deja solo los mensajes de la conversacion`() = runTest {
        println("INICIO: comprobar GET /mensaje y el filtro por conversacion.")
        println("PREPARACION: el servidor respondera con 3 mensajes; 2 son de la conversacion 1.")
        servidor.enqueue(MockResponse().setBody(
            """[
            {"id":1,"created_at":1,"conversacionId":1,"emisorId":1,"contenido":"Hola","tiempo":1794884366031,"estadoMensaje":"ENVIADO"},
            {"id":2,"created_at":2,"conversacionId":2,"emisorId":3,"contenido":"Otro chat","tiempo":1794884366032,"estadoMensaje":"ENVIADO"},
            {"id":3,"created_at":3,"conversacionId":1,"emisorId":2,"contenido":"Hola, que tal","tiempo":1794884366033,"estadoMensaje":"LEIDO"}
            ]"""
        ))

        println("ACCION: pedir los mensajes de la conversacion 1.")
        val resultado = repository.obtenerMensajes(1)

        println("COMPROBACION: deben quedar los mensajes 1 y 3, en orden.")
        assertEquals(listOf(1, 3), resultado.map { it.id })
        assertEquals("Hola, que tal", resultado.last().contenido)
        assertEquals("LEIDO", resultado.last().estadoMensaje)
        val solicitud = servidor.takeRequest()
        assertEquals("GET", solicitud.method)
        assertEquals("/api:GOcHn4ic/mensaje", solicitud.path)
        println("CORRECTO: solo se entregaron los mensajes de la conversacion.")
    }

    @Test
    fun `POST mensaje envia el contenido y devuelve el mensaje guardado`() = runTest {
        println("INICIO: comprobar POST /mensaje.")
        servidor.enqueue(MockResponse().setBody(
            """{"id":12,"created_at":5,"conversacionId":1,"emisorId":1,"contenido":"Me interesa el trabajo","tiempo":1794884366031,"estadoMensaje":"ENVIADO"}"""
        ))
        val mensaje = MensajeRemoto(
            conversacionId = 1,
            emisorId = 1,
            contenido = "Me interesa el trabajo",
            tiempo = 1794884366031L
        )

        println("ACCION: enviar el mensaje.")
        val guardado = repository.enviarMensaje(mensaje)

        println("COMPROBACION: debe devolver el id 12 y haber enviado el contenido.")
        assertEquals(12, guardado.id)
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api:GOcHn4ic/mensaje", solicitud.path)
        val cuerpo = JsonParser.parseString(solicitud.body.readUtf8()).asJsonObject
        assertEquals("Me interesa el trabajo", cuerpo.get("contenido").asString)
        assertEquals(1, cuerpo.get("conversacionId").asInt)
        assertEquals(1, cuerpo.get("emisorId").asInt)
        assertEquals(1794884366031L, cuerpo.get("tiempo").asLong)
        println("CORRECTO: el mensaje se envio y se recibio correctamente.")
    }

    @Test
    fun `POST mensaje propaga errores http`() = runTest {
        println("INICIO: comprobar un error HTTP al enviar un mensaje.")
        servidor.enqueue(MockResponse().setResponseCode(500))

        println("ACCION: enviar un mensaje al servidor local.")
        var codigo = 0
        try {
            repository.enviarMensaje(MensajeRemoto(conversacionId = 1, emisorId = 1, contenido = "Hola"))
            throw AssertionError("Se esperaba un HttpException")
        } catch (error: HttpException) {
            codigo = error.code()
        }

        println("COMPROBACION: el error recibido debe tener codigo 500.")
        assertEquals(500, codigo)
        println("CORRECTO: el repositorio propago el error HTTP 500.")
    }
}
