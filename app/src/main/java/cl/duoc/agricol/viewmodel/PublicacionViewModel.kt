package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.repository.PublicacionRepository

class PublicacionViewModel : ViewModel() {

    private val repository = PublicacionRepository()

    val publicaciones: List<Publicacion> = repository.obtenerPublicaciones()
}