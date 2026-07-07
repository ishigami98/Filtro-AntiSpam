package com.upao.filtroantispam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Corrección del usuario sobre un número (RF-05). `marcadoSpam = false` actúa
 * como lista blanca personal: ese número deja de bloquearse aunque el
 * clasificador lo marque como sospechoso. `marcadoSpam = true` refuerza el
 * contador de reportes (ver ReporteLocalDao) para la próxima evaluación.
 */
@Entity(tableName = "feedback_usuario")
data class FeedbackUsuarioEntity(
    @PrimaryKey val numero: String,
    val marcadoSpam: Boolean,
    val fecha: Long = System.currentTimeMillis()
)
