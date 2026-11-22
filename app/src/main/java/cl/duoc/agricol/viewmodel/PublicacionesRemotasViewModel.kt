package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.agricol.model.PublicacionRemota
import cl.duoc.agricol.repository.PublicacionesRemotoRepository
import java.io.IOException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface PublicacionesUiState {
    data object Cargando : PublicacionesUiState
    data class Exito(val publicaciones: List<PublicacionRemota>) : PublicacionesUiState
    data object Error : PublicacionesUiState
}

// Carga las publicaciones desde Xano y publica el estado (mismo patrón del proyecto de ejemplo)
class PublicacionesRemotasViewModel(
    private val repository: PublicacionesRemotoRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<PublicacionesUiState>(PublicacionesUiState.Cargando)
    val estado = _estado.asStateFlow()
    private var carga: Job? = null

    init {
        cargarPublicaciones()
    }

    fun cargarPublicaciones() {
        if (carga?.isActive == true) return
        _estado.value = PublicacionesUiState.Cargando
        carga = viewModelScope.launch {
            try {
                _estado.value = PublicacionesUiState.Exito(repository.obtenerPublicaciones())
            } catch (e: IOException) {
                _estado.value = PublicacionesUiState.Error
            } catch (e: HttpException) {
                _estado.value = PublicacionesUiState.Error
            }
        }
    }

    class Factory(private val repository: PublicacionesRemotoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(PublicacionesRemotasViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return PublicacionesRemotasViewModel(repository) as T
        }
    }
}
