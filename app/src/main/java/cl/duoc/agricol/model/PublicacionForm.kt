package cl.duoc.agricol.model

// Lo que el usuario escribe en la pantalla "Nueva publicación" (todo como texto)
data class PublicacionForm(
    val titulo: String = "",
    val categoria: String = "",
    val comuna: String = "",
    val ciudad: String = "",
    val fecha_inicio: String = "",      // formato dd/MM/yyyy
    val fecha_termino: String = "",     // formato dd/MM/yyyy
    val tra_necesitados: String = "",
    val descripcion: String = "",
    val requisitos: String = "",
    val info_extra: String = "",
    val paga: String = ""
)

// Un mensaje de error por cada campo
data class PublicacionFormError(
    val titulo: String? = null,
    val categoria: String? = null,
    val comuna: String? = null,
    val ciudad: String? = null,
    val fecha_inicio: String? = null,
    val fecha_termino: String? = null,
    val tra_necesitados: String? = null,
    val descripcion: String? = null,
    val requisitos: String? = null,
    val paga: String? = null
)
