package cl.duoc.agricol.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duoc.agricol.ui.theme.Inicio
import androidx.compose.runtime.collectAsState
import cl.duoc.agricol.viewmodel.UsuarioViewModel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline

@Composable
fun RegisterPart2(navController: NavController, viewModel: UsuarioViewModel) {

    val form by viewModel.form.collectAsState()
    val errors by viewModel.errors.collectAsState()
    val mensajeModal by viewModel.mensajeModal.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(64.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(GrisBoton)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Verifica tu identidad",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Selector de rol: lo elegido queda guardado en el usuario
        Text(
            text = "Selecciona tu Rol",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TarjetaRol(
                texto = "Trabajador",
                icono = Icons.Outlined.HowToReg,
                seleccionado = form.rol == "Trabajador",
                onClick = { viewModel.onRolChange("Trabajador") },
                modifier = Modifier.weight(1f)
            )
            TarjetaRol(
                texto = "Productor",
                icono = Icons.Outlined.BusinessCenter,
                seleccionado = form.rol == "Productor",
                onClick = { viewModel.onRolChange("Productor") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        CampoRegistro(
            etiqueta = "R.U.T",
            valor = form.rut,
            alCambiar = viewModel::onRutChange,
            placeholder = "12.345.678-9",
            error = errors.rut
        )

        CampoRegistro(
            etiqueta = "Nro Documento",
            valor = form.nro_documento,
            alCambiar = viewModel::onNroDocumentoChange,
            placeholder = "000.000.000",
            error = errors.nro_documento
        )

        CamaraDocumento()

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { viewModel.registrar() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GrisBoton)
        ) {
            Text(text = "Registrarme", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // El modal aparece solo cuando el ViewModel tiene un mensaje para mostrar
    mensajeModal?.let { mensaje ->
        DialogAgricol(
            titulo = if (registroExitoso) "¡Registro exitoso!" else "No se pudo registrar",
            mensaje = mensaje,
            icono = if (registroExitoso) Icons.Outlined.CheckCircle else Icons.Outlined.ErrorOutline,
            colorIcono = if (registroExitoso) VerdeEstado else RojoAlerta,
            textoBoton = if (registroExitoso) "Ir a iniciar sesión" else "Entendido",
            onAceptar = {
                val fueExitoso = registroExitoso
                viewModel.cerrarModal()
                if (fueExitoso) {
                    navController.navigate("login")
                }
            },
            onCerrar = { /* obliga a presionar el botón */ }
        )
    }

}


// Una tarjeta de rol: azul si está seleccionada, gris si no
@Composable
private fun TarjetaRol(
    texto: String,
    icono: ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .clip(forma)
            .background(if (seleccionado) AzulSuave else Color.White)
            .border(2.dp, if (seleccionado) AzulPrimario else GrisFila, forma)
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = if (seleccionado) AzulPrimario else GrisBoton,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = texto,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) AzulPrimario else Color.Black
        )
    }
}
