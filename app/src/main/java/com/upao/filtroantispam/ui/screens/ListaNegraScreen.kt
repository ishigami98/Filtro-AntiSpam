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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upao.filtroantispam.logic.FormateadorNumero
import com.upao.filtroantispam.logic.Paises
import com.upao.filtroantispam.ui.viewmodel.ListaNegraViewModel

/**
 * Números bloqueados manualmente por el usuario: se bloquean siempre,
 * sin pasar por el clasificador (ver FiltroLlamadasService).
 */
@Composable
fun ListaNegraScreen(paddingScaffold: PaddingValues, viewModel: ListaNegraViewModel = viewModel()) {
    val numerosBloqueados by viewModel.numerosBloqueados.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(paddingScaffold)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Lista Negra", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Estos números se bloquean siempre, sin evaluar nada más.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            if (numerosBloqueados.isEmpty()) {
                Text("No hay números en la lista negra.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(numerosBloqueados, key = { it.numero }) { item ->
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
                                            Icons.Filled.Block,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                    Text(FormateadorNumero.formatear(item.numero), modifier = Modifier.padding(start = 12.dp))
                                }
                                IconButton(onClick = { viewModel.eliminar(item) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Quitar")
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
            Icon(Icons.Filled.Add, contentDescription = "Agregar número")
        }
    }

    if (mostrarDialogo) {
        DialogoAgregarNumero(
            onBloquear = { numero ->
                viewModel.agregar(numero)
                mostrarDialogo = false
            },
            onReportarSpam = { numero ->
                viewModel.reportarSpam(numero)
                mostrarDialogo = false
            },
            onCancelar = { mostrarDialogo = false }
        )
    }
}

@Composable
private fun DialogoAgregarNumero(
    onBloquear: (String) -> Unit,
    onReportarSpam: (String) -> Unit,
    onCancelar: () -> Unit
) {
    var pais by remember { mutableStateOf(Paises.PREDETERMINADO) }
    var numeroNacional by remember { mutableStateOf("") }
    val numeroCompleto = numeroCompletoONull(pais, numeroNacional)

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Agregar a la lista negra") },
        text = {
            Column {
                CampoNumeroConPais(
                    pais = pais,
                    onPaisCambio = { pais = it },
                    numeroNacional = numeroNacional,
                    onNumeroCambio = { numeroNacional = it }
                )
                Text(
                    "\"Bloquear\" lo bloquea siempre. \"Reportar spam\" solo suma un reporte, " +
                        "para cuando alguien te avisa de un número sospechoso que aún no te ha llamado.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                TextButton(
                    onClick = { numeroCompleto?.let(onReportarSpam) },
                    enabled = numeroCompleto != null,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text("Reportar spam (no bloquear)")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { numeroCompleto?.let(onBloquear) },
                enabled = numeroCompleto != null
            ) {
                Text("Bloquear")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}
