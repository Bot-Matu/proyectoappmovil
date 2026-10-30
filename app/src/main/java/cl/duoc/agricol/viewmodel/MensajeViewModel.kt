package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.agricol.model.Estado
import cl.duoc.agricol.model.Mensaje
import cl.duoc.agricol.repository.MensajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MensajeViewModel(
    private val mensajeRepository: MensajeRepository,
    private val conversacionId: Int,
    private val emisorIdActual: Int,
    private val otroUsuarioId: Int
) : ViewModel() {

    // Id de quien tiene la sesión (sirve para saber qué burbujas son "mías")
    val miId: Int get() = emisorIdActual

    // Lista de mensajes en tiempo real desde Room
    val mensajesState: StateFlow<List<Mensaje>> = mensajeRepository
        .obtenerMensajesPorConversacion(conversacionId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Nombre de la otra persona (para el título de la pantalla)
    private val _nombreOtro = MutableStateFlow("")
    val nombreOtro: StateFlow<String> = _nombreOtro

    init {
        viewModelScope.launch {
            _nombreOtro.value = mensajeRepository.nombreDeUsuario(otroUsuarioId)
        }
    }

    fun enviarMensaje(texto: String) {
        if (texto.isBlank()) return

        viewModelScope.launch {
            val nuevoMensaje = Mensaje(
                conversacionId = conversacionId,
                emisorId = emisorIdActual,
                contenido = texto.trim(),
                tiempo = System.currentTimeMillis(),
                estadoMensaje = "ENVIADO"
            )
            mensajeRepository.enviarMensaje(nuevoMensaje)
        }
    }

    fun marcarLeidos() {
        viewModelScope.launch {
            mensajeRepository.marcarComoLeidos(conversacionId, emisorIdActual)
        }
    }

    fun cambiarEstado(mensajeId: Int, nuevoEstado: Estado) {
        viewModelScope.launch {
            val estadoString = when (nuevoEstado) {
                Estado.Enviando -> "ENVIANDO"
                Estado.Enviado -> "ENVIADO"
                Estado.Recibido -> "RECIBIDO"
                Estado.Leido -> "LEIDO"
                Estado.Error -> "ERROR"
            }
            mensajeRepository.actualizarEstadoMensaje(mensajeId, estadoString)
        }
    }
}

// Como este ViewModel recibe parámetros, Android necesita una "fábrica" para crearlo
class MensajeViewModelFactory(
    private val repository: MensajeRepository,
    private val conversacionId: Int,
    private val emisorIdActual: Int,
    private val otroUsuarioId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MensajeViewModel(repository, conversacionId, emisorIdActual, otroUsuarioId) as T
}