package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.agricol.model.ConversacionConNombre
import cl.duoc.agricol.model.Usuario
import cl.duoc.agricol.repository.MensajeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel de la lista de Mensajes. Usa el mismo usuarioActual del UsuarioViewModel,
// así si se cierra sesión y entra otra persona, la lista cambia sola.
@OptIn(ExperimentalCoroutinesApi::class)
class ConversacionesViewModel(
    private val repository: MensajeRepository,
    private val usuarioActual: StateFlow<Usuario?>
) : ViewModel() {

    val conversaciones: StateFlow<List<ConversacionConNombre>> =
        usuarioActual
            .flatMapLatest { yo ->
                if (yo == null) flowOf(emptyList()) else repository.obtenerConversaciones(yo.id)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Personas con las que se puede empezar un chat nuevo
    val contactos: StateFlow<List<Usuario>> =
        usuarioActual
            .flatMapLatest { yo ->
                if (yo == null) flowOf(emptyList()) else repository.obtenerContactos(yo.id)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Busca el chat con esa persona (o lo crea) y avisa con su id para navegar
    fun abrirChat(otroId: Int, onListo: (conversacionId: Int) -> Unit) {
        val yo = usuarioActual.value ?: return
        viewModelScope.launch {
            onListo(repository.obtenerOCrearConversacion(yo.id, otroId))
        }
    }
}