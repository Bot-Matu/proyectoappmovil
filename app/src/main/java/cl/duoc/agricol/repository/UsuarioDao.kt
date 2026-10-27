package cl.duoc.agricol.repository

import androidx.room.*
import cl.duoc.agricol.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario)

    // Devuelve el usuario con ese RUT, o null si no existe
    @Query("SELECT * FROM usuarios WHERE rut = :rut LIMIT 1")
    suspend fun buscarPorRut(rut: String): Usuario?

    // Devuelve el usuario solo si el RUT y la contraseña coinciden
    @Query("SELECT * FROM usuarios WHERE rut = :rut AND contrasena = :contrasena LIMIT 1")
    suspend fun iniciarSesion(rut: String, contrasena: String): Usuario?
}