package cl.duoc.agricol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.agricol.model.Usuario
import cl.duoc.agricol.ui.theme.AzulEtiqueta
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.CampoRegistro
import cl.duoc.agricol.viewmodel.PublicacionViewModel

@Composable
fun PantallaCrearPublicacion(
    viewModel: PublicacionViewModel,
    usuario: Usuario?,
    onVolver: () -> Unit
) {
    val form by viewModel.form.collectAsState()
    val errors by viewModel.errors.collectAsState()
    val publicado by viewModel.publicado.collectAsState()
    val categorias = listOf("Poda", "Riego", "Cosecha")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Nueva publicación",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CampoRegistro(
            etiqueta = "Título",
            valor = form.titulo,
            alCambiar = viewModel::onTituloChange,
            placeholder = "Ej: Cosecha de cerezas",
            error = errors.titulo
        )

        // Categoría: al elegirla se define también la foto de la publicación
        Text(
            text = "Categoría",
            color = AzulEtiqueta,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categorias.forEach { categoria ->
                FilterChip(
                    selected = form.categoria == categoria,
                    onClick = { viewModel.onCategoriaFormChange(categoria) },
                    label = { Text(categoria) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AzulPrimario,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        errors.categoria?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))

        CampoRegistro(
            etiqueta = "Comuna",
            valor = form.comuna,
            alCambiar = viewModel::onComunaChange,
            placeholder = "Ej: Curicó",
            error = errors.comuna
        )

        CampoRegistro(
            etiqueta = "Región",
            valor = form.ciudad,
            alCambiar = viewModel::onCiudadChange,
            placeholder = "Ej: Región del Maule",
            error = errors.ciudad
        )

        CampoRegistro(
            etiqueta = "Fecha de inicio",
            valor = form.fecha_inicio,
            alCambiar = viewModel::onFechaInicioChange,
            placeholder = "dd/mm/aaaa",
            error = errors.fecha_inicio
        )

        CampoRegistro(
            etiqueta = "Fecha de término",
            valor = form.fecha_termino,
            alCambiar = viewModel::onFechaTerminoChange,
            placeholder = "dd/mm/aaaa",
            error = errors.fecha_termino
        )

        CampoRegistro(
            etiqueta = "Trabajadores necesitados",
            valor = form.tra_necesitados,
            alCambiar = viewModel::onTrabajadoresChange,
            placeholder = "1",
            teclado = KeyboardType.Number,
            error = errors.tra_necesitados
        )

        CampoRegistro(
            etiqueta = "Pago",
            valor = form.paga,
            alCambiar = viewModel::onPagaChange,
            placeholder = "300000",
            teclado = KeyboardType.Number,
            prefijo = "$ ",
            error = errors.paga
        )

        CampoRegistro(
            etiqueta = "Descripción",
            valor = form.descripcion,
            alCambiar = viewModel::onDescripcionChange,
            placeholder = "Cuenta de qué trata el trabajo",
            unaLinea = false,
            error = errors.descripcion
        )

        CampoRegistro(
            etiqueta = "Requisitos",
            valor = form.requisitos,
            alCambiar = viewModel::onRequisitosChange,
            placeholder = "Ej: 1 año de experiencia",
            unaLinea = false,
            error = errors.requisitos
        )

        CampoRegistro(
            etiqueta = "Información extra (opcional)",
            valor = form.info_extra,
            alCambiar = viewModel::onInfoExtraChange,
            placeholder = "Ej: Se entrega almuerzo",
            unaLinea = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { usuario?.let { viewModel.publicar(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
        ) {
            Text(text = "Publicar", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Modal de confirmación
    if (publicado) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Publicación creada") },
            text = { Text("Tu oferta ya aparece en Inicio y en Buscar.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.cerrarModal()
                    onVolver()
                }) { Text("OK") }
            }
        )
    }
}
