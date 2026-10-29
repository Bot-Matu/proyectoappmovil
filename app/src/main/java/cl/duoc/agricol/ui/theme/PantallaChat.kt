package cl.duoc.agricol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duoc.agricol.model.Estado
import cl.duoc.agricol.model.Mensaje
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.TextoSecundario
import cl.duoc.agricol.viewmodel.MensajeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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
            TopAppBar(
                title = { Text(nombre, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .consumeWindowInsets(padding)
                .imePadding()
        ) {
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
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(mensajes, key = { it.id }) { mensaje ->
                        Burbuja(mensaje, esMio = mensaje.emisorId == viewModel.miId)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { if (it.length <= 1000) texto = it },
                    placeholder = { Text("Escribe un mensaje", fontSize = 13.sp) },
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 4,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        viewModel.enviarMensaje(texto)
                        texto = ""
                    },
                    enabled = texto.isNotBlank()
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar",
                        tint = if (texto.isNotBlank()) AzulPrimario else TextoSecundario
                    )
                }
            }
        }
    }
}

// Mío -> a la derecha, azul. De la otra persona -> a la izquierda, gris.
@Composable
private fun Burbuja(mensaje: Mensaje, esMio: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (esMio) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (esMio) AzulPrimario else Color(0xFFE5E7EB),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
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