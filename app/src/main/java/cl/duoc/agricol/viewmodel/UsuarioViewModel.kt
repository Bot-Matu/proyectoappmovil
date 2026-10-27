package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.agricol.model.Usuario
import cl.duoc.agricol.model.UsuarioForm
import cl.duoc.agricol.model.UsuarioFormError
import cl.duoc.agricol.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // ---------- Registro ----------

    // Lo que el usuario ha escrito hasta ahora
    private val _form = MutableStateFlow(UsuarioForm())
    val form: StateFlow<UsuarioForm> = _form

    // Los errores de cada campo
    private val _errors = MutableStateFlow(UsuarioFormError())
    val errors: StateFlow<UsuarioFormError> = _errors

    // Texto del modal de registro; si es null, el modal no se muestra
    private val _mensajeModal = MutableStateFlow<String?>(null)
    val mensajeModal: StateFlow<String?> = _mensajeModal

    // Sirve para saber si hay que volver al Login al cerrar el modal
    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso

    // Cada vez que se edita un campo, se guarda el valor y se borra su error
    fun onNombreChange(valor: String) {
        _form.value = _form.value.copy(nombre = valor)
        _errors.value = _errors.value.copy(nombre = null)
    }

    fun onCorreoChange(valor: String) {
        _form.value = _form.value.copy(correo = valor)
        _errors.value = _errors.value.copy(correo = null)
    }

    fun onCelularChange(valor: String) {
        // Solo se aceptan números
        if (valor.all { it.isDigit() }) {
            _form.value = _form.value.copy(celular = valor)
            _errors.value = _errors.value.copy(celular = null)
        }
    }

    fun onContrasenaChange(valor: String) {
        _form.value = _form.value.copy(contrasena = valor)
        _errors.value = _errors.value.copy(contrasena = null)
    }

    fun onRepContrasenaChange(valor: String) {
        _form.value = _form.value.copy(rep_contrasena = valor)
        _errors.value = _errors.value.copy(rep_contrasena = null)
    }

    fun onTerminosChange(valor: Boolean) {
        _form.value = _form.value.copy(terminos = valor)
        _errors.value = _errors.value.copy(terminos = null)
    }

    fun onRutChange(valor: String) {
        _form.value = _form.value.copy(rut = valor)
        _errors.value = _errors.value.copy(rut = null)
    }

    fun onNroDocumentoChange(valor: String) {
        _form.value = _form.value.copy(nro_documento = valor)
        _errors.value = _errors.value.copy(nro_documento = null)
    }

    // Valida la primera pantalla de registro. Devuelve true si no hay errores
    fun validarRegistro(): Boolean {
        val f = _form.value
        val errores = _errors.value.copy(
            nombre = if (f.nombre.isBlank()) "El nombre es obligatorio" else null,
            correo = if (!f.correo.matches(Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$"))) "Correo inválido" else null,
            celular = if (f.celular.length != 8) "Ingresa los 8 dígitos que siguen al +56 9" else null,
            contrasena = if (f.contrasena.length < 6) "Debe tener al menos 6 caracteres" else null,
            rep_contrasena = if (f.rep_contrasena != f.contrasena) "Las contraseñas no coinciden" else null,
            terminos = if (!f.terminos) "Debes aceptar los términos y condiciones" else null
        )
        _errors.value = errores

        return errores.nombre == null &&
                errores.correo == null &&
                errores.celular == null &&
                errores.contrasena == null &&
                errores.rep_contrasena == null &&
                errores.terminos == null
    }

    // Valida la segunda pantalla y, si está bien, guarda el usuario en Room
    fun registrar() {
        val f = _form.value
        val rutLimpio = limpiarRut(f.rut)
        val errores = _errors.value.copy(
            rut = if (!rutLimpio.contains("-")) "Ingresa el RUT con guion, ej: 12345678-9" else null,
            nro_documento = if (f.nro_documento.isBlank()) "El número de documento es obligatorio" else null
        )
        _errors.value = errores

        if (errores.rut != null || errores.nro_documento != null) {
            return
        }

        viewModelScope.launch {
            if (repository.existeRut(rutLimpio)) {
                _mensajeModal.value = "Ya existe un usuario registrado con ese RUT"
            } else {
                val usuario = Usuario(
                    nombre = f.nombre,
                    celular = f.celular.toInt(),
                    correo = f.correo,
                    contrasena = f.contrasena,
                    rut = rutLimpio,
                    nro_documento = f.nro_documento
                )
                repository.registrar(usuario)
                _registroExitoso.value = true
                _mensajeModal.value = "Tu cuenta fue creada correctamente"
            }
        }
    }

    // Cierra el modal; si el registro fue exitoso, deja el formulario limpio
    fun cerrarModal() {
        _mensajeModal.value = null
        if (_registroExitoso.value) {
            _registroExitoso.value = false
            _form.value = UsuarioForm()
            _errors.value = UsuarioFormError()
        }
    }

    // ---------- Login ----------

    // Mensaje de error del login; si es null, no se muestra el modal
    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin

    // Busca al usuario en Room; si existe, ejecuta onExito (que navega a Home)
    fun iniciarSesion(rut: String, contrasena: String, onExito: () -> Unit) {
        if (rut.isBlank() || contrasena.isBlank()) {
            _errorLogin.value = "Ingresa tu RUT y tu contraseña"
            return
        }

        viewModelScope.launch {
            val usuario = repository.iniciarSesion(limpiarRut(rut), contrasena)
            if (usuario != null) {
                onExito()
            } else {
                _errorLogin.value = "RUT o contraseña incorrectos"
            }
        }
    }

    fun cerrarErrorLogin() {
        _errorLogin.value = null
    }

    // Quita puntos y espacios para que "12.345.678-9" y "12345678-9" sean el mismo RUT
    private fun limpiarRut(rut: String): String {
        return rut.replace(".", "").replace(" ", "").uppercase()
    }
}