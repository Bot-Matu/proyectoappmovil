package cl.duoc.agricol.ui.theme


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// En Material 3, TopAppBar a veces requiere esta anotación experimental dependiendo de tu versión
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    var itemSeleccionado by remember { mutableStateOf(0) }
    val titulos = listOf("Inicio", "Buscar", "Mensajes","Perfil")
    val iconos = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.Message, Icons.Default.Person)

    Scaffold(
        // 1. BARRA SUPERIOR
        topBar = {
            TopAppBar(
                title = { Text("Agricolab") }, // Nombre de la app a la izquierda
                actions = {
                    // Botón de "+" (Crear)
                    IconButton(onClick = { /* Acción para crear algo nuevo */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear nuevo"
                        )
                    }
                    // Botón de Notificaciones
                    IconButton(onClick = { /* Acción de notificaciones */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Ver notificaciones"
                        )
                    }
                }
            )
        },
        // 2. BARRA INFERIOR (Menú de navegación)
        bottomBar = {
            NavigationBar {
                titulos.forEachIndexed { index, titulo ->
                    NavigationBarItem(
                        icon = { Icon(iconos[index], contentDescription = titulo) },
                        label = { Text(titulo) },
                        selected = itemSeleccionado == index,
                        onClick = { itemSeleccionado = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        // 3. CONTENIDO CENTRAL
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Vital para que no se tape con las barras
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Estás en la pestaña: ${titulos[itemSeleccionado]}")
        }
    }
}