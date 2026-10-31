package cl.duoc.agricol.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary          = AzulPrimario,
    onPrimary        = Color.White,
    secondary        = AzulEtiqueta,
    onSecondary      = Color.White,
    background       = Color.White,
    surface          = Color.White,
    onBackground     = Color(0xFF111827),
    onSurface        = Color(0xFF111827),
)

@Composable
fun AgricolTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography(),
        content     = content
    )
}


