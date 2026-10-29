package cl.duoc.agricol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Work
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.TextoSecundario
import cl.duoc.agricol.ui.theme.VerdeEstado
import cl.duoc.agricol.viewmodel.UsuarioViewModel

@Composable
fun PantallaPerfil(viewModel: UsuarioViewModel, onCerrarSesion: () -> Unit = {}) {
    val usuario by viewModel.usuarioActual.collectAsState()
    val inicial = usuario?.nombre?.take(1)?.uppercase() ?: "U"

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // Encabezado: avatar + datos
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar circular
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(AzulPrimario),
                contentAlignment = Alignment.Center
            ) {
                Text(inicial, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = usuario?.nombre ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                // Rol / descripción (puedes adaptar a tu modelo)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Work,

                        contentDescription = null,
                        tint = TextoSecundario,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Trabajos Finalizados: —", fontSize = 12.sp, color = TextoSecundario)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(VerdeEstado)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Conectado/a", fontSize = 12.sp, color = VerdeEstado)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón cerrar sesión (ícono discreto)
            IconButton(onClick = onCerrarSesion) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = TextoSecundario)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Editar Perfil (outlined, centrado)
        OutlinedButton(
            onClick = { /* editar perfil */ },
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF374151))
        ) {
            Text("Editar Perfil", fontWeight = FontWeight.Medium)
        }
    }
}