package cl.duoc.agricol.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Cada Usuario es una fila de la tabla "usuario" en Room
@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val celular: Int,
    val correo: String,
    val password: String,
    val rut: String,
    val nro_documento: String
)