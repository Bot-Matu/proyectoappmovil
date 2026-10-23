package cl.duoc.agricol.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "publicacion",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["creadorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("creadorId")]
)
data class Publicacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val comuna: String,
    val ciudad: String,
    val fecha_inicio: Long,      // milisegundos (Date.time)
    val fecha_termino: Long,     // milisegundos (Date.time)
    val tra_necesitados: Int,
    val descripcion: String,
    val info_extra: String,
    val paga: Double,
    val creadorId: Int           // id del Usuario que creó la publicación
)