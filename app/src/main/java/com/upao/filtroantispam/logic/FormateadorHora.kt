package com.upao.filtroantispam.logic

/** Formatea minutos desde medianoche (0-1439) como "10:00 PM", para que no haya
 * ambigüedad entre formato 24h y 12h al mostrar una franja horaria. */
object FormateadorHora {
    fun formato12h(minutosDesdeMedianoche: Int): String {
        val horas24 = (minutosDesdeMedianoche / 60) % 24
        val minutos = minutosDesdeMedianoche % 60
        val periodo = if (horas24 < 12) "AM" else "PM"
        val horas12 = when (val h = horas24 % 12) {
            0 -> 12
            else -> h
        }
        return "%d:%02d %s".format(horas12, minutos, periodo)
    }
}
