package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun registrar(usuario: Usuario) = usuarioDao.insertar(usuario)

    suspend fun iniciarSesion(rut: String, password: String): Usuario? =
        usuarioDao.iniciarSesion(rut, password)

    suspend fun existeRut(rut: String): Boolean =
        usuarioDao.buscarPorRut(rut) != null
}