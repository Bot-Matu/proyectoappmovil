package cl.duoc.agricol.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.agricol.viewmodel.PublicacionViewModel

@Composable
fun PantallaInicio(viewModel: PublicacionViewModel = viewModel()) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(viewModel.publicaciones) { publicacion ->
            Column {
                Image(
                    painter = painterResource(id = publicacion.imagen),
                    contentDescription = publicacion.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = publicacion.titulo,
                    style = MaterialTheme.typography.titleLarge
                )
                Text("${publicacion.comuna} - ${publicacion.ciudad}")
                Text("${publicacion.fecha_inicio} - ${publicacion.fecha_termino} • ${publicacion.dias} Días • ${publicacion.tra_necesitados} Persona(s)")

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "\$${publicacion.paga} CLP total",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("\$${publicacion.paga / publicacion.dias} por día • ${publicacion.dias} días.")
                    }
                    Button(onClick = { /* aquí irá aplicar al trabajo */ }) {
                        Text("Aplicar")
                    }
                }
            }
        }
    }
}