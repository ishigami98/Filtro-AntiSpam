package com.upao.filtroantispam.logic

/**
 * El número que el usuario escribe en Lista Negra (estilo E.164, con
 * "+código de país") no siempre coincide byte a byte con lo que
 * `Call.Details.handle.schemeSpecificPart` reporta para una llamada real: el
 * operador/Telecom puede omitir el código de país en llamadas nacionales, o
 * usar otro formato. Comparar los últimos N dígitos evita que ese desfase de
 * formato haga fallar el match exacto en base de datos.
 */
object NormalizadorNumero {
    private const val DIGITOS_COMPARACION = 8

    fun sonElMismoNumero(a: String, b: String): Boolean {
        val da = a.filter { it.isDigit() }
        val db = b.filter { it.isDigit() }
        if (da.isEmpty() || db.isEmpty()) return false
        val largo = minOf(da.length, db.length, DIGITOS_COMPARACION)
        return da.takeLast(largo) == db.takeLast(largo)
    }
}
