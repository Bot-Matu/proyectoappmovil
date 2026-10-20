package cl.duoc.agricol.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onCerrarSesion: () -> Unit = {}
) {
    var itemSeleccionado by remember { mutableIntStateOf(0) }
    val titulos = listOf("Inicio", "Buscar", "Mensajes", "Perfil")
    val iconos = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.AutoMirrored.Filled.Message,
        Icons.Default.Person
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agricolab") },
                actions = {
                    // Botón de "+" (crear una nueva publicación)
                    IconButton(onClick = { /* aquí se abre la pantalla de crear publicación */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear nuevo"
                        )
                    }

                    // Botón de notificaciones
                    IconButton(onClick = { /* aquí va lo que haga el botón de notificaciones */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Ver notificaciones"
                        )
                    }
                }
            )
        },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            when (itemSeleccionado) {
                0 -> PestanaInicio()
                1 -> PestanaBuscar()
                2 -> PestanaMensajes()
                3 -> PestanaPerfil(onCerrarSesion = onCerrarSesion)
            }
        }
    }
}

@Composable
private fun PestanaInicio() {

    Text("Publicaciones")
}

@Composable
private fun PestanaBuscar() {

    Text("Buscar")
}

@Composable
private fun PestanaMensajes() {

    Text("Mensajes")
}

@Composable
private fun PestanaPerfil(onCerrarSesion: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil")
        Button(onClick = onCerrarSesion) {
            Text("Cerrar sesión")
        }
    }
}
