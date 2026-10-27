package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.agricol.model.Mensaje
import cl.duoc.agricol.repository.MensajeRepository

class MensajeViewModel : ViewModel() {

    private val repository = MensajeRepository()

    val mensajes: List<Mensaje> = repository.obtenerMensajes()
}