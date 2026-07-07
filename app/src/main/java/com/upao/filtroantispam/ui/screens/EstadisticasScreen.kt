package com.upao.filtroantispam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upao.filtroantispam.ui.viewmodel.EstadisticasViewModel

@Composable
fun EstadisticasScreen(paddingScaffold: PaddingValues, viewModel: EstadisticasViewModel = viewModel()) {
    val estadisticas by viewModel.estadisticas.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingScaffold).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Text("Estadísticas", style = MaterialTheme.typography.headlineMedium) }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TarjetaEstadistica("Evaluadas", estadisticas.totalEvaluadas, MaterialTheme.colorScheme.primary)
                TarjetaEstadistica("Bloqueadas", estadisticas.totalBloqueadas, MaterialTheme.colorScheme.error)
                TarjetaEstadistica("Desc. permitidas", estadisticas.desconocidasPermitidas, MaterialTheme.colorScheme.tertiary)
            }
        }

        item {
            SeccionBarras(
                titulo = "Bloqueos por país (prefijo)",
                vacio = "Todavía no hay bloqueos con prefijo detectado.",
                datos = estadisticas.bloqueosPorPais.map { (prefijo, cantidad) -> "+$prefijo" to cantidad },
                color = MaterialTheme.colorScheme.error
            )
        }

        item {
            SeccionBarras(
                titulo = "Bloqueos por franja horaria",
                vacio = "Todavía no hay bloqueos registrados.",
                datos = estadisticas.bloqueosPorFranja,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Column {
                Text("Tendencia (últimos 7 días)", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Llamadas bloqueadas por día",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
                )
                GraficoTendencia(estadisticas.tendencia7Dias)
            }
        }
    }
}

@Composable
private fun TarjetaEstadistica(etiqueta: String, valor: Int, color: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor.toString(), style = MaterialTheme.typography.titleLarge, color = color)
            Text(etiqueta, style = MaterialTheme.typography.bodyMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
private fun SeccionBarras(titulo: String, vacio: String, datos: List<Pair<String, Int>>, color: Color) {
    Column {
        Text(titulo, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
        val maximo = (datos.maxOfOrNull { it.second } ?: 0).coerceAtLeast(1)
        if (datos.all { it.second == 0 }) {
            Text(vacio, style = MaterialTheme.typography.bodyMedium)
        } else {
            datos.forEach { (etiqueta, cantidad) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(etiqueta, modifier = Modifier.width(70.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(18.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(color.copy(alpha = 0.15f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(cantidad.toFloat() / maximo)
                                .fillMaxSize()
                                .clip(RoundedCornerShape(4.dp))
                                .background(color)
                        )
                    }
                    Text(cantidad.toString(), modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun GraficoTendencia(datos: List<Pair<String, Int>>) {
    val maximo = (datos.maxOfOrNull { it.second } ?: 0).coerceAtLeast(1)
    Row(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        datos.forEach { (etiqueta, cantidad) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                Text(cantidad.toString(), style = MaterialTheme.typography.bodyMedium)
                Box(modifier = Modifier.weight(1f).width(24.dp), contentAlignment = Alignment.BottomCenter) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(cantidad.toFloat() / maximo)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(MaterialTheme.colorScheme.error)
                    )
                }
                Text(
                    etiqueta,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
