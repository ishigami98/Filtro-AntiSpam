package com.upao.filtroantispam.logic

/**
 * Detecta formatos de número atípicos (VoIP/spoofing): muy cortos o con
 * dígitos secuenciales/repetidos.
 */
object DetectorFormatoNumero {

    private val SECUENCIAL_O_REPETIDO = Regex(
        "(0123456789|1234567890|9876543210)|(\\d)\\1{5,}"
    )

    fun esNumeroCortoOExtrano(numero: String): Boolean {
        val soloDigitos = numero.filter { it.isDigit() }
        if (soloDigitos.length < 7) return true
        return SECUENCIAL_O_REPETIDO.containsMatchIn(soloDigitos)
    }
}
