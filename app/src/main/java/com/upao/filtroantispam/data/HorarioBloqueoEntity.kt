package com.upao.filtroantispam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Franja horaria en la que se bloquean todas las llamadas de números
 * desconocidos (los contactos siguen pasando siempre). [minutoInicio] y
 * [minutoFin] son minutos desde medianoche (0-1439, ej. 22:30 -> 1350);
 * si minutoFin < minutoInicio, la franja cruza la medianoche (ej. 22:00 -> 6:00).
 */
@Entity(tableName = "horarios_bloqueo")
data class HorarioBloqueoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val minutoInicio: Int,
    val minutoFin: Int,
    val activo: Boolean = true
)
