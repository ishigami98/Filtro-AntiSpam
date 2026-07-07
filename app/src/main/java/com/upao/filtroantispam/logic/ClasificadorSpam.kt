package com.upao.filtroantispam.logic

/**
 * Reimplementación del Random Forest (Fase 0) como reglas ponderadas directas,
 * para poder ejecutarse en Kotlin en menos de 200ms (RNF-02) sin necesitar
 * TensorFlow Lite. Los pesos aproximan la importancia de variables observada
 * en el notebook: prefijo/frecuencia/reportes pesan más que formato/hora.
 */
object ClasificadorSpam {

    const val UMBRAL_BLOQUEO = 0.85

    fun evaluar(c: CaracteristicasLlamada): ResultadoClasificacion {
        if (c.enContactos) return ResultadoClasificacion(0.0, emptyList())

        val factores = mutableListOf<Factor>()

        if (c.prefijoRiesgo) {
            factores += Factor("prefijo de país no habitual", 0.35)
        }

        when {
            c.frecuenciaLlamadasNumero >= 10 ->
                factores += Factor("frecuencia alta (${c.frecuenciaLlamadasNumero} llamadas/semana)", 0.30)
            c.frecuenciaLlamadasNumero >= 5 ->
                factores += Factor("frecuencia moderada (${c.frecuenciaLlamadasNumero} llamadas/semana)", 0.15)
        }

        when {
            c.reportesPrevios >= 5 ->
                factores += Factor("${c.reportesPrevios} reportes previos de la comunidad", 0.30)
            c.reportesPrevios >= 1 ->
                factores += Factor("${c.reportesPrevios} reporte(s) previo(s)", 0.10)
        }

        if (c.esNumeroCortoOExtrano) {
            factores += Factor("formato de número atípico", 0.20)
        }

        if (c.horaDelDia in 9..18) {
            factores += Factor("llamada en horario laboral", 0.05)
        }

        val probabilidad = factores.sumOf { it.peso }.coerceIn(0.0, 1.0)
        return ResultadoClasificacion(probabilidad, factores)
    }

    fun probabilidadSpam(c: CaracteristicasLlamada): Double = evaluar(c).probabilidad

    fun esSpam(c: CaracteristicasLlamada): Boolean = probabilidadSpam(c) > UMBRAL_BLOQUEO
}
