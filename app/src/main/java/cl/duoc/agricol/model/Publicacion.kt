package cl.duoc.agricol.model
import java.util.UUID
import java.util.Date
import cl.duoc.agricol.model.Usuario
data class Publicacion(val id: UUID = UUID.randomUUID(),
    val titulo:String,
    val comuna:String,
    val ciudad:String,
    val fecha_inicio:Date,
    val fecha_termino:Date,
    val tra_necesitados:Int,
    val descripcion:String,
    val info_extra:String,
    val paga:Double,
    val creador: Usuario
    )
