package cl.duoc.agricol.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.agricol.viewmodel.UsuarioViewModel

@Composable
fun Navegacion(usuarioViewModel: UsuarioViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("register") { Register(navController, usuarioViewModel) }
        composable("login") { Login(navController, usuarioViewModel) }
        composable("home") { Home(navController) }
        composable("register2") { RegisterPart2(navController, usuarioViewModel) }
    }
}