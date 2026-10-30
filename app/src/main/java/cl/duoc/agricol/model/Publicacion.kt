package cl.duoc.agricol.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Una oferta de trabajo agrícola
@Entity(tableName = "publicacion")
data class Publicacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val categoria: String,        // Poda, Riego o Cosecha (la usan los filtros)
    val comuna: String,
    val ciudad: String,
    val fecha_inicio: String,
    val fecha_termino: String,
    val dias: Int,
    val tra_necesitados: Int,
    val descripcion: String,
    val requisitos: String,
    val info_extra: String,
    val contratante: String,
    val paga: Int,
    val creadorId: Int,           // id del Usuario que creó la publicación
    val imagen: Int               // id de la foto en res/drawable
)