package com.upao.filtroantispam.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackUsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun marcar(feedback: FeedbackUsuarioEntity)

    @Query("SELECT * FROM feedback_usuario ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<FeedbackUsuarioEntity>>

    // Consulta síncrona a propósito: FiltroLlamadasService la necesita antes
    // de que suene el teléfono (RNF-02), igual que el resto de consultas del
    // servicio. Se trae la lista completa porque el match es por dígitos
    // finales (ver NormalizadorNumero), no exacto.
    @Query("SELECT * FROM feedback_usuario")
    fun obtenerTodosSync(): List<FeedbackUsuarioEntity>
}
