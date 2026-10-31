package cl.duoc.agricol.ui.theme

import android.R.color.white
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duoc.agricol.viewmodel.UsuarioViewModel
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ErrorOutline

// IMPORTACIÓN CRÍTICA: Esto le dice al código dónde encontrar tus recursos como drawables
import cl.duoc.agricol.R

@Composable
fun Login(navController: NavController, viewModel: UsuarioViewModel) {
    var rut by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verPassword by remember { mutableStateOf(false) }
    var modal by remember { mutableStateOf(false) }
    val errorLogin by viewModel.errorLogin.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        // Logo circular azul con tu imagen adentro
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Image(

                painter = painterResource(id = R.drawable.mi_logo_interno),
                contentDescription = "Logo de la aplicación",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agricolab",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = Color(0xFF111827)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text("Inicia Sesion", fontSize = 26.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Rut
        Text("Rut", color = AzulEtiqueta, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        TextField(
            value = rut,
            onValueChange = { rut = it },
            placeholder = { Text("xx.xxx.xxx-k", color = Color.LightGray) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor   = AzulPrimario
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo Contraseña con ojo
        Text("Contraseña", color = AzulEtiqueta, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("xxxxxxxxxx", color = Color.LightGray) },
            singleLine = true,
            visualTransformation = if (verPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { verPassword = !verPassword }) {
                    Icon(
                        imageVector = if (verPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor   = AzulPrimario
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón principal
        Button(
            onClick = {
                viewModel.iniciarSesion(rut, password) {
                    navController.navigate("home")
                }
            },
            enabled = !cargando,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor         = GrisBoton,
                disabledContainerColor = Color.LightGray
            )
        ) {
            if (cargando) {
                CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp, color = Color.White)
            } else {
                Text("Inicia sesion", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Link registro
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("No tienes Cuenta? Registrate ", fontSize = 12.sp, color = TextoSecundario)
            Text(
                "aquí",
                fontSize = 12.sp,
                color = AzulEtiqueta,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { navController.navigate("register") }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Botón Google (outlined)
        OutlinedButton(
            onClick = { /* Google login pendiente */ },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Text("G  ", fontWeight = FontWeight.Bold, color = AzulPrimario, fontSize = 16.sp)
            Text("Continuar con Google", fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Link recuperar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Olvidaste tu contraseña? ", fontSize = 12.sp, color = TextoSecundario)
            Text(
                "Recuperar",
                fontSize = 12.sp,
                color = AzulEtiqueta,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { modal = true }
            )
        }

        // Modales de error / recuperar
        if (modal) {
            DialogAgricol(
                titulo = "Recuperar contraseña",
                mensaje = "Te enviaremos un correo con las instrucciones.",
                icono = Icons.Outlined.Email,
                colorIcono = AzulPrimario,
                onAceptar = { modal = false }
            )
        }

        errorLogin?.let { msg ->
            DialogAgricol(
                titulo = "No se pudo iniciar sesión",
                mensaje = msg,
                icono = Icons.Outlined.ErrorOutline,
                colorIcono = RojoAlerta,
                onAceptar = { viewModel.cerrarErrorLogin() }
            )
        }
    }
}
