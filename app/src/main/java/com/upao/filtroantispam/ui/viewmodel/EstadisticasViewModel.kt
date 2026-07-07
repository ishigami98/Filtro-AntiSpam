package com.upao.filtroantispam.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.upao.filtroantispam.data.FiltroDatabase
import com.upao.filtroantispam.logic.Estadisticas
import com.upao.filtroantispam.logic.EstadisticasCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class EstadisticasViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = FiltroDatabase.obtenerInstancia(application).registroLlamadaDao()

    val estadisticas: StateFlow<Estadisticas> = dao.obtenerTodos()
        .map { EstadisticasCalculator.calcular(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            EstadisticasCalculator.calcular(emptyList())
        )
}
