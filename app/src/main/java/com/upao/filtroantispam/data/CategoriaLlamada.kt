package com.upao.filtroantispam.data

/**
 * Cómo se decidió el destino de una llamada. Determina color/ícono en el
 * historial (contacto=verde, bloqueo manual=rojo oscuro, spam=rojo,
 * probable real=amarillo/gris).
 */
enum class CategoriaLlamada {
    CONTACTO,
    BLOQUEO_MANUAL,
    SPAM_MODELO,
    PROBABLE_REAL
}
