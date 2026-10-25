package cl.duoc.agricol.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val celular: Int,
    val correo: String,
    @ColumnInfo(name = "password") val contraseña: String,
    val rut: String,
    val nro_documento: String
) {
    fun info(): String {
        return "Usuario(id=$id, nombre='$nombre', correo='$correo', celular=$celular, rut=$rut)"
    }
}
