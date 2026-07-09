package com.upao.filtroantispam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Lista negra manual: si un número está aquí, se bloquea siempre,
 * sin pasar por ClasificadorSpam.
 */
@Entity(tableName = "numeros_bloqueados")
data class NumeroBloqueadoEntity(
    @PrimaryKey val numero: String,
    val fechaAgregado: Long = System.currentTimeMillis(),
    val motivo: String? = null,
    val esDemo: Boolean = false
)
