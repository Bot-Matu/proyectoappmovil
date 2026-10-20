package cl.duoc.agricol.model
import java.util.UUID
data class Usuario(val id: UUID,
                    val nombre: String,
                    val celular: Int,
                    val correo:String,
                    val contraseña:String,
                    val rut:String,
                    val nro_documento:String

){
    fun info():String{
        return "Usuario(id=$id, nombre='$nombre',correo='$correo', celular=$celular, rut=$rut,)"
    }



}
