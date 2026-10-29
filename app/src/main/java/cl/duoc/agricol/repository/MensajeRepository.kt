package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Mensaje
import kotlinx.coroutines.flow.Flow

interface MensajeRepository {
    fun obtenerMensajesPorConversacion(conversacionId: Int): Flow<List<Mensaje>>
    suspend fun enviarMensaje(mensaje: Mensaje)
    suspend fun actualizarEstadoMensaje(mensajeId: Int, nuevoEstado: String)
    suspend fun eliminarMensaje(mensaje: Mensaje)
}