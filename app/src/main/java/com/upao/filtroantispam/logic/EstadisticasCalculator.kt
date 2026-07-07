package com.upao.filtroantispam.logic

import com.upao.filtroantispam.data.CategoriaLlamada
import com.upao.filtroantispam.data.RegistroLlamadaEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

data class Estadisticas(
    val totalEvaluadas: Int,
    val totalBloqueadas: Int,
    val desconocidasPermitidas: Int,
    val bloqueosPorPais: List<Pair<String, Int>>,
    val bloqueosPorFranja: List<Pair<String, Int>>,
    val tendencia7Dias: List<Pair<String, Int>>
)

/**
 * Todo calculado en el cliente a partir del historial ya cargado: el volumen
 * esperado para un proyecto académico no justifica agregaciones SQL ni una
 * librería de gráficos.
 */
object EstadisticasCalculator {

    private val FRANJAS = listOf(
        0..5 to "00-05h",
        6..11 to "06-11h",
        12..17 to "12-17h",
        18..23 to "18-23h"
    )

    fun calcular(registros: List<RegistroLlamadaEntity>): Estadisticas {
        val totalEvaluadas = registros.count { it.categoria != CategoriaLlamada.CONTACTO.name }
        val totalBloqueadas = registros.count { it.bloqueada }
        val desconocidasPermitidas = registros.count {
            it.categoria == CategoriaLlamada.PROBABLE_REAL.name && !it.bloqueada
        }

        val bloqueosPorPais = registros
            .filter { it.bloqueada && it.prefijoPais != null }
            .groupingBy { it.prefijoPais!! }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }

        val calendarHora = Calendar.getInstance()
        fun horaDe(fechaHora: Long): Int {
            calendarHora.timeInMillis = fechaHora
            return calendarHora.get(Calendar.HOUR_OF_DAY)
        }
        val bloqueosPorFranja = FRANJAS.map { (rango, etiqueta) ->
            etiqueta to registros.count { it.bloqueada && horaDe(it.fechaHora) in rango }
        }

        val formatoDia = SimpleDateFormat("EEE dd", Locale("es", "PE"))
        val tendencia7Dias = (6 downTo 0).map { diasAtras ->
            val inicioDia = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -diasAtras)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val finDia = inicioDia + TimeUnit.DAYS.toMillis(1)
            val etiqueta = formatoDia.format(inicioDia)
            val cantidad = registros.count { it.bloqueada && it.fechaHora in inicioDia until finDia }
            etiqueta to cantidad
        }

        return Estadisticas(
            totalEvaluadas = totalEvaluadas,
            totalBloqueadas = totalBloqueadas,
            desconocidasPermitidas = desconocidasPermitidas,
            bloqueosPorPais = bloqueosPorPais,
            bloqueosPorFranja = bloqueosPorFranja,
            tendencia7Dias = tendencia7Dias
        )
    }
}
