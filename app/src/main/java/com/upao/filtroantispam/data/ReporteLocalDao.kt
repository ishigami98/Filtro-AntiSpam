package com.upao.filtroantispam.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ReporteLocalDao {

    @Query(
        """
        INSERT INTO reportes_locales(numero, cantidad) VALUES(:numero, 1)
        ON CONFLICT(numero) DO UPDATE SET cantidad = cantidad + 1
        """
    )
    fun incrementar(numero: String)

    // Consulta síncrona a propósito: FiltroLlamadasService la usa para construir
    // CaracteristicasLlamada antes de que suene el teléfono (RNF-02).
    @Query("SELECT cantidad FROM reportes_locales WHERE numero = :numero")
    fun obtenerCantidad(numero: String): Int?
}
