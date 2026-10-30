package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Notificacion

class NotificacionRepository {

    fun obtenerNotificaciones(): List<Notificacion> {
        return listOf(
            Notificacion(
                id = 1,
                titulo = "Nuevo mensaje",
                detalle = "Samantha Philgrim te escribió sobre el trabajo de poda.",
                hora = "10:42"
            ),
            Notificacion(
                id = 2,
                titulo = "Postulación vista",
                detalle = "Agrícola San Pedro revisó tu postulación.",
                hora = "Hoy"
            ),
            Notificacion(
                id = 3,
                titulo = "Nuevo trabajo en tu zona",
                detalle = "Cosecha de Arándanos en Linares.",
                hora = "Ayer"
            )
        )
    }
}