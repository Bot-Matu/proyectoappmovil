package cl.duoc.agricol.repository




import androidx.room.Database
import androidx.room.RoomDatabase
import cl.duoc.agricol.model.Conversacion
import cl.duoc.agricol.repository.ChatDao
import cl.duoc.agricol.model.Mensaje
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.model.Usuario

@Database(
    entities = [Usuario::class, Publicacion::class, Mensaje::class, Conversacion::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuariosDao(): UsuarioDao
    abstract fun publicacionesDao(): PublicacionesDao

    abstract fun chatDao(): ChatDao
}