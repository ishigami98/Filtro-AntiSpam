package com.upao.filtroantispam.data

import android.content.Context

private const val PREFS = "filtro_antispam_prefs"
private const val KEY_LLAMADAS = "filtro_llamadas_activo"

/**
 * El toggle "Filtro de Llamadas" de Registro no es solo decorativo:
 * FiltroLlamadasService y FeedbackLoopObserver lo consultan antes de evaluar nada.
 */
object PreferenciasFiltro {

    fun filtroLlamadasActivo(context: Context): Boolean =
        prefs(context).getBoolean(KEY_LLAMADAS, true)

    fun setFiltroLlamadasActivo(context: Context, activo: Boolean) {
        prefs(context).edit().putBoolean(KEY_LLAMADAS, activo).apply()
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
