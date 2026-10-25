package cl.duoc.agricol.repository

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cl.duoc.agricol.model.Usuario


object UsuarioRepository {
    private var lista_usuarios by mutableStateOf(listOf<Usuario>())
    val usuarios: List<Usuario> get() = lista_usuarios




    fun obtenerUsuarios(): List<Usuario> = usuarios

    fun crearUsuario(usuario: Usuario):String{
        val nuevoUsuario = usuario
        lista_usuarios=lista_usuarios+nuevoUsuario
        return "Usuario creado con exito"
    }
    fun buscarUsuario(id: Int): Usuario? {
        for (user: Usuario in lista_usuarios) {
            if (user.id == id) {
                return user
            }
        }
        return null
    }

    fun actualizarUsuario(id: Int, nuevoNombre: String, nuevoCelular: Int, nuevoCorreo: String, nuevaContrasena: String, nuevoNro_documento: String) {
        val useract = buscarUsuario(id)
        if (useract != null) {
            val usuarioNew = useract.copy(
                nombre = nuevoNombre,
                celular = nuevoCelular,
                correo = nuevoCorreo,
                contrasena = nuevaContrasena,
                nro_documento = nuevoNro_documento
            )
            lista_usuarios = lista_usuarios.map { user ->
                if (user.id == id) usuarioNew else user
            }
        }
    }


    fun eliminarUsuario(id:Int):String{
        val useract = buscarUsuario(id)
        if (useract != null) {
            lista_usuarios = lista_usuarios - useract
        }
        return "Usuario eliminado Con exito"

    }



}