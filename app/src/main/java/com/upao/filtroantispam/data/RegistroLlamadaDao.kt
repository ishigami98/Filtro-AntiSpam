package com.upao.filtroantispam.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Con uso real y prolongado, un teléfono puede acumular miles de llamadas.
// Sin tope, tanto la consulta como la lista en memoria (y su recálculo en
// Estadísticas) crecerían sin límite. 300 alcanza para "historial reciente".
private const val LIMITE_HISTORIAL = 300

@Dao
interface RegistroLlamadaDao {

    @Insert
    fun insertar(registro: RegistroLlamadaEntity)

    @Update
    fun actualizar(registro: RegistroLlamadaEntity)

    @Delete
    fun eliminar(registro: RegistroLlamadaEntity)

    @Query("SELECT * FROM registro_llamadas ORDER BY fechaHora DESC LIMIT $LIMITE_HISTORIAL")
    fun obtenerTodos(): Flow<List<RegistroLlamadaEntity>>

    // Se llama tras cada inserción para que la tabla en disco tampoco crezca sin límite.
    @Query(
        "DELETE FROM registro_llamadas WHERE id NOT IN " +
            "(SELECT id FROM registro_llamadas ORDER BY fechaHora DESC LIMIT $LIMITE_HISTORIAL)"
    )
    fun podarAntiguos()
}
