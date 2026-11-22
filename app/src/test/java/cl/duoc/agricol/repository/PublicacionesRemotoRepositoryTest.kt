package cl.duoc.agricol.repository

import cl.duoc.agricol.model.PublicacionRemota
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

class PublicacionesRemotoRepositoryTest {
    private lateinit var servidor: MockWebServer
    private lateinit var repository: PublicacionesRemotoRepository

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        val api = RetrofitClient.crearApi(servidor.url("/api:GOcHn4ic/").toString())
        repository = PublicacionesRemotoRepositoryImpl(api)
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun `GET publicacion consulta el endpoint y convierte la respuesta`() = runTest {
        println("INICIO: comprobar GET /publicacion y la conversion del JSON.")
        println("PREPARACION: el servidor local simulado respondera con una publicacion de Poda.")
        servidor.enqueue(MockResponse().setBody(
            """[{"id":1,"created_at":1794884366031,"titulo":"Poda de manzanos","categoria":"Poda","comuna":"Curicó","ciudad":"Región del Maule","fecha_inicio":"15/11/2026","fecha_termino":"25/11/2026","descripcion":"Poda de 2 hectáreas de manzanos","requisitos":"Experiencia de 1 año","info_extra":"Se entrega almuerzo","contratante":"Agrícola San Pedro Ltda.","dias":10,"tra_necesitados":2,"paga":300000,"creadorId":1}]"""
        ))

        println("ACCION: el repositorio llama al servidor local, no a Xano.")
        val resultado = repository.obtenerPublicaciones()

        println("COMPROBACION: el JSON debe convertirse en una publicacion y usar GET /publicacion.")
        assertEquals(1, resultado.size)
        assertEquals(
            PublicacionRemota(
                id = 1,
                titulo = "Poda de manzanos",
                categoria = "Poda",
                comuna = "Curicó",
                ciudad = "Región del Maule",
                fecha_inicio = "15/11/2026",
                fecha_termino = "25/11/2026",
                dias = 10,
                tra_necesitados = 2,
                descripcion = "Poda de 2 hectáreas de manzanos",
                requisitos = "Experiencia de 1 año",
                info_extra = "Se entrega almuerzo",
                contratante = "Agrícola San Pedro Ltda.",
                paga = 300000,
                creadorId = 1
            ),
            resultado.first()
        )
        val solicitud = servidor.takeRequest()
        assertEquals("GET", solicitud.method)
        assertEquals("/api:GOcHn4ic/publicacion", solicitud.path)
        println("CORRECTO: la solicitud y la publicacion son los esperados.")
    }

    @Test
    fun `GET publicacion con lista vacia devuelve lista vacia`() = runTest {
        println("INICIO: comprobar una respuesta vacia.")
        servidor.enqueue(MockResponse().setBody("[]"))

        println("ACCION: solicitar publicaciones.")
        val resultado = repository.obtenerPublicaciones()

        println("COMPROBACION: debe entregar una lista vacia, sin error.")
        assertEquals(emptyList<PublicacionRemota>(), resultado)
        println("CORRECTO: se recibio una lista vacia.")
    }

    @Test
    fun `GET publicacion propaga errores http`() = runTest {
        println("INICIO: comprobar la propagacion de un error HTTP 500.")
        servidor.enqueue(MockResponse().setResponseCode(500))

        println("ACCION: solicitar publicaciones al servidor local.")
        val codigo = codigoDeError { repository.obtenerPublicaciones() }

        println("COMPROBACION: el error recibido debe tener codigo 500.")
        assertEquals(500, codigo)
        println("CORRECTO: el repositorio propago el error HTTP 500.")
    }

    @Test
    fun `POST publicacion envia el cuerpo y devuelve la publicacion creada`() = runTest {
        println("INICIO: comprobar POST /publicacion.")
        println("PREPARACION: el servidor respondera con la publicacion guardada (id 9).")
        servidor.enqueue(MockResponse().setBody(
            """{"id":9,"created_at":1794884366031,"titulo":"Riego de huerto","categoria":"Riego","comuna":"Talca","ciudad":"Región del Maule","fecha_inicio":"18/11/2026","fecha_termino":"25/11/2026","descripcion":"Riego por goteo","requisitos":"Experiencia basica","info_extra":"","contratante":"Hortalizas del Valle SpA","dias":7,"tra_necesitados":1,"paga":200000,"creadorId":1}"""
        ))
        val nueva = PublicacionRemota(
            titulo = "Riego de huerto",
            categoria = "Riego",
            comuna = "Talca",
            ciudad = "Región del Maule",
            fecha_inicio = "18/11/2026",
            fecha_termino = "25/11/2026",
            dias = 7,
            tra_necesitados = 1,
            descripcion = "Riego por goteo",
            requisitos = "Experiencia basica",
            info_extra = "",
            contratante = "Hortalizas del Valle SpA",
            paga = 200000,
            creadorId = 1
        )

        println("ACCION: crear la publicacion.")
        val creada = repository.crearPublicacion(nueva)

        println("COMPROBACION: debe usar POST /publicacion, enviar los datos y devolver el id 9.")
        assertEquals(9, creada.id)
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api:GOcHn4ic/publicacion", solicitud.path)
        val cuerpo = JsonParser.parseString(solicitud.body.readUtf8()).asJsonObject
        assertEquals("Riego de huerto", cuerpo.get("titulo").asString)
        assertEquals("Riego", cuerpo.get("categoria").asString)
        assertEquals(200000, cuerpo.get("paga").asInt)
        assertEquals(1, cuerpo.get("creadorId").asInt)
        println("CORRECTO: la publicacion se envio y se recibio correctamente.")
    }

    @Test
    fun `DELETE publicacion llama al endpoint con el id`() = runTest {
        println("INICIO: comprobar DELETE /publicacion/{id}.")
        servidor.enqueue(MockResponse().setBody("null"))

        println("ACCION: eliminar la publicacion 5.")
        repository.eliminarPublicacion(5)

        println("COMPROBACION: debe usar DELETE sobre /publicacion/5.")
        val solicitud = servidor.takeRequest()
        assertEquals("DELETE", solicitud.method)
        assertEquals("/api:GOcHn4ic/publicacion/5", solicitud.path)
        println("CORRECTO: se pidio borrar la publicacion 5.")
    }

    @Test
    fun `DELETE publicacion propaga el error 404`() = runTest {
        println("INICIO: comprobar borrar una publicacion que no existe.")
        servidor.enqueue(MockResponse().setResponseCode(404))

        println("ACCION: eliminar la publicacion 99.")
        val codigo = codigoDeError { repository.eliminarPublicacion(99) }

        println("COMPROBACION: el error recibido debe tener codigo 404.")
        assertEquals(404, codigo)
        println("CORRECTO: el repositorio propago el error HTTP 404.")
    }

    // Ejecuta el bloque y devuelve el codigo del HttpException que lance
    private suspend fun codigoDeError(bloque: suspend () -> Unit): Int {
        try {
            bloque()
        } catch (error: HttpException) {
            return error.code()
        }
        throw AssertionError("Se esperaba un HttpException")
    }
}
