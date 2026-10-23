package cl.duoc.agricol.repository

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cl.duoc.agricol.model.Usuario


object UsuarioRepository {
    private var lista_usuarios by mutableStateOf(listOf<Usuario>())
    val usuarios: List<Usuario> get() = lista_usuarios
    val usuarios_registrados: MutableList<Usuario> get() = usuarios_registrados
    private var rotation_Id = 1

    fun crearNuevoUsuario(
        id: Int,
        nombre: String,
        telefono: Int,
        correo: String,
        contrasena:String,
        rut:String,
        nro_documento:String

    ): Usuario {
        val usuario = Usuario(
            id = rotation_Id++,
            nombre = nombre,
            celular = telefono,
            correo = correo,
            contrasena=contrasena,
            rut=rut,
            nro_documento=nro_documento
        )
        lista_usuarios = lista_usuarios + usuario
        return usuario
    }

    fun obtenerUsuarios(): List<Usuario> = usuarios


}