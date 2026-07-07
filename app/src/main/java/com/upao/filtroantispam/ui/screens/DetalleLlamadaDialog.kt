package com.upao.filtroantispam.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.upao.filtroantispam.data.CategoriaLlamada
import com.upao.filtroantispam.data.FeedbackUsuarioEntity
import com.upao.filtroantispam.data.RegistroLlamadaEntity
import com.upao.filtroantispam.logic.FormateadorNumero
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DetalleLlamadaDialog(
    registro: RegistroLlamadaEntity,
    feedback: FeedbackUsuarioEntity?,
    onMarcarSpam: () -> Unit,
    onMarcarNoSpam: () -> Unit,
    onEliminar: () -> Unit,
    onCerrar: () -> Unit
) {
    val categoria = runCatching { CategoriaLlamada.valueOf(registro.categoria) }.getOrNull()

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(FormateadorNumero.formatear(registro.numero)) },
        text = {
            Column {
                Text(FORMATO_FECHA_DETALLE.format(registro.fechaHora))
                Text(
                    when (categoria) {
                        CategoriaLlamada.CONTACTO -> "Contacto guardado"
                        CategoriaLlamada.BLOQUEO_MANUAL -> "Bloqueada manualmente (${registro.motivo ?: "regla manual"})"
                        CategoriaLlamada.SPAM_MODELO -> "Bloqueada por el clasificador (${"%.0f".format(registro.probabilidadSpam * 100)}% prob. spam)"
                        CategoriaLlamada.PROBABLE_REAL, null -> "Permitida (${"%.0f".format(registro.probabilidadSpam * 100)}% prob. spam)"
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
                if (categoria == CategoriaLlamada.SPAM_MODELO || categoria == CategoriaLlamada.PROBABLE_REAL) {
                    registro.motivo?.let {
                        Text("Motivo: $it", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
                    }
                }

                if (categoria != CategoriaLlamada.CONTACTO) {
                    Text(
                        "¿Esta llamada era spam de verdad?",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    if (feedback != null) {
                        Text(
                            if (feedback.marcadoSpam) "Marcada por ti como SPAM" else "Marcada por ti como NO spam",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        OutlinedButton(
                            onClick = onMarcarSpam,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Sí era spam")
                        }
                        OutlinedButton(
                            onClick = onMarcarNoSpam,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("No era spam")
                        }
                    }
                }

                TextButton(
                    onClick = onEliminar,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Eliminar del historial")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onCerrar) { Text("Cerrar") }
        }
    )
}

private val FORMATO_FECHA_DETALLE = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
