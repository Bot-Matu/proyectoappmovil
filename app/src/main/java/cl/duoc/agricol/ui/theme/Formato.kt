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