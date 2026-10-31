package cl.duoc.agricol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.navigation.NavController
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.AzulSuave
import cl.duoc.agricol.ui.theme.FondoApp
import cl.duoc.agricol.ui.theme.TextoSecundario
import cl.duoc.agricol.viewmodel.ConversacionesViewModel

@Composable
fun PantallaMensajes(
    navController: NavController,
    viewModel: ConversacionesViewModel,
    modifier: Modifier = Modifier
) {
    val conversaciones by viewModel.conversaciones.collectAsState()
    val contactos by viewModel.contactos.collectAsState()

    var busqueda by remember { mutableStateOf("") }
    var mostrarContactos by remember { mutableStateOf(false) }

    val filtradas = conversaciones.filter { it.nombre.contains(busqueda.trim(), ignoreCase = true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FondoApp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Mensajes y Contactos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar mensaje...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = AzulPrimario)
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = AzulSuave
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filtradas.isEmpty()) {
                Text(
                    text = if (conversaciones.isEmpty())
                        "Aún no tienes conversaciones. Toca + para iniciar una."
                    else
                        "No se encontraron conversaciones.",
                    color = TextoSecundario,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filtradas, key = { it.conversacionId }) { conv ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(1.dp, AzulSuave, RoundedCornerShape(16.dp))
                                .clickable {
                                    navController.navigate("chat/${conv.conversacionId}/${conv.otroUsuarioId}")
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar con la inicial del nombre
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(AzulPrimario),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = conv.nombre.take(1).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = conv.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = formatearHora(conv.ultimoMensajeTiempo),
                                        fontSize = 11.sp,
                                        color = TextoSecundario
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = conv.ultimoMensaje,
                                        fontSize = 13.sp,
                                        color = TextoSecundario,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (conv.noLeidos > 0) {
                                        Badge(containerColor = AzulPrimario) {
                                            Text(conv.noLeidos.toString())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { mostrarContactos = true },
            containerColor = AzulPrimario,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo chat")
        }
    }

    // Personas con las que se puede empezar un chat
    if (mostrarContactos) {
        AlertDialog(
            onDismissRequest = { mostrarContactos = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            title = { Text("Nuevo chat") },
            text = {
                if (contactos.isEmpty()) {
                    Text("Todavía no hay otros usuarios registrados en este dispositivo.")
                } else {
                    LazyColumn {
                        items(contactos, key = { it.id }) { usuario ->
                            Text(
                                text = usuario.nombre,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        mostrarContactos = false
                                        viewModel.abrirChat(usuario.id) { conversacionId ->
                                            navController.navigate("chat/$conversacionId/${usuario.id}")
                                        }
                                    }
                                    .padding(vertical = 12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { mostrarContactos = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = AzulPrimario)
                ) { Text("Cancelar") }
            }
        )
    }
}