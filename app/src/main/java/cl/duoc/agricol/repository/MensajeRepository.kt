package cl.duoc.agricol.repository

import android.util.Log
import cl.duoc.agricol.model.Conversacion
import cl.duoc.agricol.model.ConversacionConNombre
import cl.duoc.agricol.model.Mensaje
import cl.duoc.agricol.model.MensajeRemoto
import cl.duoc.agricol.model.Usuario
import com.google.gson.JsonParseException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.HttpException
import java.io.IOException

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

    // Ya no hacen falta (con servidor los flujos leen directo de Xano); se dejan para no tocar los ViewModels
    suspend fun sincronizar(usuarioId: Int) {}
    suspend fun sincronizarMensajes(conversacionId: Int) {}
}

// Con ChatRepository remoto, conversaciones y mensajes se leen DIRECTO de Xano (consulta cada
// pocos segundos) y no pasan por Room. Sin remoto funciona como antes, solo con Room.
class MensajeRepositoryImpl(
    private val chatDao: ChatDao,
    private val usuarioDao: UsuarioDao,
    private val remoto: ChatRepository? = null
) : MensajeRepository {

    // Para refrescar la lista de inmediato después de enviar
    private val refrescar = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    // Mensajes que no se pudieron enviar (se muestran con ERROR hasta que se cierre la app)
    private val fallidos = MutableStateFlow<List<Mensaje>>(emptyList())

    // Hasta qué hora (de los mensajes del otro) ya se leyó cada conversación
    private val leidoHasta = MutableStateFlow<Map<Int, Long>>(emptyMap())

    override fun obtenerMensajesPorConversacion(conversacionId: Int): Flow<List<Mensaje>> {
        if (remoto == null) return chatDao.getMessagesForConversation(conversacionId)

        val delServidor = flow {
            while (true) {
                try {
                    val lista = remoto.obtenerMensajes(conversacionId).map { it.aMensaje() }
                    Log.d("AgricolChat", "conv $conversacionId: ${lista.size} mensajes en Xano")
                    emit(lista)
                } catch (e: IOException) {
                    Log.w("AgricolChat", "mensajes sin red", e)
                } catch (e: HttpException) {
                    Log.w("AgricolChat", "mensajes HTTP ${e.code()}", e)
                } catch (e: JsonParseException) {
                    Log.e("AgricolChat", "mensajes: respuesta no válida", e)
                }
                // Espera 3 s o hasta que se envíe un mensaje
                withTimeoutOrNull(3_000) { refrescar.first() }
            }
        }
        return combine(delServidor, fallidos) { lista, errores ->
            (lista + errores.filter { it.conversacionId == conversacionId }).sortedBy { it.tiempo }
        }
    }

    override suspend fun enviarMensaje(mensaje: Mensaje) {
        if (remoto == null) {
            chatDao.sendMessageAndUpdateConversation(mensaje)
            return
        }
        try {
            remoto.enviarMensaje(
                MensajeRemoto(
                    conversacionId = mensaje.conversacionId,
                    emisorId = mensaje.emisorId,
                    contenido = mensaje.contenido,
                    tiempo = mensaje.tiempo,
                    estadoMensaje = mensaje.estadoMensaje
                )
            )
            refrescar.tryEmit(Unit)
        } catch (e: IOException) {
            marcarFallido(mensaje)
        } catch (e: HttpException) {
            Log.w("AgricolChat", "enviar HTTP ${e.code()}", e)
            marcarFallido(mensaje)
        }
    }

    private fun marcarFallido(mensaje: Mensaje) {
        val idLocal = -(mensaje.tiempo % 1_000_000_000L).toInt() - 1
        fallidos.value = fallidos.value + mensaje.copy(id = idLocal, estadoMensaje = "ERROR")
    }

    override suspend fun actualizarEstadoMensaje(mensajeId: Int, nuevoEstado: String) =
        chatDao.updateMessageStatus(mensajeId, nuevoEstado)

    override suspend fun eliminarMensaje(mensaje: Mensaje) =
        chatDao.deleteMessage(mensaje)

    override fun obtenerConversaciones(usuarioId: Int): Flow<List<ConversacionConNombre>> {
        if (remoto == null) return chatDao.observarConversaciones(usuarioId)

        val delServidor = flow {
            while (true) {
                try {
                    emit(armarConversaciones(remoto, usuarioId))
                } catch (e: IOException) {
                    Log.w("AgricolChat", "conversaciones sin red", e)
                } catch (e: HttpException) {
                    Log.w("AgricolChat", "conversaciones HTTP ${e.code()}", e)
                } catch (e: JsonParseException) {
                    Log.e("AgricolChat", "conversaciones: respuesta no válida", e)
                }
                delay(5_000)
            }
        }
        // Al leer un chat, se recalcula el contador de no leídos sin esperar la próxima consulta
        return combine(delServidor, leidoHasta) { lista, leidos ->
            lista.map { c -> c.copy(noLeidos = contarNoLeidos(c, leidos)) }
        }
    }

    // Una fila por conversación con al menos un mensaje, la más reciente primero
    private suspend fun armarConversaciones(api: ChatRepository, yo: Int): List<ConversacionConNombre> {
        val conversaciones = api.obtenerConversaciones(yo)
        val mensajes = api.obtenerTodosLosMensajes().groupBy { it.conversacionId }
        Log.d("AgricolChat", "usuario $yo: ${conversaciones.size} conversaciones en Xano")
        // Se guardan las horas de los mensajes del otro para calcular los no leídos
        tiemposDelOtro = mensajes.mapValues { (_, lista) -> lista.filter { it.emisorId != yo }.map { it.tiempo } }
        return conversaciones.mapNotNull { c ->
            val propios = mensajes[c.id].orEmpty()
            val ultimo = propios.maxByOrNull { it.tiempo } ?: return@mapNotNull null
            val otroId = if (c.usuario1Id == yo) c.usuario2Id else c.usuario1Id
            ConversacionConNombre(
                conversacionId = c.id,
                otroUsuarioId = otroId,
                nombre = usuarioDao.nombrePorId(otroId) ?: "Usuario",
                ultimoMensaje = ultimo.contenido,
                ultimoMensajeTiempo = ultimo.tiempo,
                noLeidos = propios.count { it.emisorId != yo && it.tiempo > (leidoHasta.value[c.id] ?: 0L) }
            )
        }.sortedByDescending { it.ultimoMensajeTiempo }
    }

    // Mensajes del otro posteriores a la última vez que se abrió el chat
    @Volatile private var tiemposDelOtro: Map<Int, List<Long>> = emptyMap()
    private fun contarNoLeidos(c: ConversacionConNombre, leidos: Map<Int, Long>): Int {
        val tiempos = tiemposDelOtro[c.conversacionId] ?: return c.noLeidos
        val limite = leidos[c.conversacionId] ?: 0L
        return tiempos.count { it > limite }
    }

    override fun obtenerContactos(usuarioId: Int): Flow<List<Usuario>> =
        usuarioDao.observarOtros(usuarioId)

    override suspend fun obtenerOCrearConversacion(usuarioId: Int, otroId: Int): Int {
        if (remoto != null) {
            // Si ya hay una conversación entre los dos en Xano se usa esa; si no, se crea
            val existente = remoto.obtenerConversaciones(usuarioId).firstOrNull {
                (it.usuario1Id == usuarioId && it.usuario2Id == otroId) ||
                        (it.usuario1Id == otroId && it.usuario2Id == usuarioId)
            }
            return existente?.id ?: remoto.crearConversacion(usuarioId, otroId).id
        }
        val existente = chatDao.buscarConversacion(usuarioId, otroId)
        if (existente != null) return existente.conversacionId
        return chatDao.insertConversacion(
            Conversacion(usuario1Id = usuarioId, usuario2Id = otroId)
        ).toInt()
    }

    override suspend fun marcarComoLeidos(conversacionId: Int, usuarioId: Int) {
        if (remoto == null) {
            chatDao.marcarLeidos(conversacionId, usuarioId)
            return
        }
        // Xano no tiene endpoint para cambiar el estado: se recuerda en el teléfono hasta qué mensaje se leyó
        try {
            val delOtro = remoto.obtenerMensajes(conversacionId)
                .filter { it.emisorId != usuarioId }.map { it.tiempo }
            val ultimo = delOtro.maxOrNull() ?: return
            leidoHasta.value = leidoHasta.value + (conversacionId to ultimo)
        } catch (e: IOException) {
        } catch (e: HttpException) {
        } catch (e: JsonParseException) {
        }
    }

    override suspend fun nombreDeUsuario(usuarioId: Int): String =
        usuarioDao.nombrePorId(usuarioId) ?: "Usuario"

    private fun MensajeRemoto.aMensaje() = Mensaje(
        id = id,
        conversacionId = conversacionId,
        emisorId = emisorId,
        contenido = contenido,
        tiempo = tiempo,
        estadoMensaje = estadoMensaje
    )
}
