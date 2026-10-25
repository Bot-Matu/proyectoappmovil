package cl.duoc.agricol.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.duoc.agricol.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario)

    // Devuelve el usuario si el RUT y la contraseña coinciden; si no, null
    @Query("SELECT * FROM usuario WHERE rut = :rut AND password = :password LIMIT 1")
    suspend fun iniciarSesion(rut: String, password: String): Usuario?

    @Query("SELECT * FROM usuario WHERE rut = :rut LIMIT 1")
    suspend fun buscarPorRut(rut: String): Usuario?
}