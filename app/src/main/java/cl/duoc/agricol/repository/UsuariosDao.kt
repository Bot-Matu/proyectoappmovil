package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Usuario
import androidx.room.*
@Dao
interface UsuariosDao {
    @Query("SELECT * FROM usuarios")
    suspend fun getAll(): List<Usuario>

    @Insert
    suspend fun insert(usuario:Usuario)

    @Update
    suspend fun update(usuario:Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)
}