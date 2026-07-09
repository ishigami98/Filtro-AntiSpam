package com.upao.filtroantispam.service

import android.provider.CallLog
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.upao.filtroantispam.data.CategoriaLlamada
import com.upao.filtroantispam.data.FiltroDatabase
import com.upao.filtroantispam.data.PreferenciasFiltro
import com.upao.filtroantispam.data.RegistroLlamadaEntity
import com.upao.filtroantispam.data.estaBloqueadoNormalizado
import com.upao.filtroantispam.data.obtenerCantidadNormalizado
import com.upao.filtroantispam.data.obtenerNormalizado
import com.upao.filtroantispam.logic.CaracteristicasLlamada
import com.upao.filtroantispam.logic.ClasificadorSpam
import com.upao.filtroantispam.logic.DetectorFormatoNumero
import com.upao.filtroantispam.logic.DetectorPrefijo
import java.util.Calendar
import java.util.concurrent.TimeUnit

private const val TAG = "FiltroLlamadasService"
private const val DIAS_VENTANA_FRECUENCIA = 7L

/**
 * Intercepta llamadas entrantes antes de que suene el teléfono (RF-04, RNF-02).
 * Extrae las 6 variables del modelo (Fase 0) usando solo metadatos (RNF-04:
 * nunca se analiza el audio) y aplica [ClasificadorSpam].
 */
class FiltroLlamadasService : CallScreeningService() {

    private val db by lazy { FiltroDatabase.obtenerInstancia(applicationContext) }

    override fun onScreenCall(callDetails: Call.Details) {
        // Este servicio corre en el hilo principal y lo dispara el ciclo de
        // vida real de las llamadas (RNF-02 exige responder rápido, sin hilos
        // extra). Cualquier excepción no capturada aquí (Room, ContentResolver,
        // permisos revocados a medias, etc.) tumba el proceso completo justo
        // cuando entra o termina una llamada. Ante cualquier error, se prefiere
        // degradar a "dejar pasar la llamada" antes que crashear la app.
        try {
            onScreenCallInterno(callDetails)
        } catch (e: Exception) {
            Log.e(TAG, "Error evaluando la llamada, se deja pasar sin bloquear", e)
            respondToCall(callDetails, CallResponse.Builder().build())
        }
    }

    private fun onScreenCallInterno(callDetails: Call.Details) {
        val numero = callDetails.handle?.schemeSpecificPart

        if (numero.isNullOrBlank()) {
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }

        // El toggle "Filtro de Llamadas" de Registro: si está apagado, no se
        // evalúa ni se registra nada, se deja pasar todo tal cual.
        if (!PreferenciasFiltro.filtroLlamadasActivo(applicationContext)) {
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }

        // La lista negra manual (pantalla Lista Negra) es una orden explícita y
        // deliberada del usuario: gana incluso si el número está guardado como
        // contacto (si lo agregaste a mano, es porque quieres bloquearlo sí o sí).
        if (db.numeroBloqueadoDao().estaBloqueadoNormalizado(numero)) {
            registrarEnHistorial(
                numero, bloqueada = true, probabilidad = 1.0,
                categoria = CategoriaLlamada.BLOQUEO_MANUAL,
                motivo = "Número en la lista negra",
                prefijoPais = DetectorPrefijo.extraerPrefijo(numero)
            )
            respondToCall(callDetails, respuestaBloqueo())
            return
        }

        // Los contactos guardados siempre pasan, pero sí quedan en el historial
        // (RF-06) para que el usuario vea el panorama completo.
        if (ContactosUtil.estaEnContactos(this, numero)) {
            registrarEnHistorial(numero, bloqueada = false, probabilidad = 0.0, categoria = CategoriaLlamada.CONTACTO)
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }

        // Lista blanca personal (RF-05: el usuario marcó una llamada de este
        // número como "no era spam"): pasa siempre, aunque el modelo o el
        // horario digan lo contrario (la lista negra ya se resolvió arriba).
        val feedback = db.feedbackUsuarioDao().obtenerNormalizado(numero)
        if (feedback != null && !feedback.marcadoSpam) {
            registrarEnHistorial(
                numero, bloqueada = false, probabilidad = 0.0,
                categoria = CategoriaLlamada.PROBABLE_REAL,
                motivo = "Marcado por el usuario como no-spam",
                prefijoPais = DetectorPrefijo.extraerPrefijo(numero)
            )
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }

        val ahora = Calendar.getInstance()
        val horaActual = ahora.get(Calendar.HOUR_OF_DAY)
        val minutoDelDiaActual = horaActual * 60 + ahora.get(Calendar.MINUTE)

        // Franja horaria programada (pantalla Horario): bloquea todo lo que no
        // sea contacto, sin pasar por el clasificador.
        if (estaEnFranjaBloqueo(minutoDelDiaActual)) {
            registrarEnHistorial(
                numero, bloqueada = true, probabilidad = 1.0,
                categoria = CategoriaLlamada.BLOQUEO_MANUAL,
                motivo = "Franja horaria de bloqueo programada",
                prefijoPais = DetectorPrefijo.extraerPrefijo(numero)
            )
            respondToCall(callDetails, respuestaBloqueo())
            return
        }

        val caracteristicas = CaracteristicasLlamada(
            enContactos = false,
            prefijoRiesgo = DetectorPrefijo.esPrefijoRiesgoso(numero, PrefijosFrecuentesCache.obtener(applicationContext)),
            frecuenciaLlamadasNumero = contarLlamadasRecientes(numero),
            horaDelDia = horaActual,
            esNumeroCortoOExtrano = DetectorFormatoNumero.esNumeroCortoOExtrano(numero),
            // Alimentado por el feedback loop (RF-05): ver FeedbackLoopObserver.
            reportesPrevios = db.reporteLocalDao().obtenerCantidadNormalizado(numero)
        )

        val resultado = ClasificadorSpam.evaluar(caracteristicas)
        val esSpam = resultado.probabilidad > ClasificadorSpam.UMBRAL_BLOQUEO

        Log.d(TAG, "Número=$numero prob=${resultado.probabilidad} bloqueado=$esSpam ($caracteristicas)")

        registrarEnHistorial(
            numero, esSpam, resultado.probabilidad,
            categoria = if (esSpam) CategoriaLlamada.SPAM_MODELO else CategoriaLlamada.PROBABLE_REAL,
            motivo = resultado.motivo(),
            prefijoPais = DetectorPrefijo.extraerPrefijo(numero)
        )
        respondToCall(callDetails, if (esSpam) respuestaBloqueo() else CallResponse.Builder().build())
    }

    private fun respuestaBloqueo(): CallResponse =
        CallResponse.Builder()
            .setDisallowCall(true)
            .setRejectCall(true)
            .setSkipNotification(true)
            .setSkipCallLog(false) // se conserva para el historial nativo también
            .build()

    private fun estaEnFranjaBloqueo(minutoDelDiaActual: Int): Boolean =
        db.horarioBloqueoDao().obtenerActivos().any { franja ->
            if (franja.minutoInicio <= franja.minutoFin) {
                minutoDelDiaActual in franja.minutoInicio..franja.minutoFin
            } else {
                minutoDelDiaActual >= franja.minutoInicio || minutoDelDiaActual <= franja.minutoFin
            }
        }

    private fun registrarEnHistorial(
        numero: String,
        bloqueada: Boolean,
        probabilidad: Double,
        categoria: CategoriaLlamada,
        motivo: String? = null,
        prefijoPais: String? = null
    ) {
        db.registroLlamadaDao().insertar(
            RegistroLlamadaEntity(
                numero = numero,
                fechaHora = System.currentTimeMillis(),
                bloqueada = bloqueada,
                probabilidadSpam = probabilidad,
                categoria = categoria.name,
                motivo = motivo,
                prefijoPais = prefijoPais
            )
        )
        db.registroLlamadaDao().podarAntiguos()
    }

    // Sin permisos otorgados (ver PermisosYRolCard) estas consultas lanzan
    // SecurityException; el servicio no debe crashear por eso, solo degradar
    // a los valores más conservadores (no bloquear).
    private fun contarLlamadasRecientes(numero: String): Int = try {
        val desde = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(DIAS_VENTANA_FRECUENCIA)
        val seleccion = "${CallLog.Calls.NUMBER} = ? AND ${CallLog.Calls.DATE} >= ?"
        contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(CallLog.Calls._ID),
            seleccion,
            arrayOf(numero, desde.toString()),
            null
        )?.use { cursor -> cursor.count } ?: 0
    } catch (e: SecurityException) {
        Log.w(TAG, "Sin permiso READ_CALL_LOG, se asume frecuencia 0", e)
        0
    }

}
