package com.upao.filtroantispam.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.upao.filtroantispam.logic.Pais
import com.upao.filtroantispam.logic.Paises

/**
 * Un AlertDialog con la lista de países, no un ExposedDropdownMenu: anidar un
 * menú desplegable (Popup) dentro de otro AlertDialog (otra ventana) hace que
 * el menú quede oculto detrás del diálogo, aunque siga siendo "clickeable".
 */
@Composable
fun SelectorPais(paisSeleccionado: Pais, onSeleccionar: (Pais) -> Unit, modifier: Modifier = Modifier) {
    var mostrarLista by remember { mutableStateOf(false) }

    OutlinedCard(onClick = { mostrarLista = true }, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${paisSeleccionado.bandera} +${paisSeleccionado.codigo} ${paisSeleccionado.nombre}")
            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Elegir país")
        }
    }

    if (mostrarLista) {
        AlertDialog(
            onDismissRequest = { mostrarLista = false },
            title = { Text("Elegir país") },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(Paises.LISTA.sortedBy { it.nombre }) { pais ->
                        Text(
                            "${pais.bandera} +${pais.codigo} ${pais.nombre}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSeleccionar(pais)
                                    mostrarLista = false
                                }
                                .padding(vertical = 12.dp)
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { mostrarLista = false }) { Text("Cancelar") }
            }
        )
    }
}

/** Selector de país + campo de número nacional, validando la cantidad de dígitos según el país. */
@Composable
fun CampoNumeroConPais(
    pais: Pais,
    onPaisCambio: (Pais) -> Unit,
    numeroNacional: String,
    onNumeroCambio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val error = numeroNacional.isNotEmpty() && numeroNacional.length != pais.digitosNumero

    Column(modifier = modifier) {
        SelectorPais(pais, onPaisCambio, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = numeroNacional,
            onValueChange = { nuevo -> if (nuevo.all { it.isDigit() }) onNumeroCambio(nuevo) },
            label = { Text("Número (${pais.digitosNumero} dígitos)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = error,
            supportingText = {
                if (error) {
                    Text("Debe tener ${pais.digitosNumero} dígitos (tiene ${numeroNacional.length})")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}

/** null si el número nacional no tiene la cantidad de dígitos que espera el país elegido. */
fun numeroCompletoONull(pais: Pais, numeroNacional: String): String? =
    if (numeroNacional.length == pais.digitosNumero) "+${pais.codigo}$numeroNacional" else null
