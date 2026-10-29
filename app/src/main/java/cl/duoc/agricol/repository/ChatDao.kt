package cl.duoc.agricol.repository
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    // AQUÍ ESTABA EL DETALLE: Se usa 'conversacionId' en lugar de 'id' para la tabla de conversaciones
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
}