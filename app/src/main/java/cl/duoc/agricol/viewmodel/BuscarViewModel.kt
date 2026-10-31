package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.repository.PublicacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BuscarViewModel(repository: PublicacionRepository) : ViewModel() {

    // Lo que el usuario escribió en la barra de búsqueda
    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto

    // La categoría elegida en los filtros ("Todos" no filtra)
    private val _categoria = MutableStateFlow("Todos")
    val categoria: StateFlow<String> = _categoria

    // Los resultados: publicaciones de Room + texto + categoría, todo junto
    val resultados: StateFlow<List<Publicacion>> =
        combine(repository.publicaciones, _texto, _categoria) { todas, texto, categoria ->
            val busqueda = texto.trim().lowercase()
            todas.filter { publicacion ->
                val coincideCategoria = categoria == "Todos" || publicacion.categoria == categoria
                val coincideTexto = busqueda.isEmpty() ||
                        publicacion.titulo.lowercase().contains(busqueda) ||
                        publicacion.comuna.lowercase().contains(busqueda) ||
                        publicacion.ciudad.lowercase().contains(busqueda) ||
                        publicacion.requisitos.lowercase().contains(busqueda)
                coincideCategoria && coincideTexto
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onTextoChange(nuevoTexto: String) {
        _texto.value = nuevoTexto
    }

    fun onCategoriaChange(nuevaCategoria: String) {
        _categoria.value = nuevaCategoria
    }
}
