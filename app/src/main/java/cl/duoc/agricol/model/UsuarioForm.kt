package cl.duoc.agricol.model

// Lo que el usuario escribe en las pantallas de registro
data class UsuarioForm(
    val nombre: String = "",
    val correo: String = "",
    val celular: String = "",
    val contrasena: String = "",
    val rep_contrasena: String = "",
    val rut: String = "",
    val nro_documento: String = "",
    val rol: String = "Trabajador",   // viene preseleccionado, como en la maqueta
    val terminos: Boolean = false
)

// Un mensaje de error por cada campo
data class UsuarioFormError(
    val nombre: String? = null,
    val correo: String? = null,
    val celular: String? = null,
    val contrasena: String? = null,
    val rep_contrasena: String? = null,
    val terminos: String? = null,
    val rut: String? = null,
    val nro_documento: String? = null
)