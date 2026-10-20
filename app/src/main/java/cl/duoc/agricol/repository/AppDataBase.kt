package cl.duoc.agricol.repository


import androidx.room.Database
import androidx.room.RoomDatabase
import cl.duoc.agricol.model.Publicacion

@Database(entities = [Publicacion::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun publicacionesDao():PublicacionesDao
}