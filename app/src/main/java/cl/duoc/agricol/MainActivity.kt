package cl.duoc.agricol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.agricol.ui.theme.AgricolTheme
import cl.duoc.agricol.ui.theme.Login
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.input.key.Key.Companion.Home
import cl.duoc.agricol.ui.theme.Inicio
import cl.duoc.agricol.ui.theme.Register
import cl.duoc.agricol.ui.theme.RegisterPart2
import cl.duoc.agricol.ui.theme.Home
import cl.duoc.agricol.ui.theme.Navegacion

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    //Home()
                    Navegacion()

                }
            }
        }
    }
}
