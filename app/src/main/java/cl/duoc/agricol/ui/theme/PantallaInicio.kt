package cl.duoc.agricol.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.TextoSecundario
import cl.duoc.agricol.viewmodel.PublicacionViewModel

@Composable
fun PantallaInicio(viewModel: PublicacionViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(viewModel.publicaciones) { pub ->
            Column(modifier = Modifier.fillMaxWidth()) {
                // Imagen de portada
                Box {
                    Image(
                        painter = painterResource(id = pub.imagen),
                        contentDescription = pub.titulo,
                        modifier = Modifier.fillMaxWidth().height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    // Estrella favorito
                    var favorito by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = { favorito = !favorito },
                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                    ) {
                        Icon(
                            imageVector = if (favorito) Icons.Default.Star else Icons.Outlined.Star,
                            contentDescription = "Favorito",
                            tint = if (favorito) Color(0xFFFBBF24) else Color.White
                        )
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    // Título + botón Detalles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = pub.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { /* Ver detalle */ }) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextoSecundario)
                            Text("Detalles", fontSize = 12.sp, color = TextoSecundario)
                        }
                    }

                    // Ubicación
                    Text(
                        "${pub.comuna} - ${pub.ciudad}",
                        fontSize = 12.sp,
                        color = TextoSecundario
                    )

                    // Fechas y personas
                    Text(
                        "${pub.fecha_inicio} - ${pub.fecha_termino} • ${pub.dias} Días • ${pub.tra_necesitados} Persona",
                        fontSize = 11.sp,
                        color = TextoSecundario
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Precio + Aplicar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "\$${pub.paga} CLP total",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                "\$${pub.paga / pub.dias} por día • ${pub.dias} días.",
                                fontSize = 11.sp,
                                color = TextoSecundario
                            )
                        }
                        Button(
                            onClick = { /* aplicar */ },
                            colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        ) {
                            Text("Aplicar", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }

                Divider(color = Color(0xFFE5E7EB), thickness = 1.dp)
            }
        }
    }
}