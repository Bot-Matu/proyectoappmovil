package cl.duoc.agricol.repository

import cl.duoc.agricol.model.PublicacionRemota
import cl.duoc.agricol.remote.PublicacionesApi
import retrofit2.HttpException

interface PublicacionesRemotoRepository {
    suspend fun obtenerPublicaciones(): List<PublicacionRemota>
    suspend fun crearPublicacion(publicacion: PublicacionRemota): PublicacionRemota
    suspend fun eliminarPublicacion(id: Int)
}

// Habla con el servidor. Room sigue guardando los datos locales en PublicacionRepository
class PublicacionesRemotoRepositoryImpl(
    private val api: PublicacionesApi
) : PublicacionesRemotoRepository {

    override suspend fun obtenerPublicaciones(): List<PublicacionRemota> =
        api.obtenerPublicaciones()

    override suspend fun crearPublicacion(publicacion: PublicacionRemota): PublicacionRemota =
        api.crearPublicacion(publicacion)

    // La API responde sin contenido útil: si el código no es 2xx, se propaga el error
    override suspend fun eliminarPublicacion(id: Int) {
        val respuesta = api.eliminarPublicacion(id)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }
}
