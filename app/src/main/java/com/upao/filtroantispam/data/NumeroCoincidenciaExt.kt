package com.upao.filtroantispam.data

import com.upao.filtroantispam.logic.NormalizadorNumero

/**
 * El número real de una llamada (Call.Details) no siempre coincide byte a
 * byte con el que el usuario escribió (ver NormalizadorNumero). Estas
 * funciones centralizan ese match "por dígitos finales" para Lista Negra,
 * feedback y reportes locales, en vez de comparar con `=` en SQL.
 */

fun NumeroBloqueadoDao.estaBloqueadoNormalizado(numero: String): Boolean =
    obtenerTodosSync().any { NormalizadorNumero.sonElMismoNumero(it.numero, numero) }

fun FeedbackUsuarioDao.obtenerNormalizado(numero: String): FeedbackUsuarioEntity? =
    obtenerTodosSync().firstOrNull { NormalizadorNumero.sonElMismoNumero(it.numero, numero) }

fun ReporteLocalDao.obtenerCantidadNormalizado(numero: String): Int =
    obtenerTodosSync()
        .filter { NormalizadorNumero.sonElMismoNumero(it.numero, numero) }
        .sumOf { it.cantidad }

/**
 * Si ya existe una fila cuyo número coincide (por dígitos finales) con
 * [numero], se incrementa ESA fila tal cual está guardada, en vez de crear
 * una fila nueva con el formato de esta llamada: si no, cada variante de
 * formato del mismo número fragmentaría el conteo de reportes en filas
 * separadas.
 */
fun ReporteLocalDao.incrementarNormalizado(numero: String) {
    val existente = obtenerTodosSync().firstOrNull { NormalizadorNumero.sonElMismoNumero(it.numero, numero) }
    incrementarExacto(existente?.numero ?: numero)
}
