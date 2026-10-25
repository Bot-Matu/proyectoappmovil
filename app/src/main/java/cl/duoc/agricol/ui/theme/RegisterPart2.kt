package cl.duoc.agricol.ui.theme

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duoc.agricol.ui.theme.Inicio

@Composable
fun RegisterPart2(navController: NavController){

    var rut by remember { mutableStateOf("") }
    var nro_documento by remember { mutableStateOf("") }
    var modal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.Start

    ){

        Spacer(modifier = Modifier.height(64.dp))

        Row(verticalAlignment = Alignment.CenterVertically){
            IconButton(
                onClick = {navController.popBackStack()},
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

        Spacer(modifier = Modifier.height(40.dp))

        CampoRegistro(
            etiqueta = "R.U.T",
            valor = rut,
            alCambiar = { rut = it },
            placeholder = "12.345.678-9"
        )

        CampoRegistro(
            etiqueta = "Nro Documento",
            valor = nro_documento,
            alCambiar = { nro_documento = it },
            placeholder = "000.000.000"
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { modal = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GrisBoton)

        ){
            Text(text = "Registrarme", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
    if (modal) {
        AlertDialog(
            onDismissRequest = { modal = false },
            title = { Text("Registro exitoso") },
            text = { Text("Tu cuenta fue creada correctamente") },
            confirmButton = {
                Button(onClick = {
                    modal = false
                    navController.navigate("login")
                }) {
                    Text("OK")
                }
            }
        )
    }

}