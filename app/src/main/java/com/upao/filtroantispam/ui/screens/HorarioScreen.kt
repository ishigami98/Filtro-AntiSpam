package com.upao.filtroantispam.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upao.filtroantispam.logic.FormateadorHora
import com.upao.filtroantispam.ui.viewmodel.HorarioViewModel

/**
 * Franjas horarias en las que se bloquean todas las llamadas de números
 * desconocidos (los contactos siguen pasando siempre).
 */
@Composable
fun HorarioScreen(paddingScaffold: PaddingValues, viewModel: HorarioViewModel = viewModel()) {
    val horarios by viewModel.horarios.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(paddingScaffold)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Horario de bloqueo", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Bloquea llamadas de números desconocidos en la franja indicada.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            if (horarios.isEmpty()) {
                Text("No hay franjas horarias configuradas.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(horarios, key = { it.id }) { franja ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier.size(40.dp).clip(CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Filled.Schedule,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        "${FormateadorHora.formato12h(franja.minutoInicio)} - " +
                                            FormateadorHora.formato12h(franja.minutoFin),
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Switch(
                                        checked = franja.activo,
                                        onCheckedChange = { viewModel.cambiarActivo(franja, it) }
                                    )
                                    IconButton(onClick = { viewModel.eliminar(franja) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Quitar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { mostrarDialogo = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar franja")
        }
    }

    if (mostrarDialogo) {
        DialogoNuevaFranja(
            onConfirmar = { minutoInicio, minutoFin ->
                viewModel.agregar(minutoInicio, minutoFin)
                mostrarDialogo = false
            },
            onCancelar = { mostrarDialogo = false }
        )
    }
}

@Composable
private fun DialogoNuevaFranja(onConfirmar: (Int, Int) -> Unit, onCancelar: () -> Unit) {
    var minutoInicio by remember { mutableIntStateOf(22 * 60) } // 10:00 PM por defecto
    var minutoFin by remember { mutableIntStateOf(6 * 60) } // 6:00 AM por defecto
    var mostrarSelectorInicio by remember { mutableStateOf(false) }
    var mostrarSelectorFin by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Nueva franja de bloqueo") },
        text = {
            Column {
                TextButton(onClick = { mostrarSelectorInicio = true }) {
                    Text("Desde: ${FormateadorHora.formato12h(minutoInicio)}")
                }
                TextButton(onClick = { mostrarSelectorFin = true }) {
                    Text("Hasta: ${FormateadorHora.formato12h(minutoFin)}")
                }
                Text(
                    "Se bloquearán llamadas desconocidas de ${FormateadorHora.formato12h(minutoInicio)} " +
                        "a ${FormateadorHora.formato12h(minutoFin)}.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirmar(minutoInicio, minutoFin) }) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )

    if (mostrarSelectorInicio) {
        DialogoSelectorHora(
            titulo = "Hora de inicio",
            minutoInicial = minutoInicio,
            onConfirmar = {
                minutoInicio = it
                mostrarSelectorInicio = false
            },
            onCancelar = { mostrarSelectorInicio = false }
        )
    }
    if (mostrarSelectorFin) {
        DialogoSelectorHora(
            titulo = "Hora de fin",
            minutoInicial = minutoFin,
            onConfirmar = {
                minutoFin = it
                mostrarSelectorFin = false
            },
            onCancelar = { mostrarSelectorFin = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoSelectorHora(
    titulo: String,
    minutoInicial: Int,
    onConfirmar: (Int) -> Unit,
    onCancelar: () -> Unit
) {
    val estado = rememberTimePickerState(
        initialHour = minutoInicial / 60,
        initialMinute = minutoInicial % 60,
        is24Hour = false
    )

    Dialog(onDismissRequest = onCancelar) {
        Surface(shape = MaterialTheme.shapes.large) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(titulo, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 12.dp))
                TimePicker(state = estado)
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    TextButton(onClick = onCancelar) { Text("Cancelar") }
                    TextButton(onClick = { onConfirmar(estado.hour * 60 + estado.minute) }) { Text("Aceptar") }
                }
            }
        }
    }
}
