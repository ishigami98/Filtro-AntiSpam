package com.upao.filtroantispam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Contador local (RF-05, feedback loop): cuántas veces un número desconocido
 * llamó y colgó casi de inmediato. Alimenta `reportesPrevios` en
 * ClasificadorSpam. Es un sustituto local de la lista negra comunitaria
 * (aspiracional, requiere backend) mencionada en el diseño original.
 */
@Entity(tableName = "reportes_locales")
data class ReporteLocalEntity(
    @PrimaryKey val numero: String,
    val cantidad: Int = 1
)
