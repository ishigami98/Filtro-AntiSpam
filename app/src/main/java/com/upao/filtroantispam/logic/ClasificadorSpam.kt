package com.upao.filtroantispam.logic

/**
 * Reimplementación del Random Forest (Fase 0) como reglas ponderadas directas,
 * para poder ejecutarse en Kotlin en menos de 200ms (RNF-02) sin necesitar
 * TensorFlow Lite.
 *
 * Casos que motivan estos pesos:
 * - Un prefijo extranjero por sí solo NO basta para bloquear: podría ser un
 *   familiar en el extranjero. Pero si ese número YA fue reportado antes
 *   (reportesPrevios >= 1), queda "en la cuerda floja": unas pocas llamadas
 *   más ya alcanzan para bloquearlo.
 * - Para un número nacional (sin prefijoRiesgo) reportado una sola vez, se
 *   exige más repetición (~5 llamadas) antes de bloquear, para no penalizar
 *   un reporte hecho por error (ej. un familiar cuyo celular falla y cuelga).
 * - Un formato de número imposible (muy corto/secuencial) es casi exclusivo
 *   de spoofing/VoIP: alcanza por sí solo para bloquear.
 */
object ClasificadorSpam {

    const val UMBRAL_BLOQUEO = 0.85

    fun evaluar(c: CaracteristicasLlamada): ResultadoClasificacion {
        if (c.enContactos) return ResultadoClasificacion(0.0, emptyList())

        val factores = mutableListOf<Factor>()

        if (c.esNumeroCortoOExtrano) {
            factores += Factor("formato de número atípico (típico de spoofing/VoIP)", 1.0)
        }

        if (c.prefijoRiesgo) {
            factores += Factor("prefijo de país no habitual", 0.30)
        }

        when {
            c.reportesPrevios >= 5 ->
                factores += Factor("${c.reportesPrevios} reportes previos de la comunidad", 0.35)
            c.reportesPrevios >= 1 ->
                factores += Factor("${c.reportesPrevios} reporte(s) previo(s)", 0.15)
        }

        // Un número ya reportado antes está "en la cuerda floja": cada llamada
        // adicional pesa mucho más que si nunca hubiera sido reportado.
        val enCuerdaFloja = c.reportesPrevios >= 1
        when {
            c.frecuenciaLlamadasNumero >= 10 -> factores += Factor(
                "frecuencia muy alta (${c.frecuenciaLlamadasNumero} llamadas/semana)",
                if (enCuerdaFloja) 0.85 else 0.35
            )
            c.frecuenciaLlamadasNumero >= 5 -> factores += Factor(
                "frecuencia alta (${c.frecuenciaLlamadasNumero} llamadas/semana)",
                if (enCuerdaFloja) 0.75 else 0.15
            )
            c.frecuenciaLlamadasNumero >= 2 -> factores += Factor(
                "ha llamado ${c.frecuenciaLlamadasNumero} veces esta semana",
                if (enCuerdaFloja) 0.55 else 0.05
            )
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
