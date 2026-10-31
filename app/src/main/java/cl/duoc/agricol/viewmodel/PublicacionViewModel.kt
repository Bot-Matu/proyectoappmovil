package cl.duoc.agricol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.agricol.R
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.model.PublicacionForm
import cl.duoc.agricol.model.PublicacionFormError
import cl.duoc.agricol.model.Usuario
import cl.duoc.agricol.repository.PublicacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PublicacionViewModel(private val repository: PublicacionRepository) : ViewModel() {

    init {
        // Si la base está vacía, guarda las publicaciones de ejemplo
        viewModelScope.launch { repository.cargarEjemplos() }
    }

    // ---------- Lista de Inicio ----------

    // La categoría elegida en los filtros ("Todos" muestra todo)
    private val _categoria = MutableStateFlow("Todos")
    val categoria: StateFlow<String> = _categoria

    // Lo que se muestra en la lista: las de Room, ya filtradas por categoría
    val publicaciones: StateFlow<List<Publicacion>> =
        combine(repository.publicaciones, _categoria) { lista, categoria ->
            if (categoria == "Todos") lista else lista.filter { it.categoria == categoria }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onCategoriaChange(nuevaCategoria: String) {
        _categoria.value = nuevaCategoria
    }

    // ---------- Crear publicación ----------

    // Lo que el usuario ha escrito hasta ahora
    private val _form = MutableStateFlow(PublicacionForm())
    val form: StateFlow<PublicacionForm> = _form

    // Los errores de cada campo
    private val _errors = MutableStateFlow(PublicacionFormError())
    val errors: StateFlow<PublicacionFormError> = _errors

    // Es true cuando la publicación ya se guardó (la pantalla muestra el modal)
    private val _publicado = MutableStateFlow(false)
    val publicado: StateFlow<Boolean> = _publicado

    // Cada vez que se edita un campo, se guarda el valor y se borra su error
    fun onTituloChange(valor: String) {
        _form.value = _form.value.copy(titulo = valor)
        _errors.value = _errors.value.copy(titulo = null)
    }

    fun onCategoriaFormChange(valor: String) {
        _form.value = _form.value.copy(categoria = valor)
        _errors.value = _errors.value.copy(categoria = null)
    }

    fun onComunaChange(valor: String) {
        _form.value = _form.value.copy(comuna = valor)
        _errors.value = _errors.value.copy(comuna = null)
    }

    fun onCiudadChange(valor: String) {
        _form.value = _form.value.copy(ciudad = valor)
        _errors.value = _errors.value.copy(ciudad = null)
    }

    fun onFechaInicioChange(valor: String) {
        if (valor.length <= 10) {
            _form.value = _form.value.copy(fecha_inicio = valor)
            _errors.value = _errors.value.copy(fecha_inicio = null)
        }
    }

    fun onFechaTerminoChange(valor: String) {
        if (valor.length <= 10) {
            _form.value = _form.value.copy(fecha_termino = valor)
            _errors.value = _errors.value.copy(fecha_termino = null)
        }
    }

    fun onTrabajadoresChange(valor: String) {
        // Solo se aceptan números
        if (valor.all { it.isDigit() } && valor.length <= 3) {
            _form.value = _form.value.copy(tra_necesitados = valor)
            _errors.value = _errors.value.copy(tra_necesitados = null)
        }
    }

    fun onDescripcionChange(valor: String) {
        _form.value = _form.value.copy(descripcion = valor)
        _errors.value = _errors.value.copy(descripcion = null)
    }

    fun onRequisitosChange(valor: String) {
        _form.value = _form.value.copy(requisitos = valor)
        _errors.value = _errors.value.copy(requisitos = null)
    }

    fun onInfoExtraChange(valor: String) {
        _form.value = _form.value.copy(info_extra = valor)
    }

    fun onPagaChange(valor: String) {
        // Solo se aceptan números
        if (valor.all { it.isDigit() } && valor.length <= 8) {
            _form.value = _form.value.copy(paga = valor)
            _errors.value = _errors.value.copy(paga = null)
        }
    }

    // Convierte "15/11/2026" en una fecha; devuelve null si no es una fecha real
    private fun leerFecha(texto: String): Date? {
        if (!texto.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$"))) return null
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        formato.isLenient = false   // rechaza fechas como 31/02/2026
        return try {
            formato.parse(texto)
        } catch (e: Exception) {
            null
        }
    }

    // Valida todo el formulario. Devuelve true si no hay errores
    private fun validar(): Boolean {
        val f = _form.value
        val inicio = leerFecha(f.fecha_inicio)
        val termino = leerFecha(f.fecha_termino)

        val errores = PublicacionFormError(
            titulo = if (f.titulo.isBlank()) "El título es obligatorio" else null,
            categoria = if (f.categoria.isBlank()) "Elige una categoría" else null,
            comuna = if (f.comuna.isBlank()) "La comuna es obligatoria" else null,
            ciudad = if (f.ciudad.isBlank()) "La región es obligatoria" else null,
            fecha_inicio = if (inicio == null) "Usa el formato dd/mm/aaaa" else null,
            fecha_termino = when {
                termino == null -> "Usa el formato dd/mm/aaaa"
                inicio != null && !termino.after(inicio) -> "Debe ser posterior al inicio"
                else -> null
            },
            tra_necesitados = if ((f.tra_necesitados.toIntOrNull() ?: 0) < 1) "Ingresa al menos 1 trabajador" else null,
            descripcion = if (f.descripcion.isBlank()) "La descripción es obligatoria" else null,
            requisitos = if (f.requisitos.isBlank()) "Los requisitos son obligatorios" else null,
            paga = if ((f.paga.toIntOrNull() ?: 0) < 1) "Ingresa un monto mayor a 0" else null
        )
        _errors.value = errores

        return errores == PublicacionFormError()   // true si todos los errores son null
    }

    // Cada categoría usa su foto de res/drawable
    private fun imagenPorCategoria(categoria: String): Int {
        return when (categoria) {
            "Poda" -> R.drawable.poda
            "Riego" -> R.drawable.riego
            else -> R.drawable.cosecha
        }
    }

    // Valida y, si está bien, guarda la publicación en Room
    fun publicar(usuario: Usuario) {
        if (!validar()) return

        val f = _form.value
        val inicio = leerFecha(f.fecha_inicio) ?: return
        val termino = leerFecha(f.fecha_termino) ?: return
        val dias = Math.round((termino.time - inicio.time) / 86_400_000.0).toInt()

        val publicacion = Publicacion(
            titulo = f.titulo.trim(),
            categoria = f.categoria,
            comuna = f.comuna.trim(),
            ciudad = f.ciudad.trim(),
            fecha_inicio = f.fecha_inicio,
            fecha_termino = f.fecha_termino,
            dias = dias,
            tra_necesitados = f.tra_necesitados.toInt(),
            descripcion = f.descripcion.trim(),
            requisitos = f.requisitos.trim(),
            info_extra = f.info_extra.trim(),
            contratante = usuario.nombre,      // el contratante es quien está logueado
            paga = f.paga.toInt(),
            creadorId = usuario.id,
            imagen = imagenPorCategoria(f.categoria)
        )

        viewModelScope.launch {
            repository.crear(publicacion)
            _publicado.value = true
        }
    }

    // Cierra el modal y deja el formulario limpio
    fun cerrarModal() {
        _publicado.value = false
        _form.value = PublicacionForm()
        _errors.value = PublicacionFormError()
    }
}
