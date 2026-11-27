package cl.duoc.agricol.viewmodel

import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.model.Usuario
import cl.duoc.agricol.repository.PublicacionRepository
import cl.duoc.agricol.repository.PublicacionesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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
class PublicacionViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val dao = FakePublicacionesDao()
    private val productor = Usuario(
        id = 7,
        nombre = "Productor Prueba",
        celular = 12345678,
        correo = "productor@correo.cl",
        contrasena = "123456",
        rut = "12345678-5",
        nro_documento = "123456789",
        rol = "Productor"
    )

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `un formulario vacio no se publica`() = runTest {
        println("INICIO: comprobar que un formulario vacio no se publique.")
        val viewModel = crearViewModel()
        advanceUntilIdle()

        viewModel.publicar(productor)
        advanceUntilIdle()

        println("COMPROBACION: debe mostrar errores y no guardar nada nuevo.")
        assertFalse(viewModel.publicado.value)
        assertEquals("El título es obligatorio", viewModel.errors.value.titulo)
        assertEquals(3, dao.guardadas.value.size)
        println("CORRECTO: no se publico el formulario vacio.")
    }

    @Test
    fun `rechaza una fecha que no existe`() = runTest {
        println("INICIO: comprobar una fecha inexistente.")
        val viewModel = crearViewModel()
        advanceUntilIdle()
        llenarFormularioValido(viewModel)
        viewModel.onFechaInicioChange("31/02/2026")

        viewModel.publicar(productor)
        advanceUntilIdle()

        println("COMPROBACION: debe pedir el formato correcto y no publicar.")
        assertEquals("Usa el formato dd/mm/aaaa", viewModel.errors.value.fecha_inicio)
        assertFalse(viewModel.publicado.value)
        println("CORRECTO: la fecha inexistente fue rechazada.")
    }

    @Test
    fun `rechaza una fecha de termino anterior al inicio`() = runTest {
        println("INICIO: comprobar fecha de termino anterior al inicio.")
        val viewModel = crearViewModel()
        advanceUntilIdle()
        llenarFormularioValido(viewModel)
        viewModel.onFechaTerminoChange("01/11/2026")

        viewModel.publicar(productor)
        advanceUntilIdle()

        println("COMPROBACION: debe avisar que el termino debe ser posterior.")
        assertEquals("Debe ser posterior al inicio", viewModel.errors.value.fecha_termino)
        assertFalse(viewModel.publicado.value)
        println("CORRECTO: el rango de fechas invalido fue rechazado.")
    }

    @Test
    fun `publica una oferta valida`() = runTest {
        println("INICIO: comprobar la publicacion de una oferta valida.")
        val viewModel = crearViewModel()
        advanceUntilIdle()
        llenarFormularioValido(viewModel)

        viewModel.publicar(productor)
        advanceUntilIdle()

        println("COMPROBACION: debe guardarse con los dias calculados y el productor como contratante.")
        assertTrue(viewModel.publicado.value)
        assertEquals(4, dao.guardadas.value.size)
        val nueva = dao.guardadas.value.first()
        assertEquals("Poda de manzanos", nueva.titulo)
        assertEquals(10, nueva.dias)
        assertEquals("Productor Prueba", nueva.contratante)
        assertEquals(7, nueva.creadorId)
        println("CORRECTO: la oferta fue publicada.")
    }

    @Test
    fun `filtra las publicaciones por categoria`() = runTest {
        println("INICIO: comprobar el filtro por categoria.")
        val viewModel = crearViewModel()
        backgroundScope.launch { viewModel.publicaciones.collect { } }
        advanceUntilIdle()

        viewModel.onCategoriaChange("Riego")
        advanceUntilIdle()

        println("COMPROBACION: solo debe quedar la publicacion de Riego.")
        assertEquals(1, viewModel.publicaciones.value.size)
        assertEquals("Riego", viewModel.publicaciones.value.first().categoria)
        println("CORRECTO: el filtro funciona.")
    }

    private fun crearViewModel() = PublicacionViewModel(PublicacionRepository(dao))

    // Deja el formulario con datos correctos (10 dias entre inicio y termino)
    private fun llenarFormularioValido(viewModel: PublicacionViewModel) {
        viewModel.onTituloChange("Poda de manzanos")
        viewModel.onCategoriaFormChange("Poda")
        viewModel.onComunaChange("Curicó")
        viewModel.onCiudadChange("Región del Maule")
        viewModel.onFechaInicioChange("10/11/2026")
        viewModel.onFechaTerminoChange("20/11/2026")
        viewModel.onTrabajadoresChange("2")
        viewModel.onDescripcionChange("Poda de 2 hectáreas de manzanos")
        viewModel.onRequisitosChange("Experiencia previa")
        viewModel.onPagaChange("300000")
    }

    // DAO falso: guarda las publicaciones en memoria, sin Room
    private class FakePublicacionesDao : PublicacionesDao {
        val guardadas = MutableStateFlow<List<Publicacion>>(emptyList())

        override fun observarTodas(): Flow<List<Publicacion>> = guardadas

        override suspend fun contar(): Int = guardadas.value.size

        override suspend fun insert(publicacion: Publicacion) {
            guardadas.value = listOf(publicacion.copy(id = guardadas.value.size + 1)) + guardadas.value
        }

        override suspend fun insertarVarias(publicaciones: List<Publicacion>) {
            guardadas.value = publicaciones + guardadas.value
        }

        override suspend fun upsertVarias(publicaciones: List<Publicacion>) {
            val ids = publicaciones.map { it.id }
            guardadas.value = publicaciones + guardadas.value.filter { it.id !in ids }
        }

        override suspend fun borrarExcepto(ids: List<Int>) {
            guardadas.value = guardadas.value.filter { it.id in ids }
        }


        override suspend fun update(publicacion: Publicacion) {}

        override suspend fun delete(publicacion: Publicacion) {}
    }
}