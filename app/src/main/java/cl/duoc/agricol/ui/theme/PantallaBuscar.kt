package cl.duoc.agricol.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.agricol.ui.theme.AzulPrimario

@Composable
fun PantallaBuscar() {
    var textoBusqueda by remember { mutableStateOf("") }
    val filtros = remember { mutableStateListOf("Día", "1 Persona", "Riego") }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Barra de búsqueda
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                placeholder = { Text("Buscar en Agricolab", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f).height(52.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* buscar */ },
                modifier = Modifier.height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
            ) {
                Text("Buscar", color = Color.White, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chips de filtros
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.FilterList, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp).align(Alignment.CenterVertically))
            Text("Filtros", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterVertically))
            filtros.forEach { filtro ->
                FilterChip(
                    selected = true,
                    onClick = { filtros.remove(filtro) },
                    label = { Text(filtro, fontSize = 11.sp) },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AzulPrimario.copy(alpha = 0.1f),
                        selectedLabelColor     = AzulPrimario
                    )
                )
            }
        }
    }
}