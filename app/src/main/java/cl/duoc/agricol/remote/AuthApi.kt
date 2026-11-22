package cl.duoc.agricol.remote

import cl.duoc.agricol.model.AuthResponse
import cl.duoc.agricol.model.LoginRequest
import cl.duoc.agricol.model.SignupRequest
import cl.duoc.agricol.model.UsuarioRemoto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    // Inicia sesión con correo y contraseña; devuelve el token
    @POST("auth/login")
    suspend fun login(@Body datos: LoginRequest): AuthResponse

    // Crea la cuenta; devuelve el token
    @POST("auth/signup")
    suspend fun signup(@Body datos: SignupRequest): AuthResponse

    // Devuelve el usuario dueño del token ("Bearer <token>")
    @GET("auth/me")
    suspend fun obtenerPerfil(@Header("Authorization") token: String): UsuarioRemoto
}
