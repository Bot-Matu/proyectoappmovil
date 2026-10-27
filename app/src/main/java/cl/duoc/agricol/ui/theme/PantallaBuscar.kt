package cl.duoc.agricol.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaBuscar() {

    var textoBusqueda by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar en Agricolab") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* aquí irá la búsqueda */ }) {
                Text("Buscar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Filtros",
            style = MaterialTheme.typography.titleMedium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Día X")
            Text("1 Persona X")
            Text("Riego X")
        }
    }
}