package cl.duoc.agricol.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "publicacion"
)
data class Publicacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val comuna: String,
    val ciudad: String,
    val fecha_inicio: Long,
    val fecha_termino: Long,
    val tra_necesitados: Int,
    val descripcion: String,
    val info_extra: String,
    val paga: Double,
    val creadorId: Int
)