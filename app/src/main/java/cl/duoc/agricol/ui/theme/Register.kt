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
import androidx.compose.runtime.collectAsState
import cl.duoc.agricol.viewmodel.UsuarioViewModel



@Composable
fun Register(navController: NavController, viewModel: UsuarioViewModel) {

    val form by viewModel.form.collectAsState()
    val errors by viewModel.errors.collectAsState()

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
            valor = form.correo,
            alCambiar = viewModel::onCorreoChange,
            placeholder = "Agricultor@Gmail.com",
            teclado = KeyboardType.Email,
            error = errors.correo
        )

        CampoRegistro(
            etiqueta = "Nombre Completo",
            valor = form.nombre,
            alCambiar = viewModel::onNombreChange,
            placeholder = "Nombre",
            error = errors.nombre
        )

        CampoRegistro(
            etiqueta = "Celular",
            valor = form.celular,
            alCambiar = viewModel::onCelularChange,
            placeholder = "12345678",
            teclado = KeyboardType.Phone,
            prefijo = "+56 9 ",
            error = errors.celular
        )

        CampoRegistro(
            etiqueta = "Contraseña",
            valor = form.contrasena,
            alCambiar = viewModel::onContrasenaChange,
            placeholder = "xxxxxxxxxxxxx",
            teclado = KeyboardType.Password,
            esPassword = true,
            error = errors.contrasena
        )

        CampoRegistro(
            etiqueta = "Repetir Contraseña",
            valor = form.rep_contrasena,
            alCambiar = viewModel::onRepContrasenaChange,
            placeholder = "xxxxxxxxxxxxx",
            teclado = KeyboardType.Password,
            esPassword = true,
            error = errors.rep_contrasena
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = form.terminos,
                onCheckedChange = viewModel::onTerminosChange
            )
            Text(text = "Aceptar Terminos y Condiciones", fontSize = 13.sp)
        }
        errors.terminos?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (viewModel.validarRegistro()) {
                    navController.navigate("register2")
                }
            },
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

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// Esta función dibuja UN campo: etiqueta azul + caja de texto con línea abajo.
// Los parámetros son lo único que cambia entre un campo y otro.
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
    prefijo: String? = null,                    // por defecto NO tiene prefijo
    error: String? = null                       // por defecto NO tiene error
) {
    Text(
        text = etiqueta,
        color = AzulEtiqueta,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )

    TextField(
        value = valor,
        onValueChange = alCambiar,
        placeholder = { Text(text = placeholder, color = Color.LightGray) },
        prefix = if (prefijo != null) ({ Text(text = prefijo) }) else null,
        singleLine = true,
        // Si hay error, el campo se pinta en rojo y se muestra el mensaje debajo
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        visualTransformation = if (esPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(12.dp))
}