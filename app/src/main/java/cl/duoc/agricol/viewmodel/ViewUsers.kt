package cl.duoc.agricol.viewmodel

import cl.duoc.agricol.model.Usuario
import cl.duoc.agricol.model.UsuarioForm
import cl.duoc.agricol.repository.UsuarioRepository

class ViewUsers(private val repositorio: UsuarioRepository) {
    fun traerListaUsers(): List<Usuario>{
        return repositorio.obtenerUsuarios()
    }

}

