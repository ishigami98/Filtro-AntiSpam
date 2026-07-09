package com.upao.filtroantispam.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.upao.filtroantispam.data.CategoriaLlamada
import com.upao.filtroantispam.data.FeedbackUsuarioEntity
import com.upao.filtroantispam.data.FiltroDatabase
import com.upao.filtroantispam.data.RegistroLlamadaEntity
import com.upao.filtroantispam.data.incrementarNormalizado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "RegistroViewModel"

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FiltroDatabase.obtenerInstancia(application)
    private val dao = db.registroLlamadaDao()

    val registros: StateFlow<List<RegistroLlamadaEntity>> = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Última corrección del usuario por número, para reflejarla en el diálogo de detalle. */
    val feedbackPorNumero: StateFlow<Map<String, FeedbackUsuarioEntity>> = db.feedbackUsuarioDao()
        .obtenerTodos()
        .combine(registros) { feedback, _ -> feedback.associateBy { it.numero } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    /**
     * La corrección se guarda para el futuro (feedback_usuario + reportesPrevios),
     * pero también hay que actualizar ESTA fila ya mostrada: si no, el historial y
     * las estadísticas nunca reflejan el cambio porque solo leen registro_llamadas.
     */
    fun marcarSpam(registro: RegistroLlamadaEntity) {
        Log.d(TAG, "marcarSpam(${registro.numero})")
        viewModelScope.launch(Dispatchers.IO) {
            db.feedbackUsuarioDao().marcar(FeedbackUsuarioEntity(registro.numero, marcadoSpam = true))
            db.reporteLocalDao().incrementarNormalizado(registro.numero)
            dao.actualizar(
                registro.copy(
                    categoria = CategoriaLlamada.SPAM_MODELO.name,
                    bloqueada = true,
                    motivo = "Marcado por el usuario como spam"
                )
            )
        }
    }

    fun marcarNoSpam(registro: RegistroLlamadaEntity) {
        Log.d(TAG, "marcarNoSpam(${registro.numero})")
        viewModelScope.launch(Dispatchers.IO) {
            db.feedbackUsuarioDao().marcar(FeedbackUsuarioEntity(registro.numero, marcadoSpam = false))
            dao.actualizar(
                registro.copy(
                    categoria = CategoriaLlamada.PROBABLE_REAL.name,
                    bloqueada = false,
                    motivo = "Marcado por el usuario como no-spam"
                )
            )
        }
    }

    fun eliminarRegistro(registro: RegistroLlamadaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.eliminar(registro)
        }
    }
}
