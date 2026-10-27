package cl.duoc.agricol.repository

import cl.duoc.agricol.R
import cl.duoc.agricol.model.Publicacion

class PublicacionRepository {

    fun obtenerPublicaciones(): List<Publicacion> {
        return listOf(
            Publicacion(
                id = 1,
                titulo = "Trabajo de Poda",
                comuna = "Los Barrancos",
                ciudad = "Los Andes",
                fecha_inicio = "15 nov",
                fecha_termino = "18 nov",
                dias = 3,
                tra_necesitados = 1,
                descripcion = "El trabajo consistirá en realizar una poda técnica en 25 hectáreas de plantas para mejorar la salud y producción de las plantas.",
                info_extra = "Facilitaremos los implementos necesarios para el correcto término del trabajo.",
                paga = 300000,
                creadorId = 1,
                imagen = R.drawable.poda
            ),
            Publicacion(
                id = 2,
                titulo = "Trabajo de Riego",
                comuna = "El Cañaveral",
                ciudad = "Los Andes",
                fecha_inicio = "18 nov",
                fecha_termino = "19 nov",
                dias = 1,
                tra_necesitados = 1,
                descripcion = "Se necesita una persona para realizar el riego de hortalizas durante un día.",
                info_extra = "Se entrega almuerzo durante la jornada.",
                paga = 200000,
                creadorId = 1,
                imagen = R.drawable.riego
            )
        )
    }
}