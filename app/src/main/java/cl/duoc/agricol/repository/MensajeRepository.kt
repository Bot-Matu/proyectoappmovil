package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Conversacion
import cl.duoc.agricol.model.ConversacionConNombre
import cl.duoc.agricol.model.Mensaje
import cl.duoc.agricol.model.Usuario
import kotlinx.coroutines.flow.Flow

interface MensajeRepository {
    fun obtenerMensajesPorConversacion(conversacionId: Int): Flow<List<Mensaje>>
    suspend fun enviarMensaje(mensaje: Mensaje)
    suspend fun actualizarEstadoMensaje(mensajeId: Int, nuevoEstado: String)
    suspend fun eliminarMensaje(mensaje: Mensaje)

    // Nuevo
    fun obtenerConversaciones(usuarioId: Int): Flow<List<ConversacionConNombre>>
    fun obtenerContactos(usuarioId: Int): Flow<List<Usuario>>
    suspend fun obtenerOCrearConversacion(usuarioId: Int, otroId: Int): Int
    suspend fun marcarComoLeidos(conversacionId: Int, usuarioId: Int)
    suspend fun nombreDeUsuario(usuarioId: Int): String
}

// Hoy guarda todo en Room. Si algún día usan un servidor (Firebase, API),
// solo se cambia esta clase: el ViewModel y las pantallas no se enteran.
class MensajeRepositoryImpl(
    private val chatDao: ChatDao,
    private val usuarioDao: UsuarioDao
) : MensajeRepository {

    override fun obtenerMensajesPorConversacion(conversacionId: Int): Flow<List<Mensaje>> =
        chatDao.getMessagesForConversation(conversacionId)

    // Guarda el mensaje y actualiza el "último mensaje" de la conversación (tu @Transaction)
    override suspend fun enviarMensaje(mensaje: Mensaje) =
        chatDao.sendMessageAndUpdateConversation(mensaje)

    override suspend fun actualizarEstadoMensaje(mensajeId: Int, nuevoEstado: String) =
        chatDao.updateMessageStatus(mensajeId, nuevoEstado)

    override suspend fun eliminarMensaje(mensaje: Mensaje) =
        chatDao.deleteMessage(mensaje)

    override fun obtenerConversaciones(usuarioId: Int): Flow<List<ConversacionConNombre>> =
        chatDao.observarConversaciones(usuarioId)

    override fun obtenerContactos(usuarioId: Int): Flow<List<Usuario>> =
        usuarioDao.observarOtros(usuarioId)

    override suspend fun obtenerOCrearConversacion(usuarioId: Int, otroId: Int): Int {
        val existente = chatDao.buscarConversacion(usuarioId, otroId)
        if (existente != null) return existente.conversacionId
        return chatDao.insertConversacion(
            Conversacion(usuario1Id = usuarioId, usuario2Id = otroId)
        ).toInt()
    }

    override suspend fun marcarComoLeidos(conversacionId: Int, usuarioId: Int) =
        chatDao.marcarLeidos(conversacionId, usuarioId)

    override suspend fun nombreDeUsuario(usuarioId: Int): String =
        usuarioDao.nombrePorId(usuarioId) ?: "Usuario"
}