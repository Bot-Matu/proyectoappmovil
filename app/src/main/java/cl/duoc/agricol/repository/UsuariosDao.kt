package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Usuario
import androidx.room.*

@Dao
interface UsuariosDao {
    @Query("SELECT * FROM usuarios")
    fun getAll(): List<Usuario>

    @Insert
    fun insert(usuario: Usuario): Long

    @Update
    fun update(usuario: Usuario): Int

    @Delete
    fun delete(usuario: Usuario): Int
}
