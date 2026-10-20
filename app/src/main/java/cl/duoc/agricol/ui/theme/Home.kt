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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    var itemSeleccionado by remember { mutableStateOf(0) }
    val titulos = listOf("Inicio", "Buscar", "Mensajes","Perfil")
    val iconos = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.Message, Icons.Default.Person)

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("Agricolab") },
                actions = {
                    // Botón de "+" (Crear)
                    IconButton(onClick = { /* aqui se manda a crear una nueva notificacion */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear nuevo"
                        )
                    }

                    IconButton(onClick = {/*aqui se agrega lo que vaya en notificaciones o lo que quieres que haga el boton*/  }) {
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
            //aqui va lo del medio
        }
    }
}