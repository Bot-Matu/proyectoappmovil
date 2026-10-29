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

    // Marcar una sesión como activa
    @Query("UPDATE usuarios SET sesionActiva = 1 WHERE id = :id")
    suspend fun activarSesion(id: Int)

    // Cerrar todas las sesiones
    @Query("UPDATE usuarios SET sesionActiva = 0")
    suspend fun cerrarSesiones()

    // Obtener la sesión activa
    @Query("SELECT * FROM usuarios WHERE sesionActiva = 1 LIMIT 1")
    suspend fun buscarSesionActiva(): Usuario?


}