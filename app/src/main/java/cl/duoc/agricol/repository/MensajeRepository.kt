package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Conversacion
import cl.duoc.agricol.model.ConversacionConNombre
import cl.duoc.agricol.model.Mensaje
import cl.duoc.agricol.model.Usuario
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import retrofit2.HttpException
import cl.duoc.agricol.model.MensajeRemoto

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

    // Traen lo nuevo del servidor a Room (sin servidor no hacen nada)
    suspend fun sincronizar(usuarioId: Int) {}
    suspend fun sincronizarMensajes(conversacionId: Int) {}
}

// Room sigue alimentando las pantallas. Si recibe el ChatRepository remoto, las conversaciones
// y mensajes se envían a Xano y lo nuevo se baja a Room con sincronizar().
class MensajeRepositoryImpl(
    private val chatDao: ChatDao,
    private val usuarioDao: UsuarioDao,
    private val remoto: ChatRepository? = null
) : MensajeRepository {

    override fun obtenerMensajesPorConversacion(conversacionId: Int): Flow<List<Mensaje>> =
        chatDao.getMessagesForConversation(conversacionId)

    // Guarda el mensaje y actualiza el "último mensaje" de la conversación (tu @Transaction)
    override suspend fun enviarMensaje(mensaje: Mensaje) {
        if (remoto == null) {
            chatDao.sendMessageAndUpdateConversation(mensaje)
            return
        }
        try {
            val enviado = remoto.enviarMensaje(
                MensajeRemoto(
                    conversacionId = mensaje.conversacionId,
                    emisorId = mensaje.emisorId,
                    contenido = mensaje.contenido,
                    tiempo = mensaje.tiempo,
                    estadoMensaje = mensaje.estadoMensaje
                )
            )
            // Queda en Room con el id de Xano
            chatDao.sendMessageAndUpdateConversation(mensaje.copy(id = enviado.id))
        } catch (e: IOException) {
            guardarConError(mensaje)
        } catch (e: HttpException) {
            guardarConError(mensaje)
        }
    }

    // Si no se pudo enviar, el mensaje queda en Room marcado con ERROR (id negativo para no chocar con los de Xano)
    private suspend fun guardarConError(mensaje: Mensaje) {
        val idLocal = -(mensaje.tiempo % 1_000_000_000L).toInt() - 1
        chatDao.sendMessageAndUpdateConversation(
            mensaje.copy(id = idLocal, estadoMensaje = "ERROR")
        )
    }

    override suspend fun actualizarEstadoMensaje(mensajeId: Int, nuevoEstado: String) =
        chatDao.updateMessageStatus(mensajeId, nuevoEstado)

    override suspend fun eliminarMensaje(mensaje: Mensaje) =
        chatDao.deleteMessage(mensaje)

    override fun obtenerConversaciones(usuarioId: Int): Flow<List<ConversacionConNombre>> =
        chatDao.observarConversaciones(usuarioId)

    override fun obtenerContactos(usuarioId: Int): Flow<List<Usuario>> =
        usuarioDao.observarOtros(usuarioId)

    override suspend fun obtenerOCrearConversacion(usuarioId: Int, otroId: Int): Int {
        if (remoto != null) {
            // Primero se trae lo que ya existe en el servidor; si no hay red se usa lo de Room
            try {
                sincronizarConversaciones(usuarioId)
            } catch (e: IOException) {
            } catch (e: HttpException) {
            }
        }
        val existente = chatDao.buscarConversacion(usuarioId, otroId)
        if (existente != null) return existente.conversacionId
        if (remoto != null) {
            val creada = remoto.crearConversacion(usuarioId, otroId)
            chatDao.upsertConversacion(
                Conversacion(
                    conversacionId = creada.id,
                    usuario1Id = creada.usuario1Id,
                    usuario2Id = creada.usuario2Id
                )
            )
            return creada.id
        }
        return chatDao.insertConversacion(
            Conversacion(usuario1Id = usuarioId, usuario2Id = otroId)
        ).toInt()
    }

    override suspend fun marcarComoLeidos(conversacionId: Int, usuarioId: Int) =
        chatDao.marcarLeidos(conversacionId, usuarioId)

    override suspend fun nombreDeUsuario(usuarioId: Int): String =
        usuarioDao.nombrePorId(usuarioId) ?: "Usuario"

    // ---------- Sincronización con Xano ----------

    // Conversaciones del usuario + sus mensajes (lo que alimenta la pantalla Mensajes)
    override suspend fun sincronizar(usuarioId: Int) {
        if (remoto == null) return
        sincronizarConversaciones(usuarioId).forEach { sincronizarMensajes(it) }
    }

    // Devuelve los ids de las conversaciones del usuario ya guardadas en Room
    private suspend fun sincronizarConversaciones(usuarioId: Int): List<Int> {
        if (remoto == null) return emptyList()
        return remoto.obtenerConversaciones(usuarioId).map { r ->
            // Se conserva el último mensaje que ya calculó Room
            val local = chatDao.buscarConversacion(r.usuario1Id, r.usuario2Id)
            chatDao.upsertConversacion(
                Conversacion(
                    conversacionId = r.id,
                    usuario1Id = r.usuario1Id,
                    usuario2Id = r.usuario2Id,
                    ultimoMensaje = local?.ultimoMensaje ?: r.ultimoMensaje,
                    ultimoMensajeTiempo = maxOf(local?.ultimoMensajeTiempo ?: 0L, r.ultimoMensajeTiempo)
                )
            )
            r.id
        }
    }

    // Guarda en Room solo los mensajes nuevos (los que ya están conservan su estado)
    override suspend fun sincronizarMensajes(conversacionId: Int) {
        if (remoto == null) return
        val remotos = remoto.obtenerMensajes(conversacionId)
        val yaGuardados = chatDao.idsMensajes(conversacionId).toSet()
        remotos.filter { it.id !in yaGuardados }.forEach {
            chatDao.insertMessage(
                Mensaje(
                    id = it.id,
                    conversacionId = it.conversacionId,
                    emisorId = it.emisorId,
                    contenido = it.contenido,
                    tiempo = it.tiempo,
                    estadoMensaje = it.estadoMensaje
                )
            )
        }
        // La lista de Mensajes muestra el último mensaje de cada chat
        remotos.filter { it.tiempo > 0 }.maxByOrNull { it.tiempo }?.let {
            chatDao.updateLastMessage(conversacionId, it.contenido, it.tiempo)
        }
    }
}