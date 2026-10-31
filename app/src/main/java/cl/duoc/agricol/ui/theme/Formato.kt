package cl.duoc.agricol.ui

// Convierte 300000 en "$300.000"
fun formatearPrecio(valor: Int): String {
    return "$" + "%,d".format(valor).replace(",", ".")
}

// Convierte 14 en "2 Semanas" y 10 en "10 Días"
fun formatearDuracion(dias: Int): String {
    val semanas = dias / 7
    return when {
        dias % 7 != 0 -> "$dias Días"
        semanas == 1 -> "1 Semana"
        else -> "$semanas Semanas"
    }
}

// Convierte "198765432" en "19.876.543-2"
fun formatearRut(rut: String): String {
    if (!rut.contains("-")) {
        return rut
    }
    val cuerpo = rut.substringBefore("-")
    val digito = rut.substringAfter("-")
    val conPuntos = cuerpo.reversed().chunked(3).joinToString(".").reversed()
    return "$conPuntos-$digito"
}

// Convierte "Productor" en "Productor / Empleador"
fun formatearRol(rol: String): String {
    return if (rol == "Productor") "Productor / Empleador" else "Trabajador Agrícola"
}

// Convierte 87654321 en "+56 9 8765 4321"
fun formatearCelular(celular: Int): String {
    val texto = celular.toString().padStart(8, '0')
    return "+56 9 ${texto.take(4)} ${texto.drop(4)}"
}