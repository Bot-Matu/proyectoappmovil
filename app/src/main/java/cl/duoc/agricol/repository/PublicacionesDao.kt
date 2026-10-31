package cl.duoc.agricol.repository

import androidx.room.*
import cl.duoc.agricol.model.Publicacion
import kotlinx.coroutines.flow.Flow

@Dao
interface PublicacionesDao {

    // Flow: cada vez que se inserta una publicación, las pantallas se actualizan solas
    @Query("SELECT * FROM publicacion ORDER BY id DESC")
    fun observarTodas(): Flow<List<Publicacion>>

    @Query("SELECT COUNT(*) FROM publicacion")
    suspend fun contar(): Int

    @Insert
    suspend fun insert(publicacion: Publicacion)

    @Insert
    suspend fun insertarVarias(publicaciones: List<Publicacion>)

    @Update
    suspend fun update(publicacion: Publicacion)

    @Delete
    suspend fun delete(publicacion: Publicacion)
}
