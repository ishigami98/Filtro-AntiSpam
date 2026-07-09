package com.upao.filtroantispam.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NumeroBloqueadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun agregar(numero: NumeroBloqueadoEntity)

    @Delete
    fun eliminar(numero: NumeroBloqueadoEntity)

    @Query("SELECT * FROM numeros_bloqueados ORDER BY fechaAgregado DESC")
    fun obtenerTodos(): Flow<List<NumeroBloqueadoEntity>>

    // Consulta síncrona a propósito: FiltroLlamadasService necesita la respuesta
    // antes de que suene el teléfono (RNF-02), igual que las consultas a
    // ContactsContract/CallLog en el mismo servicio. Se trae la lista completa
    // (en vez de comparar en SQL) porque el match contra el número real de la
    // llamada es por dígitos finales (ver NormalizadorNumero), no exacto.
    @Query("SELECT * FROM numeros_bloqueados")
    fun obtenerTodosSync(): List<NumeroBloqueadoEntity>
}
