package cl.duoc.agricol.model

// Una publicación tal como viaja por la API.
// No lleva imagen: la app la elige según la categoría.
data class PublicacionRemota(
    val id: Int = 0,
    val titulo: String,
    val categoria: String,
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
    val creadorId: Int
)