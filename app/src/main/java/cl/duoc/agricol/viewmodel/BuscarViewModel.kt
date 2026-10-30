package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.repository.PublicacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BuscarViewModel : ViewModel() {

    private val repository = PublicacionRepository()

    // Todas las publicaciones, sin filtrar
    private val todas: List<Publicacion> = repository.obtenerPublicaciones()

    // Lo que el usuario escribió en la barra de búsqueda
    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto

    // La categoría elegida en los filtros ("Todos" no filtra)
    private val _categoria = MutableStateFlow("Todos")
    val categoria: StateFlow<String> = _categoria

    // Los resultados que se muestran en la lista
    private val _resultados = MutableStateFlow(todas)
    val resultados: StateFlow<List<Publicacion>> = _resultados

    fun onTextoChange(nuevoTexto: String) {
        _texto.value = nuevoTexto
        filtrar()
    }

    fun onCategoriaChange(nuevaCategoria: String) {
        _categoria.value = nuevaCategoria
        filtrar()
    }

    // Aplica la categoría y el texto al mismo tiempo
    private fun filtrar() {
        val busqueda = _texto.value.trim().lowercase()
        val categoria = _categoria.value

        _resultados.value = todas.filter { publicacion ->
            val coincideCategoria = categoria == "Todos" || publicacion.categoria == categoria
            val coincideTexto = busqueda.isEmpty() ||
                    publicacion.titulo.lowercase().contains(busqueda) ||
                    publicacion.comuna.lowercase().contains(busqueda) ||
                    publicacion.ciudad.lowercase().contains(busqueda) ||
                    publicacion.requisitos.lowercase().contains(busqueda)
            coincideCategoria && coincideTexto
        }
    }
}