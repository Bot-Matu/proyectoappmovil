package cl.duoc.agricol.repository

import cl.duoc.agricol.model.SignupRequest
import cl.duoc.agricol.model.Usuario

// Si recibe un AuthRepository, registro y login van a Xano y Room queda como copia local
// de la sesión. Sin AuthRepository funciona igual que antes (solo Room).
class UsuarioRepository(
    private val usuarioDao: UsuarioDao,
    private val auth: AuthRepository? = null
) {

    val usaServidor: Boolean get() = auth != null

    suspend fun registrar(usuario: Usuario) {
        if (auth == null) {
            usuarioDao.insertar(usuario)
        } else {
            auth.registrar(
                SignupRequest(
                    name = usuario.nombre,
                    email = usuario.correo,
                    password = usuario.contrasena,
                    rut = usuario.rut,
                    nro_documento = usuario.nro_documento,
                    rol = usuario.rol,
                    celular = usuario.celular
                )
            )
        }
    }

    // Con servidor, "identificador" es el correo; sin servidor, el RUT
    suspend fun iniciarSesion(identificador: String, contrasena: String): Usuario? {
        if (auth == null) return usuarioDao.iniciarSesion(identificador, contrasena)

        val token = auth.iniciarSesion(identificador, contrasena)
        val perfil = auth.obtenerPerfil(token)
        val usuario = Usuario(
            id = perfil.id,                 // el id de Xano: lo usan publicaciones y chat
            nombre = perfil.name,
            celular = perfil.celular,
            correo = perfil.email,
            contrasena = "",                // la contraseña no se guarda en el teléfono
            rut = perfil.rut,
            nro_documento = perfil.nro_documento,
            rol = perfil.rol,
            token = token
        )
        usuarioDao.upsert(usuario)
        return usuario
    }

    // Con servidor, la validación de duplicados la hace Xano al registrar
    suspend fun existeRut(rut: String): Boolean =
        if (auth != null) false else usuarioDao.buscarPorRut(rut) != null

    suspend fun guardarSesion(usuario: Usuario) {
        usuarioDao.cerrarSesiones()
        usuarioDao.activarSesion(usuario.id)
    }

    suspend fun cerrarSesion() = usuarioDao.cerrarSesiones()

    suspend fun obtenerSesionActiva(): Usuario? = usuarioDao.buscarSesionActiva()
}
