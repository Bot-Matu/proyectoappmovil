package cl.duoc.agricol.model



sealed interface Estado {
    data object Enviando : Estado
    data object Enviado : Estado
    data object Recibido : Estado
    data object Leido : Estado
    data object Error : Estado
}