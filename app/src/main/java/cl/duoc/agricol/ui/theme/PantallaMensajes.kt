package cl.duoc.agricol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.TextoSecundario

data class ConversacionUi(val nombre: String, val ultimoMensaje: String)

@Composable
fun PantallaMensajes(modifier: Modifier = Modifier) {
    var busqueda by remember { mutableStateOf("") }

    // Reemplaza por tus datos reales (ViewModel / BD)
    val conversaciones = remember {
        listOf(
            ConversacionUi("Mario Soto", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla"),
            ConversacionUi("Maria Perez", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla"),
            ConversacionUi("Diego Raul", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla")
        )
    }
    val filtradas = conversaciones.filter { it.nombre.contains(busqueda, ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = { Text("Buscar mensaje...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AzulPrimario) },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filtradas) { conv ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AzulPrimario),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            conv.nombre.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(conv.nombre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(
                            conv.ultimoMensaje,
                            fontSize = 11.sp,
                            color = TextoSecundario,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Icon(
                        Icons.Default.DoneAll,
                        contentDescription = "Leído",
                        tint = AzulPrimario,
                        modifier = Modifier.size(16.dp)
                    )
                }
                HorizontalDivider(color = Color(0xFFE5E7EB))
            }
        }
    }
}