package cl.duoc.agricol.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.agricol.model.Estado
import cl.duoc.agricol.model.Mensaje
import cl.duoc.agricol.repository.MensajeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MensajeViewModel(
    private val mensajeRepository: MensajeRepository,
    private val conversacionId: Int,
    private val emisorIdActual: Int
) : ViewModel() {

    // Lista de mensajes en tiempo real traída directamente desde el repositorio (Room)
    val mensajesState: StateFlow<List<Mensaje>> = mensajeRepository
        .obtenerMensajesPorConversacion(conversacionId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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