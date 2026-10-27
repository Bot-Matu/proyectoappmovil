package cl.duoc.agricol.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.agricol.R
import cl.duoc.agricol.viewmodel.PerfilViewModel

@Composable
fun PantallaPerfil(
    onCerrarSesion: () -> Unit = {},
    viewModel: PerfilViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = viewModel.nombre,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text("Trabajos Finalizados: ${viewModel.trabajosFinalizados}")
                Text("Conectado/a")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* aquí irá editar perfil */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Editar Perfil")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}