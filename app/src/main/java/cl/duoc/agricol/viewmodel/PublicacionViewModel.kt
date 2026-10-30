package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.repository.PublicacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PublicacionViewModel : ViewModel() {

    private val repository = PublicacionRepository()

    // Todas las publicaciones, sin filtrar
    private val todas: List<Publicacion> = repository.obtenerPublicaciones()

    // La categoría elegida en los filtros ("Todos" muestra todo)
    private val _categoria = MutableStateFlow("Todos")
    val categoria: StateFlow<String> = _categoria

    // Lo que se muestra en la lista, ya filtrado
    private val _publicaciones = MutableStateFlow(todas)
    val publicaciones: StateFlow<List<Publicacion>> = _publicaciones

    fun onCategoriaChange(nuevaCategoria: String) {
        _categoria.value = nuevaCategoria
        _publicaciones.value = if (nuevaCategoria == "Todos") {
            todas
        } else {
            todas.filter { it.categoria == nuevaCategoria }
        }
    }
}