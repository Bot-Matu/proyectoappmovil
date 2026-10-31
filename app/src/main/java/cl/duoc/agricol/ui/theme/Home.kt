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
import cl.duoc.agricol.viewmodel.ConversacionesViewModel
import androidx.compose.runtime.mutableStateOf
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.ui.PantallaDetalle
import cl.duoc.agricol.ui.PantallaCrearPublicacion
import cl.duoc.agricol.viewmodel.BuscarViewModel
import cl.duoc.agricol.viewmodel.PublicacionViewModel
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.agricol.ui.PanelNotificaciones
import cl.duoc.agricol.viewmodel.NotificacionViewModel
import androidx.compose.runtime.saveable.rememberSaveable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel,
    conversacionesViewModel: ConversacionesViewModel,
    publicacionViewModel: PublicacionViewModel,
    buscarViewModel: BuscarViewModel,
    notificacionViewModel: NotificacionViewModel = viewModel()
) {
    var itemSeleccionado by rememberSaveable { mutableIntStateOf(0) }
    var publicacionSeleccionada by remember { mutableStateOf<Publicacion?>(null) }
    var mostrarCrear by remember { mutableStateOf(false) }
    var mostrarNotificaciones by remember { mutableStateOf(false) }
    var avisoChat by remember { mutableStateOf<String?>(null) }
    val hayNoLeidas by notificacionViewModel.hayNoLeidas.collectAsState()
    val usuarioActual by usuarioViewModel.usuarioActual.collectAsState()


    val titulos = listOf("Inicio", "Buscar", "Mensajes", "Perfil")
    val iconos  = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.AutoMirrored.Filled.Message,
        Icons.Default.Person,
        Icons.Default.Notifications

    )

    Scaffold(
        topBar = {
            TopAppBar(

                title = { Text("Agricolab", style = MaterialTheme.typography.titleMedium) },
                actions = {
                    IconButton(onClick = {
                        mostrarCrear = true
                        publicacionSeleccionada = null
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo")
                    }
                    // Botón de notificaciones, con el panel pegado debajo
                    Box {
                        IconButton(onClick = {
                            mostrarNotificaciones = true
                            notificacionViewModel.marcarComoLeidas()
                        }) {
                            BadgedBox(badge = { if (hayNoLeidas) { Badge() } }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Ver notificaciones"
                                )
                            }
                        }
                        PanelNotificaciones(
                            expandido = mostrarNotificaciones,
                            notificaciones = notificacionViewModel.notificaciones,
                            onCerrar = { mostrarNotificaciones = false }
                        )
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
                        onClick = {
                            itemSeleccionado = index
                            publicacionSeleccionada = null
                            mostrarCrear = false
                        },
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
            val publicacion = publicacionSeleccionada

            if (mostrarCrear) {
                PantallaCrearPublicacion(
                    viewModel = publicacionViewModel,
                    usuario = usuarioActual,
                    onVolver = { mostrarCrear = false }
                )
            } else if (publicacion != null) {
                PantallaDetalle(
                    publicacion = publicacion,
                    onVolver = { publicacionSeleccionada = null },
                    onChat = {
                        val miId = usuarioActual?.id
                        if (miId == publicacion.creadorId) {
                            avisoChat = "Esta publicación es tuya, no puedes chatear contigo mismo."
                        } else {
                            conversacionesViewModel.abrirChat(publicacion.creadorId) { conversacionId ->
                                navController.navigate("chat/$conversacionId/${publicacion.creadorId}")
                            }
                        }
                    }
                )
            } else {
                when (itemSeleccionado) {
                    0 -> PantallaInicio(
                        onVerDetalle = { publicacionSeleccionada = it },
                        viewModel = publicacionViewModel
                    )
                    1 -> PantallaBuscar(
                        onVerDetalle = { publicacionSeleccionada = it },
                        viewModel = buscarViewModel
                    )
                    2 -> PantallaMensajes(navController, conversacionesViewModel)
                    3 -> PantallaPerfil(
                        viewModel = usuarioViewModel,
                        onCerrarSesion = {
                            usuarioViewModel.cerrarSesion()
                            navController.navigate("login")
                        }
                    )
                }
            }
        }
    }
    avisoChat?.let { mensaje ->
        AlertDialog(
            onDismissRequest = { avisoChat = null },
            title = { Text("Chat") },
            text = { Text(mensaje) },
            confirmButton = {
                TextButton(onClick = { avisoChat = null }) { Text("OK") }
            }
        )
    }
}

