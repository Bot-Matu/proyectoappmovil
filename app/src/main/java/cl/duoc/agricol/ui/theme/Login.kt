package cl.duoc.agricol.ui.theme



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import cl.duoc.agricol.viewmodel.UsuarioViewModel
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.size


@Composable
fun Login(navController: NavController, viewModel: UsuarioViewModel){


    var rut by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verPassword by remember {mutableStateOf(false)}
    var modal by remember { mutableStateOf(false) }
    val errorLogin by viewModel.errorLogin.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.Start

    ){

        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF2F6BFF))
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center

        ){
            Text(text = "A", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agricolab",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Iniciar Sesion", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Rut", color = AzulEtiqueta, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        TextField(
            value = rut,
            onValueChange = {rut = it},
            placeholder = { Text(text = "xx.xxx.xxx-k", color = Color.LightGray) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Contraseña", color = AzulEtiqueta, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        TextField(
            value = password,
            onValueChange = {password = it},
            placeholder = { Text(text = "xxxxxxxxxx", color = Color.LightGray) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.iniciarSesion(rut, password) {
                    navController.navigate("home")
                }
            },
            enabled = !cargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GrisBoton,
                disabledContainerColor = Color.LightGray
            )
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 2.dp,
                    color = AzulEtiqueta
                )
            } else {
                Text(text = "Iniciar Sesion", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "No tienes Cuenta? Registrate ", fontSize = 12.sp)
            Text(
                text = "aquí",
                fontSize = 12.sp,
                color = AzulEtiqueta,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { navController.navigate("register") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* pendiente: login con Google */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Text(text = "Continuar con Google", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Olvidaste tu contraseña? ", fontSize = 12.sp, color = Color.Gray)
            Text(
                text = "Recuperar",
                fontSize = 12.sp,
                color = AzulEtiqueta,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { modal = true  }
            )
        }

        if (modal) {
            AlertDialog(
                onDismissRequest = { modal = false },
                title = { Text("Recuperar contraseña") },
                text = { Text("Te enviaremos un correo con las instrucciones.") },
                confirmButton = {
                    Button(onClick = { modal = false }) {
                        Text("OK")
                    }
                }
            )
        }
        errorLogin?.let { mensaje ->
            AlertDialog(
                onDismissRequest = { viewModel.cerrarErrorLogin() },
                title = { Text("No se pudo iniciar sesión") },
                text = { Text(mensaje) },
                confirmButton = {
                    Button(onClick = { viewModel.cerrarErrorLogin() }) {
                        Text("OK")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))


    }
}