package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Usuario
import androidx.room.*
@Dao
interface UsuariosDao {
    @Query("SELECT * FROM usuarios")
    suspend fun getAll(): List<Usuario>

    @Insert
    suspend fun insert(publicacion:Usuario)

    @Update
    suspend fun update(publicacion:Usuario)

    @Delete
    suspend fun delete(publicacion: Usuario)
}