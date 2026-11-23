package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.agricol.model.Notificacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificacionViewModel : ViewModel() {

    // Avisos que se ven en el panel de la campanita (parte vacía)
    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones

    // true mientras el usuario no haya abierto el panel (muestra el punto en la campana)
    private val _hayNoLeidas = MutableStateFlow(false)
    val hayNoLeidas: StateFlow<Boolean> = _hayNoLeidas

    // Agrega un aviso arriba de la lista
    fun agregar(titulo: String, detalle: String) {
        val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val nueva = Notificacion(
            id = _notificaciones.value.size + 1,
            titulo = titulo,
            detalle = detalle,
            hora = hora
        )
        _notificaciones.value = listOf(nueva) + _notificaciones.value
        _hayNoLeidas.value = true
    }

    fun marcarComoLeidas() {
        _hayNoLeidas.value = false
    }
}