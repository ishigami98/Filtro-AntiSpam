package com.upao.filtroantispam.service

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log

private const val TAG = "ContactosUtil"

/**
 * Compartido por FiltroLlamadasService y FeedbackLoopObserver: ambos necesitan
 * saber si un número está guardado en contactos antes de actuar sobre él.
 */
object ContactosUtil {
    fun estaEnContactos(context: Context, numero: String): Boolean = try {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(numero)
        )
        context.contentResolver.query(uri, arrayOf(ContactsContract.PhoneLookup._ID), null, null, null)
            ?.use { cursor -> cursor.moveToFirst() } ?: false
    } catch (e: SecurityException) {
        Log.w(TAG, "Sin permiso READ_CONTACTS, se asume que no está en contactos", e)
        false
    }
}
