package cl.duoc.agricol.viewmodel

import cl.duoc.agricol.model.PublicacionRemota
import cl.duoc.agricol.repository.PublicacionesRemotoRepository
import java.io.IOException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class PublicacionesRemotasViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `carga las publicaciones y publica el resultado`() = runTest {
        println("INICIO: comprobar la carga de publicaciones.")
        println("PREPARACION: el repositorio falso devolvera una publicacion; no se usa internet.")
        val publicaciones = listOf(publicacion(1, "Poda de manzanos"))
        val viewModel = PublicacionesRemotasViewModel(FakeRepository { publicaciones })

        println("ACCION: iniciar la carga y ejecutar las corrutinas pendientes.")
        assertEquals(PublicacionesUiState.Cargando, viewModel.estado.value)
        advanceUntilIdle()
        println("COMPROBACION: el estado debe contener la publicacion.")
        assertEquals(PublicacionesUiState.Exito(publicaciones), viewModel.estado.value)
        println("CORRECTO: se publicaron las publicaciones esperadas.")
    }

    @Test
    fun `una lista vacia se publica como exito`() = runTest {
        println("INICIO: comprobar una lista vacia.")
        val viewModel = PublicacionesRemotasViewModel(FakeRepository { emptyList() })

        println("ACCION: ejecutar la carga.")
        advanceUntilIdle()
        println("COMPROBACION: una lista vacia debe ser un resultado exitoso.")
        assertEquals(PublicacionesUiState.Exito(emptyList()), viewModel.estado.value)
        println("CORRECTO: se publico una lista vacia sin error.")
    }

    @Test
    fun `un error de red permite reintentar`() = runTest {
        println("INICIO: comprobar el reintento tras un error de red.")
        println("PREPARACION: el repositorio falso fallara una vez y luego devolvera una publicacion.")
        val publicaciones = listOf(publicacion(2, "Cosecha de arandanos"))
        var intentos = 0
        val viewModel = PublicacionesRemotasViewModel(FakeRepository {
            intentos++
            if (intentos == 1) throw IOException("Sin conexion")
            publicaciones
        })

        println("ACCION: ejecutar la primera carga.")
        advanceUntilIdle()
        println("COMPROBACION: la primera carga debe terminar en error.")
        assertEquals(PublicacionesUiState.Error, viewModel.estado.value)
        println("ACCION: reintentar la carga.")
        viewModel.cargarPublicaciones()
        assertEquals(PublicacionesUiState.Cargando, viewModel.estado.value)
        advanceUntilIdle()
        println("COMPROBACION: el reintento debe devolver la publicacion y sumar dos intentos.")
        assertEquals(PublicacionesUiState.Exito(publicaciones), viewModel.estado.value)
        assertEquals(2, intentos)
        println("CORRECTO: el reintento recupero las publicaciones.")
    }

    @Test
    fun `un error http publica error`() = runTest {
        println("INICIO: comprobar un error HTTP.")
        println("PREPARACION: el repositorio falso respondera con un error HTTP 500.")
        val viewModel = PublicacionesRemotasViewModel(FakeRepository {
            throw HttpException(Response.error<Any>(500, "".toResponseBody()))
        })

        println("ACCION: ejecutar la carga.")
        advanceUntilIdle()
        println("COMPROBACION: el ViewModel debe publicar el estado de error.")
        assertEquals(PublicacionesUiState.Error, viewModel.estado.value)
        println("CORRECTO: se publico el estado de error.")
    }

    @Test
    fun `no inicia otra carga mientras hay una pendiente`() = runTest {
        println("INICIO: comprobar que no haya cargas simultaneas.")
        println("PREPARACION: el repositorio falso mantendra la primera carga pendiente.")
        val respuesta = CompletableDeferred<List<PublicacionRemota>>()
        var intentos = 0
        val viewModel = PublicacionesRemotasViewModel(FakeRepository {
            intentos++
            respuesta.await()
        })

        println("ACCION: iniciar la primera carga y solicitar otra antes de terminar.")
        runCurrent()
        viewModel.cargarPublicaciones()
        runCurrent()
        println("COMPROBACION: el repositorio debe haberse llamado solo una vez.")
        assertEquals(1, intentos)
        println("ACCION: completar la primera carga con una lista vacia.")
        respuesta.complete(emptyList())
        advanceUntilIdle()
        assertEquals(PublicacionesUiState.Exito(emptyList()), viewModel.estado.value)
        println("CORRECTO: no se inicio una segunda carga.")
    }

    private fun publicacion(id: Int, titulo: String) = PublicacionRemota(
        id = id,
        titulo = titulo,
        categoria = "Poda",
        comuna = "Curicó",
        ciudad = "Región del Maule",
        fecha_inicio = "15/11/2026",
        fecha_termino = "25/11/2026",
        dias = 10,
        tra_necesitados = 2,
        descripcion = "Descripcion de prueba",
        requisitos = "Experiencia de 1 año",
        info_extra = "Se entrega almuerzo",
        contratante = "Agrícola San Pedro Ltda.",
        paga = 300000,
        creadorId = 1
    )

    private class FakeRepository(
        private val respuesta: suspend () -> List<PublicacionRemota>
    ) : PublicacionesRemotoRepository {
        override suspend fun obtenerPublicaciones(): List<PublicacionRemota> = respuesta()

        // Estos dos no se usan en estas pruebas, pero la interfaz los exige
        override suspend fun crearPublicacion(publicacion: PublicacionRemota): PublicacionRemota = publicacion

        override suspend fun eliminarPublicacion(id: Int) {}
    }
}
