package cl.duoc.agricol.repository
import cl.duoc.agricol.model.Publicacion
import androidx.room.*

@Dao
interface PublicacionesDao {

    @Query("SELECT * FROM publicacion LIMIT 3")
    fun getAll(): List<Publicacion>

    @Insert
    fun insert(publicacion: Publicacion): Long

    @Update
    fun update(publicacion: Publicacion): Int

    @Delete
    fun delete(publicacion: Publicacion): Int
}
