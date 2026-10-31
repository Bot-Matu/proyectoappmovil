package cl.duoc.agricol.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.agricol.model.Publicacion
import cl.duoc.agricol.ui.theme.AzulOscuro
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.AzulSuave
import cl.duoc.agricol.ui.theme.FondoApp
import cl.duoc.agricol.ui.theme.TextoSecundario
import cl.duoc.agricol.viewmodel.PublicacionViewModel

@Composable
fun PantallaInicio(
    onVerDetalle: (Publicacion) -> Unit = {},
    viewModel: PublicacionViewModel
) {
    val publicaciones by viewModel.publicaciones.collectAsState()
    val categoriaSeleccionada by viewModel.categoria.collectAsState()
    val categorias = listOf("Todos", "Poda", "Riego", "Cosecha")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoApp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { BannerTemporada() }

        // Filtros: al tocar uno, el ViewModel filtra la lista
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categorias.forEach { categoria ->
                    FilterChip(
                        selected = categoria == categoriaSeleccionada,
                        onClick = { viewModel.onCategoriaChange(categoria) },
                        label = { Text(categoria) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AzulPrimario,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        items(publicaciones) { publicacion ->
            TarjetaPublicacion(
                publicacion = publicacion,
                onVerDetalle = { onVerDetalle(publicacion) }
            )
        }
    }
}

@Composable
private fun BannerTemporada() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(AzulPrimario, AzulOscuro)))
            .padding(20.dp)
    ) {
        Text(
            text = "TEMPORADA ALTA",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Trabajos Agrícolas Disponibles",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Encuentra ofertas verificadas en tu zona",
            color = Color.White,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun TarjetaPublicacion(
    publicacion: Publicacion,
    onVerDetalle: () -> Unit
) {
    // Guarda si el usuario marcó la estrella de favorito en esta tarjeta
    var favorito by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Image(
                painter = painterResource(id = publicacion.imagen),
                contentDescription = publicacion.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            // El precio va sobre la foto, arriba a la derecha
            Text(
                text = formatearPrecio(publicacion.paga),
                color = AzulPrimario,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = publicacion.titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { favorito = !favorito }) {
                    Icon(
                        imageVector = if (favorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favorito",
                        tint = if (favorito) AzulPrimario else TextoSecundario
                    )
                }
            }

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
                    color = TextoSecundario,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = publicacion.descripcion,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatearDuracion(publicacion.dias),
                    color = AzulPrimario,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(AzulSuave)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
                Button(
                    onClick = onVerDetalle,
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                ) {
                    Text(text = "Ver Detalle", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}