package cl.duoc.agricol.model

// Modelos que viajan por la API de Xano (login, registro, chat).
// Los nombres de campo son los mismos que tienen las tablas en Xano.

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val rut: String,
    val nro_documento: String,
    val rol: String,
    val celular: Int
)

// Xano responde con el token tanto en login como en signup
data class AuthResponse(
    val authToken: String
)

// Lo que devuelve GET auth/me (nunca incluye la contraseña)
data class UsuarioRemoto(
    val id: Int,
    val name: String,
    val email: String,
    val rut: String,
    val nro_documento: String,
    val rol: String,
    val celular: Int
)

data class ConversacionRemota(
    val id: Int = 0,
    val usuario1Id: Int,
    val usuario2Id: Int,
    val ultimoMensaje: String = "",
    val ultimoMensajeTiempo: Long = 0L
)

data class MensajeRemoto(
    val id: Int = 0,
    val conversacionId: Int,
    val emisorId: Int,
    val contenido: String,
    val tiempo: Long = 0L,
    val estadoMensaje: String = "ENVIADO"
)
