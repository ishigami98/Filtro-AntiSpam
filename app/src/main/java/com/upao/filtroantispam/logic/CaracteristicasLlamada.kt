package com.upao.filtroantispam.logic

/**
 * Las 6 variables del modelo, todas conocidas antes de contestar la llamada
 * (ver Fase0_ModeloAntispam.ipynb). `enContactos` se resuelve antes de construir
 * esta clase: si es true, la llamada nunca pasa por [ClasificadorSpam].
 */
data class CaracteristicasLlamada(
    val enContactos: Boolean,
    val prefijoRiesgo: Boolean,
    val frecuenciaLlamadasNumero: Int,
    val horaDelDia: Int,
    val esNumeroCortoOExtrano: Boolean,
    val reportesPrevios: Int
)
