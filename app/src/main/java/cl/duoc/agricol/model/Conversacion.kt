package cl.duoc.agricol.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversaciones")
data class Conversacion(
    @PrimaryKey(autoGenerate = true) val conversacionId: Int = 0,
    val usuario1Id: Int,
    val usuario2Id: Int,
    val titulo: String = "",
    val ultimoMensaje: String = "",
    val ultimoMensajeTiempo: Long = 0L
)