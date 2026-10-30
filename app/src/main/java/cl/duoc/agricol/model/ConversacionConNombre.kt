package cl.duoc.agricol.model


data class ConversacionConNombre(
    val conversacionId: Int,
    val otroUsuarioId: Int,
    val nombre: String,
    val ultimoMensaje: String,
    val ultimoMensajeTiempo: Long,
    val noLeidos: Int
)