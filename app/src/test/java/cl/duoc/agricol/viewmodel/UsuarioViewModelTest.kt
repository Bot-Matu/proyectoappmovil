package cl.duoc.agricol.viewmodel

import cl.duoc.agricol.model.Usuario
import cl.duoc.agricol.repository.UsuarioDao
import cl.duoc.agricol.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val dao = FakeUsuarioDao()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `un formulario vacio no pasa la validacion del registro`() = runTest {
        println("INICIO: comprobar la validacion con el formulario vacio.")
        val viewModel = crearViewModel()

        val esValido = viewModel.validarRegistro()

        println("COMPROBACION: debe fallar y mostrar un error por campo.")
        assertFalse(esValido)
        assertEquals("El nombre es obligatorio", viewModel.errors.value.nombre)
        assertEquals("Correo inválido", viewModel.errors.value.correo)
        assertEquals("Debes aceptar los términos y condiciones", viewModel.errors.value.terminos)
        println("CORRECTO: se mostraron los errores esperados.")
    }

    @Test
    fun `un formulario correcto pasa la validacion del registro`() = runTest {
        println("INICIO: comprobar la validacion con datos correctos.")
        val viewModel = crearViewModel()
        llenarRegistroValido(viewModel)

        val esValido = viewModel.validarRegistro()

        println("COMPROBACION: debe ser valido.")
        assertTrue(esValido)
        println("CORRECTO: el formulario fue aceptado.")
    }

    @Test
    fun `un rut invalido muestra error y no registra`() = runTest {
        println("INICIO: comprobar un RUT invalido.")
        val viewModel = crearViewModel()
        llenarRegistroValido(viewModel)
        viewModel.onRutChange("123")

        viewModel.registrar()
        advanceUntilIdle()

        println("COMPROBACION: debe haber error de RUT y ningun usuario guardado.")
        assertEquals("RUT inválido. Ejemplo: 12345678-9", viewModel.errors.value.rut)
        assertEquals(0, dao.usuarios.size)
        println("CORRECTO: no se registro el usuario.")
    }

    @Test
    fun `registrar guarda al usuario nuevo`() = runTest {
        println("INICIO: comprobar el registro de un usuario nuevo.")
        val viewModel = crearViewModel()
        llenarRegistroValido(viewModel)

        viewModel.registrar()
        advanceUntilIdle()

        println("COMPROBACION: debe guardarse con el RUT limpio y mostrar el modal de exito.")
        assertEquals(1, dao.usuarios.size)
        assertEquals("12345678-5", dao.usuarios.first().rut)
        assertTrue(viewModel.registroExitoso.value)
        assertEquals("Tu cuenta fue creada correctamente", viewModel.mensajeModal.value)
        println("CORRECTO: el usuario fue registrado.")
    }

    @Test
    fun `no permite registrar un rut que ya existe`() = runTest {
        println("INICIO: comprobar un RUT repetido.")
        dao.usuarios.add(usuarioDePrueba())
        val viewModel = crearViewModel()
        llenarRegistroValido(viewModel)

        viewModel.registrar()
        advanceUntilIdle()

        println("COMPROBACION: debe avisar que el RUT ya existe y no duplicar al usuario.")
        assertEquals("Ya existe un usuario registrado con ese RUT", viewModel.mensajeModal.value)
        assertEquals(1, dao.usuarios.size)
        println("CORRECTO: no se duplico el usuario.")
    }

    @Test
    fun `iniciar sesion con datos correctos guarda la sesion`() = runTest {
        println("INICIO: comprobar el login correcto.")
        dao.usuarios.add(usuarioDePrueba())
        val viewModel = crearViewModel()
        var entro = false

        viewModel.iniciarSesion("12.345.678-5", "123456") { entro = true }
        advanceUntilIdle()

        println("COMPROBACION: debe avisar el exito y dejar la sesion activa.")
        assertTrue(entro)
        assertEquals("Ana Perez", viewModel.usuarioActual.value?.nombre)
        assertEquals(1, dao.sesionActivaId)
        assertFalse(viewModel.cargando.value)
        println("CORRECTO: la sesion quedo iniciada.")
    }

    @Test
    fun `iniciar sesion con clave incorrecta muestra error`() = runTest {
        println("INICIO: comprobar el login con clave incorrecta.")
        dao.usuarios.add(usuarioDePrueba())
        val viewModel = crearViewModel()
        var entro = false

        viewModel.iniciarSesion("12345678-5", "clave-mala") { entro = true }
        advanceUntilIdle()

        println("COMPROBACION: debe mostrar el error y no entrar.")
        assertFalse(entro)
        assertEquals("RUT o contraseña incorrectos", viewModel.errorLogin.value)
        println("CORRECTO: el login fue rechazado.")
    }

    private fun crearViewModel() = UsuarioViewModel(UsuarioRepository(dao))

    private fun usuarioDePrueba() = Usuario(
        id = 1,
        nombre = "Ana Perez",
        celular = 12345678,
        correo = "ana@correo.cl",
        contrasena = "123456",
        rut = "12345678-5",
        nro_documento = "123456789"
    )

    // Deja el formulario de registro con datos correctos
    private fun llenarRegistroValido(viewModel: UsuarioViewModel) {
        viewModel.onNombreChange("Ana Perez")
        viewModel.onCorreoChange("ana@correo.cl")
        viewModel.onCelularChange("12345678")
        viewModel.onContrasenaChange("123456")
        viewModel.onRepContrasenaChange("123456")
        viewModel.onTerminosChange(true)
        viewModel.onRutChange("12.345.678-5")
        viewModel.onNroDocumentoChange("123456789")
    }

    // DAO falso: guarda todo en memoria, sin Room
    private class FakeUsuarioDao : UsuarioDao {
        val usuarios = mutableListOf<Usuario>()
        var sesionActivaId: Int? = null

        override suspend fun insertar(usuario: Usuario) {
            usuarios.add(usuario.copy(id = usuarios.size + 1))
        }

        override suspend fun upsert(usuario: Usuario) {
            usuarios.removeAll { it.id == usuario.id }
            usuarios.add(usuario)
        }

        override suspend fun buscarPorRut(rut: String): Usuario? =
            usuarios.find { it.rut == rut }

        override suspend fun iniciarSesion(rut: String, contrasena: String): Usuario? =
            usuarios.find { it.rut == rut && it.contrasena == contrasena }

        override suspend fun activarSesion(id: Int) {
            sesionActivaId = id
        }

        override suspend fun cerrarSesiones() {
            sesionActivaId = null
        }

        override suspend fun buscarSesionActiva(): Usuario? =
            usuarios.find { it.id == sesionActivaId }

        override fun observarOtros(yoId: Int): Flow<List<Usuario>> =
            flowOf(usuarios.filter { it.id != yoId })

        override suspend fun nombrePorId(id: Int): String? =
            usuarios.find { it.id == id }?.nombre
    }
}