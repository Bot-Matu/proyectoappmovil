package cl.duoc.agricol.repository

import cl.duoc.agricol.model.ConversacionRemota
import cl.duoc.agricol.model.MensajeRemoto
import cl.duoc.agricol.remote.ChatApi

interface ChatRepository {
    suspend fun obtenerConversaciones(usuarioId: Int): List<ConversacionRemota>
    suspend fun crearConversacion(usuario1Id: Int, usuario2Id: Int): ConversacionRemota
    suspend fun obtenerMensajes(conversacionId: Int): List<MensajeRemoto>
    suspend fun enviarMensaje(mensaje: MensajeRemoto): MensajeRemoto
}

class ChatRepositoryImpl(
    private val api: ChatApi
) : ChatRepository {

    // La API entrega todas; aquí quedan solo las del usuario
    override suspend fun obtenerConversaciones(usuarioId: Int): List<ConversacionRemota> =
        api.obtenerConversaciones().filter {
            it.usuario1Id == usuarioId || it.usuario2Id == usuarioId
        }

    override suspend fun crearConversacion(usuario1Id: Int, usuario2Id: Int): ConversacionRemota =
        api.crearConversacion(
            ConversacionRemota(usuario1Id = usuario1Id, usuario2Id = usuario2Id)
        )

    // La API entrega todos; aquí quedan solo los de esa conversación
    override suspend fun obtenerMensajes(conversacionId: Int): List<MensajeRemoto> =
        api.obtenerMensajes().filter { it.conversacionId == conversacionId }

    override suspend fun enviarMensaje(mensaje: MensajeRemoto): MensajeRemoto =
        api.enviarMensaje(mensaje)
}
