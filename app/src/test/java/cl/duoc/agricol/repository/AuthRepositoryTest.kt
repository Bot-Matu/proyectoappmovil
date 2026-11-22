package cl.duoc.agricol.repository

import cl.duoc.agricol.model.SignupRequest
import cl.duoc.agricol.model.UsuarioRemoto
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

class AuthRepositoryTest {
    private lateinit var servidor: MockWebServer
    private lateinit var repository: AuthRepository

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        val api = RetrofitClient.crearAuthApi(servidor.url("/api:GOcHn4ic/").toString())
        repository = AuthRepositoryImpl(api)
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun `login envia correo y contrasena y devuelve el token`() = runTest {
        println("INICIO: comprobar POST /auth/login.")
        println("PREPARACION: el servidor local respondera con un token.")
        servidor.enqueue(MockResponse().setBody("""{"authToken":"token-abc-123"}"""))

        println("ACCION: iniciar sesion con correo y contrasena.")
        val token = repository.iniciarSesion("ana@correo.cl", "123456")

        println("COMPROBACION: debe devolver el token y enviar email y password.")
        assertEquals("token-abc-123", token)
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api:GOcHn4ic/auth/login", solicitud.path)
        val cuerpo = JsonParser.parseString(solicitud.body.readUtf8()).asJsonObject
        assertEquals("ana@correo.cl", cuerpo.get("email").asString)
        assertEquals("123456", cuerpo.get("password").asString)
        println("CORRECTO: el login envio los datos esperados.")
    }

    @Test
    fun `login con credenciales incorrectas propaga el error`() = runTest {
        println("INICIO: comprobar un login rechazado.")
        println("PREPARACION: el servidor respondera HTTP 403.")
        servidor.enqueue(MockResponse().setResponseCode(403))

        println("ACCION: iniciar sesion con una clave mala.")
        val codigo = codigoDeError { repository.iniciarSesion("ana@correo.cl", "clave-mala") }

        println("COMPROBACION: el error recibido debe tener codigo 403.")
        assertEquals(403, codigo)
        println("CORRECTO: el login rechazado se propago como error HTTP.")
    }

    @Test
    fun `signup envia los datos del usuario y devuelve el token`() = runTest {
        println("INICIO: comprobar POST /auth/signup.")
        servidor.enqueue(MockResponse().setBody("""{"authToken":"token-nuevo"}"""))
        val datos = SignupRequest(
            name = "Luis Soto",
            email = "luis@correo.cl",
            password = "a12345",
            rut = "11111111-2",
            nro_documento = "987654322",
            rol = "Trabajador",
            celular = 67654321
        )

        println("ACCION: registrar al usuario.")
        val token = repository.registrar(datos)

        println("COMPROBACION: debe devolver el token y enviar todos los campos.")
        assertEquals("token-nuevo", token)
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api:GOcHn4ic/auth/signup", solicitud.path)
        val cuerpo = JsonParser.parseString(solicitud.body.readUtf8()).asJsonObject
        assertEquals("Luis Soto", cuerpo.get("name").asString)
        assertEquals("luis@correo.cl", cuerpo.get("email").asString)
        assertEquals("11111111-2", cuerpo.get("rut").asString)
        assertEquals("Trabajador", cuerpo.get("rol").asString)
        assertEquals(67654321, cuerpo.get("celular").asInt)
        println("CORRECTO: el registro envio los datos esperados.")
    }

    @Test
    fun `me envia el token con Bearer y convierte el usuario`() = runTest {
        println("INICIO: comprobar GET /auth/me.")
        println("PREPARACION: el servidor respondera con el usuario del token.")
        servidor.enqueue(MockResponse().setBody(
            """{"id":4,"created_at":1795386781851,"name":"camilo","rut":"91111111-2","nro_documento":"997654322","rol":"Trabajador","email":"campito@correo.cl","celular":68654321}"""
        ))

        println("ACCION: pedir el perfil con un token.")
        val usuario = repository.obtenerPerfil("token-de-prueba")

        println("COMPROBACION: debe convertir el usuario y enviar el header Authorization.")
        assertEquals(
            UsuarioRemoto(
                id = 4,
                name = "camilo",
                email = "campito@correo.cl",
                rut = "91111111-2",
                nro_documento = "997654322",
                rol = "Trabajador",
                celular = 68654321
            ),
            usuario
        )
        val solicitud = servidor.takeRequest()
        assertEquals("GET", solicitud.method)
        assertEquals("/api:GOcHn4ic/auth/me", solicitud.path)
        assertEquals("Bearer token-de-prueba", solicitud.getHeader("Authorization"))
        println("CORRECTO: el perfil y el header son los esperados.")
    }

    @Test
    fun `me con token invalido propaga el error 401`() = runTest {
        println("INICIO: comprobar un token invalido.")
        servidor.enqueue(MockResponse().setResponseCode(401))

        println("ACCION: pedir el perfil con un token malo.")
        val codigo = codigoDeError { repository.obtenerPerfil("token-malo") }

        println("COMPROBACION: el error recibido debe tener codigo 401.")
        assertEquals(401, codigo)
        println("CORRECTO: el token invalido se propago como error HTTP.")
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
