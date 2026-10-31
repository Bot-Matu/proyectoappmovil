package cl.duoc.agricol.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Cada Usuario es una fila de la tabla "usuarios" en Room
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val celular: Int,
    val correo: String,
    val contrasena: String,
    val rut: String,
    val nro_documento: String,
    val rol: String = "Trabajador",   // "Trabajador" o "Productor"
    val sesionActiva : Boolean = false
)
