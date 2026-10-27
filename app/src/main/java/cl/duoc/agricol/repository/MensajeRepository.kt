package cl.duoc.agricol.repository

import cl.duoc.agricol.model.Mensaje

class MensajeRepository {

    fun obtenerMensajes(): List<Mensaje> {
        return listOf(
            Mensaje(id = 1, nombre = "Mario Soto", texto = "Hola, ¿sigue disponible el trabajo de poda?"),
            Mensaje(id = 2, nombre = "María Pérez", texto = "Gracias por aplicar, te escribo mañana."),
            Mensaje(id = 3, nombre = "Diego Raúl", texto = "Perfecto, nos vemos el 15 de noviembre.")
        )
    }
}