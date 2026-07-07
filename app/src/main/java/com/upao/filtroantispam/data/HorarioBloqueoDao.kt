package com.upao.filtroantispam.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HorarioBloqueoDao {

    @Insert
    fun agregar(horario: HorarioBloqueoEntity)

    @Update
    fun actualizar(horario: HorarioBloqueoEntity)

    @Delete
    fun eliminar(horario: HorarioBloqueoEntity)

    @Query("SELECT * FROM horarios_bloqueo ORDER BY minutoInicio")
    fun obtenerTodos(): Flow<List<HorarioBloqueoEntity>>

    // Consulta síncrona a propósito, igual que NumeroBloqueadoDao.estaBloqueado:
    // FiltroLlamadasService necesita la respuesta antes de que suene el teléfono (RNF-02).
    @Query("SELECT * FROM horarios_bloqueo WHERE activo = 1")
    fun obtenerActivos(): List<HorarioBloqueoEntity>
}
