package com.upao.filtroantispam.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.upao.filtroantispam.data.FiltroDatabase
import com.upao.filtroantispam.data.HorarioBloqueoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HorarioViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = FiltroDatabase.obtenerInstancia(application).horarioBloqueoDao()

    val horarios: StateFlow<List<HorarioBloqueoEntity>> = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agregar(minutoInicio: Int, minutoFin: Int) {
        if (minutoInicio !in 0..1439 || minutoFin !in 0..1439) return
        viewModelScope.launch(Dispatchers.IO) {
            dao.agregar(HorarioBloqueoEntity(minutoInicio = minutoInicio, minutoFin = minutoFin))
        }
    }

    fun cambiarActivo(item: HorarioBloqueoEntity, activo: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.actualizar(item.copy(activo = activo))
        }
    }

    fun eliminar(item: HorarioBloqueoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.eliminar(item)
        }
    }
}
