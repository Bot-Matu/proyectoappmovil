package cl.duoc.agricol.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import cl.duoc.agricol.ui.PantallaBuscar
import cl.duoc.agricol.ui.PantallaInicio
import cl.duoc.agricol.ui.PantallaMensajes
import cl.duoc.agricol.ui.PantallaPerfil
import cl.duoc.agricol.viewmodel.UsuarioViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController, usuarioViewModel: UsuarioViewModel) {
    var itemSeleccionado by remember { mutableIntStateOf(0) }

    val titulos = listOf("Inicio", "Buscar", "Mensajes", "Perfil")
    val iconos  = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.AutoMirrored.Filled.Message,
        Icons.Default.Person
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /* abrir drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                title = { Text("Agricolab", style = MaterialTheme.typography.titleMedium) },
                actions = {
                    IconButton(onClick = { /* nueva publicación */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo")
                    }
                    IconButton(onClick = { itemSeleccionado = 3 }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                titulos.forEachIndexed { index, titulo ->
                    NavigationBarItem(
                        icon    = { Icon(iconos[index], contentDescription = titulo) },
                        label   = { Text(titulo) },
                        selected = itemSeleccionado == index,
                        onClick  = { itemSeleccionado = index },
                        colors  = NavigationBarItemDefaults.colors(
                            selectedIconColor       = AzulPrimario,
                            selectedTextColor       = AzulPrimario,
                            indicatorColor          = AzulPrimario.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (itemSeleccionado) {
                0 -> PantallaInicio()
                1 -> PantallaBuscar()
                2 -> PantallaMensajes()
                3 -> PantallaPerfil(
                    viewModel      = usuarioViewModel,
                    onCerrarSesion = {
                        usuarioViewModel.cerrarSesion()
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}