package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun registrar(usuario: Usuario) = usuarioDao.insertar(usuario)

    suspend fun iniciarSesion(rut: String, contrasena: String): Usuario? =
        usuarioDao.iniciarSesion(rut, contrasena)

    suspend fun existeRut(rut: String): Boolean =
        usuarioDao.buscarPorRut(rut) != null

    suspend fun guardarSesion(usuario: Usuario) {
        usuarioDao.cerrarSesiones()
        usuarioDao.activarSesion(usuario.id)
    }

    suspend fun cerrarSesion() = usuarioDao.cerrarSesiones()

    suspend fun obtenerSesionActiva(): Usuario? = usuarioDao.buscarSesionActiva()



}