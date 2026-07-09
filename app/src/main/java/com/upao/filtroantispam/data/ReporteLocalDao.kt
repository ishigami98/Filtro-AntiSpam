package com.upao.filtroantispam.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ReporteLocalDao {

    // "Exacto" porque el número ya normalizado/elegido (ver incrementarNormalizado
    // en NumeroCoincidenciaExt) es el que se guarda o incrementa tal cual.
    @Query(
        """
        INSERT INTO reportes_locales(numero, cantidad) VALUES(:numeroExacto, 1)
        ON CONFLICT(numero) DO UPDATE SET cantidad = cantidad + 1
        """
    )
    fun incrementarExacto(numeroExacto: String)

    // Consulta síncrona a propósito: FiltroLlamadasService la usa para construir
    // CaracteristicasLlamada antes de que suene el teléfono (RNF-02). Se trae
    // la lista completa porque el match es por dígitos finales (ver
    // NormalizadorNumero), no exacto.
    @Query("SELECT * FROM reportes_locales")
    fun obtenerTodosSync(): List<ReporteLocalEntity>
}
