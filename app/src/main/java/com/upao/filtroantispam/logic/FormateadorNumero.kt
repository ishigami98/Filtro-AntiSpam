package com.upao.filtroantispam.logic

/** Inserta un espacio entre el prefijo de país y el resto del número: +51 957037350. */
object FormateadorNumero {
    fun formatear(numero: String): String {
        val prefijo = DetectorPrefijo.extraerPrefijo(numero) ?: return numero
        val resto = numero.removePrefix("+").removePrefix(prefijo)
        return if (resto.isNotEmpty()) "+$prefijo $resto" else numero
    }
}
