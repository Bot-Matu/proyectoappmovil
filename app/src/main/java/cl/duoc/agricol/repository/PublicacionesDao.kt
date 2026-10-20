package cl.duoc.agricol.repository
import cl.duoc.agricol.model.Publicacion
import androidx.room.*



@Dao
interface PublicacionesDao {

    suspend fun getAll(): List<Publicacion>

    @Insert
    suspend fun insert(publicacion:Publicacion)

    @Update
    suspend fun update(publicacion:Publicacion)

    @Delete
    suspend fun delete(publicacion:Publicacion)
}
