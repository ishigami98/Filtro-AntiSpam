package com.upao.filtroantispam.logic

/** Un factor que contribuyó a la probabilidad de spam, para mostrar el motivo (transparencia). */
data class Factor(val descripcion: String, val peso: Double)

data class ResultadoClasificacion(
    val probabilidad: Double,
    val factores: List<Factor>
) {
    /** Texto legible con los factores de mayor peso, para mostrar en el detalle de la llamada. */
    fun motivo(maximo: Int = 2): String? =
        factores.sortedByDescending { it.peso }
            .take(maximo)
            .takeIf { it.isNotEmpty() }
            ?.joinToString(" + ") { it.descripcion }
}
