package com.upao.filtroantispam.logic

/**
 * Determina si el prefijo de país de un número es "de riesgo": uno que el
 * usuario no suele recibir (no coincide con el país del propio SIM ni con los
 * prefijos vistos entre sus contactos guardados).
 *
 * Nota: no es una tabla exhaustiva de códigos E.164 (para eso se usaría
 * libphonenumber); cubre los códigos más comunes, suficiente para un modelo
 * "simple" reimplementable como reglas en Kotlin (RNF-02).
 */
object DetectorPrefijo {

    fun extraerPrefijo(numero: String): String? {
        if (!numero.startsWith("+")) return null
        val digitos = numero.removePrefix("+").filter { it.isDigit() }
        return Paises.CODIGOS.firstOrNull { digitos.startsWith(it) }
    }

    /**
     * Un número sin código de país explícito (marcado en formato local) se
     * considera familiar por defecto: no hay señal de que venga del extranjero.
     */
    fun esPrefijoRiesgoso(numero: String, prefijosFrecuentes: Set<String>): Boolean {
        val prefijo = extraerPrefijo(numero) ?: return false
        return prefijo !in prefijosFrecuentes
    }
}
