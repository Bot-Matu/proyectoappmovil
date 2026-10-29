package cl.duoc.agricol.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mensajes",
    foreignKeys = [
        ForeignKey(
            entity = Conversacion::class,
            parentColumns = ["conversacionId"],
            childColumns = ["conversacionId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index(value = ["conversacionId"])]
)
data class Mensaje(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val conversacionId: Int,
    val emisorId: Int,
    val contenido: String,
    val tiempo: Long = System.currentTimeMillis(),
    val estadoMensaje: String = "ENVIADO"
) {
    val estado: Estado
        get() = when (estadoMensaje) {
            "ENVIANDO" -> Estado.Enviando
            "RECIBIDO" -> Estado.Recibido
            "LEIDO" -> Estado.Leido
            "ERROR" -> Estado.Error
            else -> Estado.Enviado
        }
}