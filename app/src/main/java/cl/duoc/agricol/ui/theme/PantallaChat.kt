package cl.duoc.agricol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duoc.agricol.model.Estado
import cl.duoc.agricol.model.Mensaje
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.AzulSuave
import cl.duoc.agricol.ui.theme.FondoApp
import cl.duoc.agricol.ui.theme.FondoChat
import cl.duoc.agricol.ui.theme.TextoSecundario
import cl.duoc.agricol.viewmodel.MensajeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun PantallaChat(navController: NavController, viewModel: MensajeViewModel) {
    val mensajes by viewModel.mensajesState.collectAsState()
    val nombre by viewModel.nombreOtro.collectAsState()
    var texto by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Al abrir el chat o llegar un mensaje: marcarlos como leídos y bajar al último
    LaunchedEffect(mensajes.size) {
        viewModel.marcarLeidos()
        if (mensajes.isNotEmpty()) listState.animateScrollToItem(mensajes.lastIndex)
    }

    Scaffold(
        topBar = {
            EncabezadoChat(
                nombre = nombre,
                onVolver = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoChat)
                .padding(padding)
                .consumeWindowInsets(padding)
                .imePadding()
        ) {
            // Zona de mensajes
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (mensajes.isEmpty()) {
                    Text(
                        "Escribe el primer mensaje",
                        color = TextoSecundario,
                        fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(mensajes, key = { it.id }) { mensaje ->
                        Burbuja(mensaje, esMio = mensaje.emisorId == viewModel.miId)
                    }
                }
            }

            // Barra para escribir y enviar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { if (it.length <= 1000) texto = it },
                    placeholder = { Text("Escribe un mensaje...", fontSize = 14.sp) },
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FondoApp,
                        unfocusedContainerColor = FondoApp,
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        viewModel.enviarMensaje(texto)
                        texto = ""
                    },
                    enabled = texto.isNotBlank(),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (texto.isNotBlank()) AzulPrimario else Color(0xFFCBD5E1))
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// Encabezado: flecha, avatar con la inicial y nombre de la otra persona
@Composable
private fun EncabezadoChat(nombre: String, onVolver: () -> Unit) {
    Column(modifier = Modifier.background(Color.White)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AzulPrimario),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nombre.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(text = nombre, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        HorizontalDivider(color = AzulSuave)
    }
}

// Mío -> a la derecha, azul. De la otra persona -> a la izquierda, blanca.
@Composable
private fun Burbuja(mensaje: Mensaje, esMio: Boolean) {
    // La esquina de abajo del lado de quien habla es menos redondeada
    val forma = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (esMio) 16.dp else 4.dp,
        bottomEnd = if (esMio) 4.dp else 16.dp
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (esMio) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (esMio) AzulPrimario else Color.White,
            shape = forma,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    mensaje.contenido,
                    fontSize = 14.sp,
                    color = if (esMio) Color.White else Color.Black
                )
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(mensaje.tiempo)),
                        fontSize = 10.sp,
                        color = if (esMio) Color.White.copy(alpha = 0.8f) else TextoSecundario
                    )
                    if (esMio) {
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            Icons.Default.DoneAll,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            // Se ve más claro cuando la otra persona ya lo leyó
                            tint = if (mensaje.estado == Estado.Leido) Color.White
                            else Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

// "15:30" si es de hoy; si no, "15/11" (lo usa la lista de Mensajes)
internal fun formatearHora(milisegundos: Long): String {
    val ahora = Calendar.getInstance()
    val fecha = Calendar.getInstance().apply { timeInMillis = milisegundos }
    val esHoy = ahora.get(Calendar.YEAR) == fecha.get(Calendar.YEAR) &&
            ahora.get(Calendar.DAY_OF_YEAR) == fecha.get(Calendar.DAY_OF_YEAR)
    return SimpleDateFormat(if (esHoy) "HH:mm" else "dd/MM", Locale.getDefault())
        .format(Date(milisegundos))
}