package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.agricol.model.Notificacion
import cl.duoc.agricol.repository.NotificacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificacionViewModel : ViewModel() {

    private val repository = NotificacionRepository()

    val notificaciones: List<Notificacion> = repository.obtenerNotificaciones()

    // true mientras el usuario no haya abierto el panel (muestra el punto en la campana)
    private val _hayNoLeidas = MutableStateFlow(true)
    val hayNoLeidas: StateFlow<Boolean> = _hayNoLeidas

    fun marcarComoLeidas() {
        _hayNoLeidas.value = false
    }
}