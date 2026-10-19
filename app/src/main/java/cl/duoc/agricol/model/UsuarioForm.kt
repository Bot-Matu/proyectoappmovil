package cl.duoc.agricol.model

data class UsuarioForm (
    var nombre: String = "",
    var correo: String = "",
    var celular: Int? = null,
    var contraseña:String="",
    var rep_contraseña:String="",
    var rut:String="",
    var nro_Documento:String="",
    var terminos: Boolean = false
){
    fun validar(): String? {
        if (nombre.isBlank()) return "El nombre es obligatorio"
        if (!correo.contains("@") || !correo.contains(".")) return "Ingresa un correo válido"
        if (contraseña.length < 6) return "La contraseña debe tener al menos 6 caracteres"
        if (contraseña != rep_contraseña) return "Las contraseñas no coinciden"
        if (rut.isBlank()) return "El RUT es obligatorio"
        if (nro_Documento.isBlank()) return "El número de documento es obligatorio"
        if (!terminos) return "Debes aceptar los términos y condiciones"

        return null
    }
}
