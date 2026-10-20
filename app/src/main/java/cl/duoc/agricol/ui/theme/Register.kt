package cl.duoc.agricol.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cl.duoc.agricol.ui.theme.Inicio

@Composable
fun Register(){

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rep_password by remember { mutableStateOf("") }
    var modal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(36.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(text="Registro")
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(text = "Correo")

        OutlinedTextField(
            value = correo,
            onValueChange = { nuevoTexto ->
                correo = nuevoTexto
            },
            label = { Text("Introduce tu correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nombre")

        OutlinedTextField(
            value = nombre,
            onValueChange = { nuevoTexto ->
                nombre = nuevoTexto
            },
            label = { Text("Introduce tu Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "telefono")

        OutlinedTextField(
            value = telefono,
            onValueChange = { newValue ->

                if (newValue.all { it.isDigit() }) {
                    telefono = newValue
                }
            },
            label = { Text("Teléfono") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Contraseña")

        OutlinedTextField(
            value = password,
            onValueChange = { nuevoTexto ->
                password = nuevoTexto
            },
            label = { Text("Introduce tu contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Repetir Contraseña")

        OutlinedTextField(
            value = rep_password,
            onValueChange = { nuevoTexto ->
                rep_password = nuevoTexto
            },
            label = { Text("Introduce tu contraseña nuevamente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { modal = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Continuar")

        }



        if(modal){
            AlertDialog(
                onDismissRequest = { modal = false },
                title = { Text("Mensaje") },
                text = { Text("Se debe crear el usuario: $nombre con la contraseña:$password") },
                confirmButton = {
                    Button(onClick = { modal = false }) {
                        Text("OK")
                    }
                }
            )
        }

    }

}