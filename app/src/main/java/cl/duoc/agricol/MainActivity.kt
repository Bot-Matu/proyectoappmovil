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
import cl.duoc.agricol.repository.MensajeRepositoryImpl
import cl.duoc.agricol.repository.PublicacionRepository
import cl.duoc.agricol.repository.UsuarioRepository
import cl.duoc.agricol.ui.theme.Navegacion
import cl.duoc.agricol.viewmodel.BuscarViewModel
import cl.duoc.agricol.viewmodel.ConversacionesViewModel
import cl.duoc.agricol.viewmodel.PublicacionViewModel
import cl.duoc.agricol.viewmodel.UsuarioViewModel
import cl.duoc.agricol.ui.theme.AgricolTheme

class MainActivity : ComponentActivity() {

    // Crea la base de datos de Room
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "agricolab_db"
        )
            .fallbackToDestructiveMigration() // mientras desarrollan: si cambia el esquema, recrea la BD
            .build()
    }
    private val usuarioRepository by lazy { UsuarioRepository(db.usuariosDao()) }
    private val usuarioViewModel by lazy { UsuarioViewModel(usuarioRepository) }

    private val publicacionRepository by lazy { PublicacionRepository(db.publicacionesDao()) }
    private val publicacionViewModel by lazy { PublicacionViewModel(publicacionRepository) }
    private val buscarViewModel by lazy { BuscarViewModel(publicacionRepository) }

    private val mensajeRepository by lazy { MensajeRepositoryImpl(db.chatDao(), db.usuariosDao()) }
    private val conversacionesViewModel by lazy {
        ConversacionesViewModel(mensajeRepository, usuarioViewModel.usuarioActual)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgricolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navegacion(
                        usuarioViewModel,
                        conversacionesViewModel,
                        mensajeRepository,
                        publicacionViewModel,
                        buscarViewModel
                    )
                }
            }
        }
    }
}