package cl.duoc.agricol.repository

import cl.duoc.agricol.model.LoginRequest
import cl.duoc.agricol.model.SignupRequest
import cl.duoc.agricol.model.UsuarioRemoto
import cl.duoc.agricol.remote.AuthApi

interface AuthRepository {
    // Devuelven el token que entrega Xano
    suspend fun iniciarSesion(email: String, password: String): String
    suspend fun registrar(datos: SignupRequest): String
    suspend fun obtenerPerfil(token: String): UsuarioRemoto
}

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun iniciarSesion(email: String, password: String): String =
        api.login(LoginRequest(email, password)).authToken

    override suspend fun registrar(datos: SignupRequest): String =
        api.signup(datos).authToken

    // Recibe el token solo; aquí se le agrega el prefijo "Bearer "
    override suspend fun obtenerPerfil(token: String): UsuarioRemoto =
        api.obtenerPerfil("Bearer $token")
}
