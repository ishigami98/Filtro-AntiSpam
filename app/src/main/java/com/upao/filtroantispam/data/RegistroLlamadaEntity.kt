package com.upao.filtroantispam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Una fila por cada llamada evaluada por FiltroLlamadasService (RF-06).
 */
@Entity(tableName = "registro_llamadas")
data class RegistroLlamadaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val numero: String,
    val fechaHora: Long,
    val bloqueada: Boolean,
    val probabilidadSpam: Double,
    val categoria: String = CategoriaLlamada.PROBABLE_REAL.name,
    // Solo tiene sentido para SPAM_MODELO/PROBABLE_REAL: por qué el clasificador decidió esto.
    val motivo: String? = null,
    // Código de país detectado (para las estadísticas por país); null si no aplica (ej. contactos).
    val prefijoPais: String? = null,
    // Marca registros de ejemplo/prueba (ya sin forma de crearse desde la UI,
    // pero se conserva para no perder ni migrar los que ya existen).
    val esDemo: Boolean = false
)
