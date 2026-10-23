package cl.duoc.agricol.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
                     @PrimaryKey
                    val id: Int,
                    val nombre: String,
                    val celular: Int,
                    val correo:String,
                    val contrasena:String,
                    val rut:String,
                    val nro_documento:String

){
    fun info():String{
        return "Usuario(id=$id, nombre='$nombre',correo='$correo', celular=$celular, rut=$rut,)"
    }



}
