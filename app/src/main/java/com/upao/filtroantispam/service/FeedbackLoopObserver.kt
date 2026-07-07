package com.upao.filtroantispam.service

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.CallLog
import android.util.Log
import com.upao.filtroantispam.data.FiltroDatabase
import com.upao.filtroantispam.data.PreferenciasFiltro

private const val TAG = "FeedbackLoopObserver"
private const val DURACION_SOSPECHOSA_SEGUNDOS = 10

/**
 * RF-05: `duracion` no sirve para decidir antes de contestar (por eso no está
 * en CaracteristicasLlamada), pero después de colgar sí es una señal útil.
 * Si un número desconocido llama y la llamada dura casi nada, es un patrón
 * típico de robocall/spam: se refuerza su contador de reportes para que la
 * próxima vez ClasificadorSpam lo considere más sospechoso.
 */
object FeedbackLoopObserver {

    @Volatile private var registrado = false
    @Volatile private var ultimoIdProcesado = 0L

    fun registrar(context: Context) {
        if (registrado) return
        val appContext = context.applicationContext
        try {
            appContext.contentResolver.registerContentObserver(
                CallLog.Calls.CONTENT_URI,
                true,
                object : ContentObserver(Handler(Looper.getMainLooper())) {
                    override fun onChange(selfChange: Boolean) {
                        procesarUltimaLlamada(appContext)
                    }
                }
            )
            registrado = true
        } catch (e: SecurityException) {
            Log.w(TAG, "Sin permiso READ_CALL_LOG, feedback loop desactivado por ahora", e)
        }
    }

    private fun procesarUltimaLlamada(context: Context) {
        if (!PreferenciasFiltro.filtroLlamadasActivo(context)) return
        try {
            context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DURATION),
                null,
                null,
                "${CallLog.Calls.DATE} DESC LIMIT 1"
            )?.use { cursor ->
                if (!cursor.moveToFirst()) return

                val id = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls._ID))
                if (id <= ultimoIdProcesado) return
                ultimoIdProcesado = id

                val numero = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                val tipo = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                val duracion = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION))

                if (numero.isNullOrBlank()) return
                if (tipo != CallLog.Calls.INCOMING_TYPE) return // ignora salientes, perdidas y las que ya bloqueamos
                if (duracion >= DURACION_SOSPECHOSA_SEGUNDOS) return
                if (ContactosUtil.estaEnContactos(context, numero)) return

                FiltroDatabase.obtenerInstancia(context).reporteLocalDao().incrementar(numero)
                Log.d(TAG, "Llamada corta de número desconocido ($numero, ${duracion}s): reforzando patrón de spam")
            }
        } catch (e: SecurityException) {
            Log.w(TAG, "Sin permiso para leer CallLog en el feedback loop", e)
        }
    }
}
