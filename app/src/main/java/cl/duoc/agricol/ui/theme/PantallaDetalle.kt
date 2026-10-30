package cl.duoc.agricol.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.AzulSuave
import cl.duoc.agricol.ui.theme.TextoSecundario

@Composable
fun PantallaDetalle(
    publicacion: Publicacion,
    onVolver: () -> Unit,
    onChat: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Parte que se desplaza (todo menos la barra de abajo)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                Image(
                    painter = painterResource(id = publicacion.imagen),
                    contentDescription = publicacion.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = publicacion.categoria.uppercase(),
                    color = AzulPrimario,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = publicacion.titulo,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = AzulPrimario,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${publicacion.comuna}, ${publicacion.ciudad}",
                        color = TextoSecundario
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Caja con el pago estimado y la duración
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(AzulSuave)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Pago estimado",
                            color = TextoSecundario,
                            fontSize = 13.sp
                        )
                        Text(
                            text = formatearPrecio(publicacion.paga),
                            color = AzulPrimario,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = formatearDuracion(publicacion.dias),
                        color = AzulPrimario,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                SeccionDetalle("Descripción:", publicacion.descripcion)
                SeccionDetalle("Requisitos:", publicacion.requisitos)
                SeccionDetalle(
                    "Información adicional:",
                    "Contratante: ${publicacion.contratante}\n${publicacion.info_extra}"
                )
            }
        }

        // Barra fija de abajo: postular y chat
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /* aquí irá aplicar al trabajo */ },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
            ) {
                Text(text = "Aplicar / Postular", fontWeight = FontWeight.Bold)
            }

            IconButton(
                onClick = onChat,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AzulSuave)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Chatear con el contratante",
                    tint = AzulPrimario
                )
            }
        }
    }
}

// Un título en negrita con su texto debajo
@Composable
private fun SeccionDetalle(titulo: String, contenido: String) {
    Text(text = titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(6.dp))
    Text(text = contenido, fontSize = 14.sp, color = TextoSecundario)
    Spacer(modifier = Modifier.height(16.dp))
}