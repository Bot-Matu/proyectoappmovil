package cl.duoc.agricol.repository

import cl.duoc.agricol.model.PublicacionRemota
import cl.duoc.agricol.remote.PublicacionesApi

interface PublicacionesRemotoRepository {
    suspend fun obtenerPublicaciones(): List<PublicacionRemota>
    suspend fun crearPublicacion(publicacion: PublicacionRemota): PublicacionRemota
}

// Habla con el servidor. Room sigue guardando los datos locales en PublicacionRepository
class PublicacionesRemotoRepositoryImpl(
    private val api: PublicacionesApi
) : PublicacionesRemotoRepository {

    override suspend fun obtenerPublicaciones(): List<PublicacionRemota> =
        api.obtenerPublicaciones()

    override suspend fun crearPublicacion(publicacion: PublicacionRemota): PublicacionRemota =
        api.crearPublicacion(publicacion)
}