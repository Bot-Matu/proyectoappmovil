package cl.duoc.agricol.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.agricol.repository.MensajeRepository
import cl.duoc.agricol.ui.PantallaChat
import cl.duoc.agricol.viewmodel.BuscarViewModel
import cl.duoc.agricol.viewmodel.ConversacionesViewModel
import cl.duoc.agricol.viewmodel.PublicacionViewModel
import cl.duoc.agricol.viewmodel.MensajeViewModel
import cl.duoc.agricol.viewmodel.MensajeViewModelFactory
import cl.duoc.agricol.viewmodel.UsuarioViewModel

@Composable
fun Navegacion(
    usuarioViewModel: UsuarioViewModel,
    conversacionesViewModel: ConversacionesViewModel,
    mensajeRepository: MensajeRepository,
    publicacionViewModel: PublicacionViewModel,
    buscarViewModel: BuscarViewModel
) {
    val navController = rememberNavController()
    val sesionCargada by usuarioViewModel.sesionCargada.collectAsState()

    // No se dibuja nada hasta saber si hay una sesión guardada
    if (sesionCargada) {

        // Se decide una sola vez: Home si hay sesión, Login si no
        val destinoInicial = remember {
            if (usuarioViewModel.usuarioActual.value != null) "home" else "login"
        }

        NavHost(navController, startDestination = destinoInicial) {
            composable("register") { Register(navController, usuarioViewModel) }
            composable("login") { Login(navController, usuarioViewModel) }
            composable("home") { Home(navController, usuarioViewModel, conversacionesViewModel, publicacionViewModel, buscarViewModel) }
            composable("register2") { RegisterPart2(navController, usuarioViewModel) }

            // Un chat: "chat/<id de la conversación>/<id de la otra persona>"
            composable(
                route = "chat/{conversacionId}/{otroId}",
                arguments = listOf(
                    navArgument("conversacionId") { type = NavType.IntType },
                    navArgument("otroId") { type = NavType.IntType }
                )
            ) { entry ->
                val conversacionId = entry.arguments?.getInt("conversacionId") ?: return@composable
                val otroId = entry.arguments?.getInt("otroId") ?: return@composable
                val miId = usuarioViewModel.usuarioActual.value?.id ?: return@composable

                val chatViewModel: MensajeViewModel = viewModel(
                    key = "chat_$conversacionId",
                    factory = MensajeViewModelFactory(mensajeRepository, conversacionId, miId, otroId)
                )
                PantallaChat(navController, chatViewModel)
            }
        }
    }
}