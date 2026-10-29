package cl.duoc.agricol.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.agricol.viewmodel.UsuarioViewModel

@Composable
fun Navegacion(usuarioViewModel: UsuarioViewModel) {
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
            composable("home") { Home(navController, usuarioViewModel) }
            composable("register2") { RegisterPart2(navController, usuarioViewModel) }
        }
    }
}