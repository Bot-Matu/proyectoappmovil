package cl.duoc.agricol.repository

import cl.duoc.agricol.R
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.model.PublicacionRemota
import kotlinx.coroutines.flow.Flow

// Si recibe el repositorio remoto, las publicaciones viven en Xano y Room es la copia local
// que alimenta las pantallas. Sin remoto funciona igual que antes (solo Room + ejemplos).
class PublicacionRepository(
    private val dao: PublicacionesDao,
    private val remoto: PublicacionesRemotoRepository? = null
) {

    // Todas las publicaciones guardadas en Room (se actualiza sola)
    val publicaciones: Flow<List<Publicacion>> = dao.observarTodas()

    // Con servidor: se guarda en Xano y en Room queda con el id que asignó Xano
    suspend fun crear(publicacion: Publicacion) {
        if (remoto == null) {
            dao.insert(publicacion)
        } else {
            val creada = remoto.crearPublicacion(publicacion.aRemota())
            dao.upsertVarias(listOf(publicacion.copy(id = creada.id)))
        }
    }

    // Baja las publicaciones de Xano y deja Room igual al servidor.
    // Sin servidor, solo carga los ejemplos la primera vez.
    suspend fun sincronizar() {
        if (remoto == null) {
            cargarEjemplos()
            return
        }
        val lista = remoto.obtenerPublicaciones().map { it.aLocal() }
        dao.upsertVarias(lista)
        dao.borrarExcepto(lista.map { it.id })
    }

    // La imagen no viaja por la API: se elige según la categoría
    private fun imagenPorCategoria(categoria: String): Int = when (categoria) {
        "Poda" -> R.drawable.poda
        "Riego" -> R.drawable.riego
        else -> R.drawable.cosecha
    }

    private fun PublicacionRemota.aLocal() = Publicacion(
        id = id, titulo = titulo, categoria = categoria, comuna = comuna, ciudad = ciudad,
        fecha_inicio = fecha_inicio, fecha_termino = fecha_termino, dias = dias,
        tra_necesitados = tra_necesitados, descripcion = descripcion, requisitos = requisitos,
        info_extra = info_extra, contratante = contratante, paga = paga, creadorId = creadorId,
        imagen = imagenPorCategoria(categoria)
    )

    private fun Publicacion.aRemota() = PublicacionRemota(
        titulo = titulo, categoria = categoria, comuna = comuna, ciudad = ciudad,
        fecha_inicio = fecha_inicio, fecha_termino = fecha_termino, dias = dias,
        tra_necesitados = tra_necesitados, descripcion = descripcion, requisitos = requisitos,
        info_extra = info_extra, contratante = contratante, paga = paga, creadorId = creadorId
    )

    // La primera vez que se abre la app, guarda las 3 publicaciones de ejemplo
    suspend fun cargarEjemplos() {
        if (dao.contar() == 0) {
            dao.insertarVarias(ejemplos())
        }
    }

    private fun ejemplos(): List<Publicacion> {
        return listOf(
            Publicacion(
                id = 1,
                titulo = "Trabajo de Poda",
                categoria = "Poda",
                comuna = "Curicó",
                ciudad = "Región del Maule",
                fecha_inicio = "15 nov",
                fecha_termino = "29 nov",
                dias = 14,
                tra_necesitados = 1,
                descripcion = "Se busca cuadrilla o podador experimentado para 2.5 hectáreas de manzanas. Trabajo por temporada. Herramientas básicas suministradas en el huerto. Se requiere experiencia previa comprobable en frutales de pepita.",
                requisitos = "Experiencia de 1 año, equipo de protección personal básico.",
                info_extra = "Se entrega almuerzo durante la jornada.",
                contratante = "Agrícola San Pedro Ltda.",
                paga = 300000,
                creadorId = 1,
                imagen = R.drawable.poda
            ),
            Publicacion(
                id = 2,
                titulo = "Trabajo de Riego",
                categoria = "Riego",
                comuna = "Talca",
                ciudad = "Región del Maule",
                fecha_inicio = "18 nov",
                fecha_termino = "25 nov",
                dias = 7,
                tra_necesitados = 1,
                descripcion = "Se necesita personal para el riego de hortalizas y la revisión diaria del sistema de goteo. Jornada diurna.",
                requisitos = "Experiencia básica en riego por goteo.",
                info_extra = "Se entrega colación durante la jornada.",
                contratante = "Hortalizas del Valle SpA",
                paga = 200000,
                creadorId = 1,
                imagen = R.drawable.riego
            ),
            Publicacion(
                id = 3,
                titulo = "Cosecha de Arándanos",
                categoria = "Cosecha",
                comuna = "Linares",
                ciudad = "Región del Maule",
                fecha_inicio = "1 dic",
                fecha_termino = "22 dic",
                dias = 21,
                tra_necesitados = 5,
                descripcion = "Cosecha manual con selección de calidad para exportación. Pago semanal por rendimiento.",
                requisitos = "Disponibilidad para trabajar en jornada completa.",
                info_extra = "Se entrega transporte desde Linares.",
                contratante = "Berries del Sur Ltda.",
                paga = 450000,
                creadorId = 1,
                imagen = R.drawable.cosecha
            )
        )
    }
}
