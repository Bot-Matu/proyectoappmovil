package cl.duoc.agricol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import cl.duoc.agricol.repository.AppDatabase
import cl.duoc.agricol.repository.UsuarioRepository
import cl.duoc.agricol.ui.theme.Navegacion
import cl.duoc.agricol.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {

    // Crea la base de datos de Room
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "agricolab_db"
        ).build()
    }
    private val usuarioRepository by lazy { UsuarioRepository(db.usuariosDao()) }
    private val usuarioViewModel by lazy { UsuarioViewModel(usuarioRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navegacion(usuarioViewModel)
                }
            }
        }
    }
}