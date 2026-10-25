package cl.duoc.agricol.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.duoc.agricol.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    fun insertar(usuario: Usuario): Long

    @Query("SELECT * FROM usuario WHERE rut = :rut AND password = :password LIMIT 1")
    fun iniciarSesion(rut: String, password: String): Usuario?

    @Query("SELECT * FROM usuario WHERE rut = :rut LIMIT 1")
    fun buscarPorRut(rut: String): Usuario?
}
