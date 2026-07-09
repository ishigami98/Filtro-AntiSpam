package com.upao.filtroantispam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upao.filtroantispam.data.CategoriaLlamada
import com.upao.filtroantispam.data.PreferenciasFiltro
import com.upao.filtroantispam.data.RegistroLlamadaEntity
import com.upao.filtroantispam.logic.FormateadorNumero
import com.upao.filtroantispam.ui.theme.AmbarAdvertencia
import com.upao.filtroantispam.ui.theme.RojoOscuro
import com.upao.filtroantispam.ui.viewmodel.RegistroViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RegistroScreen(paddingScaffold: PaddingValues, viewModel: RegistroViewModel = viewModel()) {
    val context = LocalContext.current
    var filtroLlamadasActivo by remember { mutableStateOf(PreferenciasFiltro.filtroLlamadasActivo(context)) }
    val registros by viewModel.registros.collectAsState()
    val feedbackPorNumero by viewModel.feedbackPorNumero.collectAsState()
    val estado = rememberEstadoProteccion()
    var registroSeleccionado by remember { mutableStateOf<RegistroLlamadaEntity?>(null) }

    val totalEvaluadas = registros.count { it.categoria != CategoriaLlamada.CONTACTO.name }
    val totalBloqueadas = registros.count { it.bloqueada }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingScaffold).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EncabezadoProteccion(
                protegido = estado.protegido && filtroLlamadasActivo,
                filtroActivo = filtroLlamadasActivo,
                totalEvaluadas = totalEvaluadas,
                totalBloqueadas = totalBloqueadas
            )
        }

        if (!estado.protegido) {
            item { PermisosYRolCard(estado) }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaToggle("Filtro de Llamadas", filtroLlamadasActivo) {
                        filtroLlamadasActivo = it
                        PreferenciasFiltro.setFiltroLlamadasActivo(context, it)
                    }
                }
            }
        }

        item {
            Text(
                "Historial reciente",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (registros.isEmpty()) {
            item { Text("Todavía no se ha evaluado ninguna llamada.") }
        } else {
            items(registros, key = { it.id }) { registro ->
                FilaRegistro(registro) { registroSeleccionado = registro }
            }
        }
    }

    registroSeleccionado?.let { registro ->
        DetalleLlamadaDialog(
            registro = registro,
            feedback = feedbackPorNumero[registro.numero],
            onMarcarSpam = {
                viewModel.marcarSpam(registro)
                registroSeleccionado = null
            },
            onMarcarNoSpam = {
                viewModel.marcarNoSpam(registro)
                registroSeleccionado = null
            },
            onEliminar = {
                viewModel.eliminarRegistro(registro)
                registroSeleccionado = null
            },
            onCerrar = { registroSeleccionado = null }
        )
    }
}

@Composable
private fun EncabezadoProteccion(protegido: Boolean, filtroActivo: Boolean, totalEvaluadas: Int, totalBloqueadas: Int) {
    val colorFondo = if (protegido) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
    val titulo = when {
        !filtroActivo -> "Filtro desactivado"
        !protegido -> "Protección incompleta"
        else -> "Protegido"
    }
    val subtitulo = when {
        !filtroActivo -> "Actívalo arriba para volver a bloquear llamadas spam"
        !protegido -> "Completa los pasos de abajo para activarlo"
        else -> "El filtro de llamadas está activo"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Shield,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Text(
                titulo,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(subtitulo, color = Color.White, textAlign = TextAlign.Center)

            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                EstadisticaRapida(valor = totalEvaluadas, etiqueta = "Evaluadas")
                EstadisticaRapida(valor = totalBloqueadas, etiqueta = "Bloqueadas")
            }
        }
    }
}

@Composable
private fun EstadisticaRapida(valor: Int, etiqueta: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor.toString(), style = MaterialTheme.typography.titleLarge, color = Color.White)
        Text(etiqueta, color = Color.White)
    }
}

@Composable
private fun FilaToggle(etiqueta: String, activo: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(etiqueta)
        Switch(checked = activo, onCheckedChange = onCambio)
    }
}

private val FORMATO_FECHA = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

private data class EstiloCategoria(val icono: ImageVector, val color: Color, val etiqueta: String)

@Composable
private fun estiloDe(categoria: CategoriaLlamada?): EstiloCategoria = when (categoria) {
    CategoriaLlamada.CONTACTO -> EstiloCategoria(Icons.Filled.Person, MaterialTheme.colorScheme.tertiary, "Contacto")
    CategoriaLlamada.BLOQUEO_MANUAL -> EstiloCategoria(Icons.Filled.Block, RojoOscuro, "Bloqueo manual")
    CategoriaLlamada.SPAM_MODELO -> EstiloCategoria(Icons.Filled.Block, MaterialTheme.colorScheme.error, "Spam")
    CategoriaLlamada.PROBABLE_REAL, null -> EstiloCategoria(Icons.Filled.HelpOutline, AmbarAdvertencia, "Probable real")
}

@Composable
private fun FilaRegistro(registro: RegistroLlamadaEntity, onClick: () -> Unit) {
    val categoria = runCatching { CategoriaLlamada.valueOf(registro.categoria) }.getOrNull()
    val estilo = estiloDe(categoria)

    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconoEstado(icono = estilo.icono, color = estilo.color)

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(FormateadorNumero.formatear(registro.numero), style = MaterialTheme.typography.titleMedium)
                    if (registro.esDemo) {
                        Text(
                            "DEMO",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        )
                    }
                }
                Text(FORMATO_FECHA.format(registro.fechaHora), style = MaterialTheme.typography.bodyMedium)
                Text(
                    if (categoria == CategoriaLlamada.BLOQUEO_MANUAL) {
                        estilo.etiqueta
                    } else {
                        "${estilo.etiqueta} · prob. spam ${"%.0f".format(registro.probabilidadSpam * 100)}%"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = estilo.color
                )
            }
        }
    }
}

@Composable
private fun IconoEstado(icono: ImageVector, color: Color) {
    Box(
        modifier = Modifier.size(40.dp).clip(CircleShape).background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icono, contentDescription = null, tint = color)
    }
}
