package cl.duoc.agricol.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.agricol.ui.theme.Inicio

@Composable
fun RegisterPart2(navController: NavController){

    var rut by remember { mutableStateOf("") }
    var nro_documento by remember { mutableStateOf("") }

    var modal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(text="Verificate")
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){

        Text(text = "RUT")

        OutlinedTextField(
            value = rut,
            onValueChange = { nuevoTexto ->
                rut = nuevoTexto
            },
            label = { Text("Introduce tu RUT") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nro Documento")

        OutlinedTextField(
            value = nro_documento,
            onValueChange = { nuevoTexto ->
                nro_documento = nuevoTexto
            },
            label = { Text("Introduce tu Numero de Documento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(42.dp))

        Button(
            onClick = { modal = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarme")

        }

        Spacer(modifier = Modifier.height(62.dp))




        if(modal){
            AlertDialog(
                onDismissRequest = { modal = false },
                title = { Text("Mensaje") },
                text = { Text("Se debe crear el usuario: $rut con la contraseña:$nro_documento") },
                confirmButton = {
                    Button(onClick = { modal = false }) {
                        Text("OK")
                    }
                }
            )
        }

    }

}