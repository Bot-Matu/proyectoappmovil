package cl.duoc.agricol.repository




import androidx.room.Database
import androidx.room.RoomDatabase
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.model.Usuario

@Database(
    entities = [Usuario::class, Publicacion::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuariosDao(): UsuariosDao
    abstract fun publicacionesDao(): PublicacionesDao
}