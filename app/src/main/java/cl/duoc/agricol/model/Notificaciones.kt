package cl.duoc.agricol.model

// Un aviso que aparece en el panel de la campanita
data class Notificacion(
    val id: Int,
    val titulo: String,
    val detalle: String,
    val hora: String
)