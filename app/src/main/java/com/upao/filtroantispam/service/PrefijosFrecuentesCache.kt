package com.upao.filtroantispam.service

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.upao.filtroantispam.logic.DetectorPrefijo

private const val TAG = "PrefijosFrecuentesCache"
private const val TTL_MS = 60 * 60 * 1000L // 1 hora

/**
 * Android puede recrear FiltroLlamadasService en cada llamada entrante (no es
 * un singleton garantizado): un caché por instancia (`by lazy` en el propio
 * servicio) no evita que se recorran TODOS los contactos del teléfono en cada
 * llamada. En un teléfono real con cientos de contactos, eso es justo el tipo
 * de trabajo repetido que agota memoria/CPU rápido. Este caché vive a nivel
 * de proceso (singleton real), así que sobrevive a que el servicio se recree.
 */
object PrefijosFrecuentesCache {
    @Volatile private var cache: Set<String> = emptySet()
    @Volatile private var calculadoEnMs: Long = 0L

    fun obtener(context: Context): Set<String> {
        val ahora = System.currentTimeMillis()
        if (ahora - calculadoEnMs > TTL_MS) {
            synchronized(this) {
                if (ahora - calculadoEnMs > TTL_MS) {
                    cache = calcular(context)
                    calculadoEnMs = ahora
                }
            }
        }
        return cache
    }

    private fun calcular(context: Context): Set<String> = try {
        val prefijos = mutableSetOf<String>()
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null, null, null
        )?.use { cursor ->
            val columna = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (cursor.moveToNext()) {
                val numeroContacto = cursor.getString(columna) ?: continue
                DetectorPrefijo.extraerPrefijo(numeroContacto)?.let { prefijos.add(it) }
            }
        }
        prefijos
    } catch (e: SecurityException) {
        Log.w(TAG, "Sin permiso READ_CONTACTS, ningún prefijo se considera frecuente", e)
        emptySet()
    }
}
