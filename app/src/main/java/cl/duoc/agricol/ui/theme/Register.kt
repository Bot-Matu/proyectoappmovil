package cl.duoc.agricol.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

val AzulEtiqueta = Color(0xFF4A5BD0)
val GrisBoton = Color(0xFF50525F)

@Composable
fun Register(navController: NavController) {

    var correo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rep_password by remember { mutableStateOf("") }
    var terminos by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "Registrate",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        CampoRegistro(
            etiqueta = "Email",
            valor = correo,
            alCambiar = { correo = it },
            placeholder = "Agricultor@Gmail.com",
            teclado = KeyboardType.Email
        )

        CampoRegistro(
            etiqueta = "Nombre Completo",
            valor = nombre,
            alCambiar = { nombre = it },
            placeholder = "Nombre"
        )

        CampoRegistro(
            etiqueta = "Celular",
            valor = telefono,
            alCambiar = { if (it.all { letra -> letra.isDigit() }) telefono = it },
            placeholder = "12345678",
            teclado = KeyboardType.Phone,
            prefijo = "+56 9 "
        )

        CampoRegistro(
            etiqueta = "Contraseña",
            valor = password,
            alCambiar = { password = it },
            placeholder = "xxxxxxxxxxxxx",
            teclado = KeyboardType.Password,
            esPassword = true
        )

        CampoRegistro(
            etiqueta = "Repetir Contraseña",
            valor = rep_password,
            alCambiar = { rep_password = it },
            placeholder = "xxxxxxxxxxxxx",
            teclado = KeyboardType.Password,
            esPassword = true
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = terminos,
                onCheckedChange = { terminos = it }
            )
            Text(text = "Aceptar Terminos y Condiciones", fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("register2") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GrisBoton)
        ) {
            Text(text = "Continuar", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Tienes Cuenta? ")
            Text(
                text = "Inicia Sesion",
                color = AzulEtiqueta,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }

    }
        Spacer(modifier = Modifier.height(32.dp))
}

// Esta función dibuja UN campo: etiqueta azul + caja de texto con línea abajo.
// Los parámetros son lo único que cambia entre un campo y otro.
@Composable
fun CampoRegistro(
    etiqueta: String,
    valor: String,
    alCambiar: (String) -> Unit,
    placeholder: String,
    teclado: KeyboardType = KeyboardType.Text,  // valor por defecto: teclado normal
    esPassword: Boolean = false,                // por defecto NO es contraseña
    prefijo: String? = null                     // por defecto NO tiene prefijo
) {
    Text(
        text = etiqueta,
        color = AzulEtiqueta,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )

    // TextField en vez de OutlinedTextField.
    // OutlinedTextField dibuja una caja con borde completo.
    // TextField dibuja solo la línea de abajo (mockup).
    TextField(
        value = valor,
        onValueChange = alCambiar,
        // placeholder = texto gris de ejemplo que desaparece al escribir
        placeholder = { Text(text = placeholder, color = Color.LightGray) },
        prefix = if (prefijo != null) ({ Text(text = prefijo) }) else null,
        singleLine = true,
        // Si es contraseña, muestra puntitos; si no, muestra el texto normal.
        visualTransformation = if (esPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        // Fondo transparente: por defecto TextField trae un fondo gris/morado.
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(12.dp))
}