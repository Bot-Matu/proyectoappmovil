package cl.duoc.agricol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.agricol.model.Notificacion
import cl.duoc.agricol.ui.theme.AzulPrimario
import cl.duoc.agricol.ui.theme.AzulSuave
import cl.duoc.agricol.ui.theme.TextoSecundario

@Composable
fun PanelNotificaciones(
    expandido: Boolean,
    notificaciones: List<Notificacion>,
    onCerrar: () -> Unit
) {
    DropdownMenu(
        expanded = expandido,
        onDismissRequest = onCerrar,
        modifier = Modifier
            .width(280.dp)
            .background(Color.White)
    ) {
        Text(
            text = "Notificaciones",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPrimario,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        HorizontalDivider(color = AzulSuave)

        notificaciones.forEach { notificacion ->
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notificacion.titulo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = notificacion.hora,
                        fontSize = 11.sp,
                        color = TextoSecundario
                    )
                }
                Text(
                    text = notificacion.detalle,
                    fontSize = 13.sp,
                    color = TextoSecundario
                )
            }
        }
    }
}