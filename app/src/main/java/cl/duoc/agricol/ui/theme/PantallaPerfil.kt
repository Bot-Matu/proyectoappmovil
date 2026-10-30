package cl.duoc.agricol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.AzulSuave
import cl.duoc.agricol.ui.theme.GrisFondo
import cl.duoc.agricol.ui.theme.RojoAlerta
import cl.duoc.agricol.ui.theme.RojoSuave
import cl.duoc.agricol.ui.theme.TextoSecundario
import cl.duoc.agricol.viewmodel.UsuarioViewModel

@Composable
fun PantallaPerfil(viewModel: UsuarioViewModel, onCerrarSesion: () -> Unit = {}) {
    val usuario by viewModel.usuarioActual.collectAsState()
    val inicial = usuario?.nombre?.take(1)?.uppercase() ?: "U"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar con la inicial y la insignia de cámara en la esquina
        Box {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(AzulPrimario)
                    .border(3.dp, AzulSuave, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inicial,
                    color = Color.White,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(AzulPrimario)
                    .border(2.dp, Color.White, CircleShape)
                    .padding(7.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = usuario?.nombre ?: "",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Trabajador Agrícola",
            color = AzulPrimario,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(AzulSuave)
                .padding(horizontal = 14.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        TituloSeccion("DATOS PERSONALES")
        FilaDato("RUT / Identificación", formatearRut(usuario?.rut ?: ""))
        FilaDato("Correo Electrónico", usuario?.correo ?: "")
        FilaDato("Teléfono", usuario?.let { formatearCelular(it.celular) } ?: "")

        Spacer(modifier = Modifier.height(16.dp))

        TituloSeccion("AJUSTES & DOCUMENTOS")
        FilaAjuste("Verificar Documentos de Poda")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCerrarSesion,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RojoSuave,
                contentColor = RojoAlerta
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Cerrar Sesión", fontWeight = FontWeight.Bold)
        }
    }
}

// Título pequeño en gris, como "DATOS PERSONALES"
@Composable
private fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextoSecundario,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

// Una fila con el nombre del dato a la izquierda y su valor en negrita a la derecha
@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(GrisFondo)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = etiqueta, fontSize = 13.sp, color = TextoSecundario)
        Text(text = valor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

// Una fila de ajustes con ícono a la izquierda y flecha a la derecha
@Composable
private fun FilaAjuste(texto: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(GrisFondo)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Description,
            contentDescription = null,
            tint = AzulPrimario,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = texto, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextoSecundario
        )
    }
}