package com.upao.filtroantispam.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.upao.filtroantispam.data.FiltroDatabase
import com.upao.filtroantispam.data.NumeroBloqueadoEntity
import com.upao.filtroantispam.data.incrementarNormalizado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListaNegraViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FiltroDatabase.obtenerInstancia(application)
    private val dao = db.numeroBloqueadoDao()

    val numerosBloqueados: StateFlow<List<NumeroBloqueadoEntity>> = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Bloqueo total: el número nunca vuelve a sonar, sin pasar por el clasificador. */
    fun agregar(numero: String) {
        val numeroLimpio = numero.trim()
        if (numeroLimpio.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            dao.agregar(NumeroBloqueadoEntity(numero = numeroLimpio))
        }
    }

    /**
     * Reporte manual (ej. un familiar avisa de un número sospechoso que
     * todavía no ha llamado): solo suma a reportesPrevios, no bloquea directo.
     */
    fun reportarSpam(numero: String) {
        val numeroLimpio = numero.trim()
        if (numeroLimpio.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            db.reporteLocalDao().incrementarNormalizado(numeroLimpio)
        }
    }

    fun eliminar(item: NumeroBloqueadoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.eliminar(item)
        }
    }
}
