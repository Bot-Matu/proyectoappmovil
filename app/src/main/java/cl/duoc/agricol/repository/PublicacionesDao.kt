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

    // Sincronización con el servidor: actualiza las que existen y agrega las nuevas
    @Upsert
    suspend fun upsertVarias(publicaciones: List<Publicacion>)

    // Quita de Room las que ya no existen en el servidor
    @Query("DELETE FROM publicacion WHERE id NOT IN (:ids)")
    suspend fun borrarExcepto(ids: List<Int>)

    @Update
    suspend fun update(publicacion: Publicacion)

    @Delete
    suspend fun delete(publicacion: Publicacion)
}
