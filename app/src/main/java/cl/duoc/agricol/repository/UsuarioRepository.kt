package cl.duoc.agricol.repository

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cl.duoc.agricol.model.Usuario
import java.util.UUID

object UsuarioRepository {
    private var lista_usuarios by mutableStateOf(listOf<Usuario>())
    val usuarios: List<Usuario> get() = lista_usuarios


    fun crearNuevoUsuario(
        id: UUID = UUID.randomUUID(),
        nombre: String,
        telefono: Int,
        correo: String,
        contraseña:String,
        rut:String,
        nro_documento:String

    ): Usuario {
        val usuario = Usuario(
            id = id,
            nombre = nombre,
            celular = telefono,
            correo = correo,
            contraseña=contraseña,
            rut=rut,
            nro_documento=nro_documento
        )
        lista_usuarios = lista_usuarios + usuario
        return usuario
    }

    fun obtenerUsuarios(): List<Usuario> = usuarios

}