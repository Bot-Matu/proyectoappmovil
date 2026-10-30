package cl.duoc.agricol.repository

import cl.duoc.agricol.R
import cl.duoc.agricol.model.Publicacion

class PublicacionRepository {

    fun obtenerPublicaciones(): List<Publicacion> {
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