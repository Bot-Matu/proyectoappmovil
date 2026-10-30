package cl.duoc.agricol.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import cl.duoc.agricol.model.Conversacion
import cl.duoc.agricol.model.ConversacionConNombre
import cl.duoc.agricol.model.Mensaje
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query("SELECT * FROM mensajes WHERE conversacionId = :conversacionId ORDER BY tiempo ASC")
    fun getMessagesForConversation(conversacionId: Int): Flow<List<Mensaje>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(mensaje: Mensaje): Long

    @Query("UPDATE mensajes SET estadoMensaje = :nuevoEstado WHERE id = :mensajeId")
    suspend fun updateMessageStatus(mensajeId: Int, nuevoEstado: String)

    @Delete
    suspend fun deleteMessage(mensaje: Mensaje)

    @Query("UPDATE conversaciones SET ultimoMensaje = :ultimoMensaje, ultimoMensajeTiempo = :tiempo WHERE conversacionId = :conversacionId")
    suspend fun updateLastMessage(conversacionId: Int, ultimoMensaje: String, tiempo: Long)

    @Transaction
    suspend fun sendMessageAndUpdateConversation(mensaje: Mensaje) {
        insertMessage(mensaje)
        updateLastMessage(
            conversacionId = mensaje.conversacionId,
            ultimoMensaje = mensaje.contenido,
            tiempo = mensaje.tiempo
        )
    }

    // ---------- Nuevo ----------

    // ¿Ya existe un chat entre estas dos personas? (da igual quién sea la 1 y la 2)
    @Query(
        """
        SELECT * FROM conversaciones
        WHERE (usuario1Id = :a AND usuario2Id = :b) OR (usuario1Id = :b AND usuario2Id = :a)
        LIMIT 1
        """
    )
    suspend fun buscarConversacion(a: Int, b: Int): Conversacion?

    @Insert
    suspend fun insertConversacion(conversacion: Conversacion): Long

    // Lista de Mensajes: una fila por chat, con el nombre de la OTRA persona.
    // Solo aparecen los chats que ya tienen al menos un mensaje.
    @Query(
        """
        SELECT c.conversacionId AS conversacionId,
               u.id AS otroUsuarioId,
               u.nombre AS nombre,
               c.ultimoMensaje AS ultimoMensaje,
               c.ultimoMensajeTiempo AS ultimoMensajeTiempo,
               (SELECT COUNT(*) FROM mensajes m
                WHERE m.conversacionId = c.conversacionId
                  AND m.emisorId != :yoId
                  AND m.estadoMensaje != 'LEIDO') AS noLeidos
        FROM conversaciones c
        JOIN usuarios u
          ON u.id = CASE WHEN c.usuario1Id = :yoId THEN c.usuario2Id ELSE c.usuario1Id END
        WHERE (c.usuario1Id = :yoId OR c.usuario2Id = :yoId)
          AND c.ultimoMensajeTiempo > 0
        ORDER BY c.ultimoMensajeTiempo DESC
        """
    )
    fun observarConversaciones(yoId: Int): Flow<List<ConversacionConNombre>>

    // Al abrir un chat: los mensajes de la otra persona pasan a LEIDO
    @Query("UPDATE mensajes SET estadoMensaje = 'LEIDO' WHERE conversacionId = :conversacionId AND emisorId != :yoId AND estadoMensaje != 'LEIDO'")
    suspend fun marcarLeidos(conversacionId: Int, yoId: Int)
}