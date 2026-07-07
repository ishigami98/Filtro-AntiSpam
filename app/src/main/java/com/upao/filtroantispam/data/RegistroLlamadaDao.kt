package com.upao.filtroantispam.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroLlamadaDao {

    @Insert
    fun insertar(registro: RegistroLlamadaEntity)

    @Update
    fun actualizar(registro: RegistroLlamadaEntity)

    @Delete
    fun eliminar(registro: RegistroLlamadaEntity)

    @Query("SELECT * FROM registro_llamadas ORDER BY fechaHora DESC")
    fun obtenerTodos(): Flow<List<RegistroLlamadaEntity>>
}
