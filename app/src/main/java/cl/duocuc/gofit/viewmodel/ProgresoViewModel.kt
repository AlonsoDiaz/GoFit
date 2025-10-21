package cl.duocuc.gofit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import cl.duocuc.gofit.model.HistorialEntrenamiento
import cl.duocuc.gofit.repository.ProgresoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


class ProgresoViewModel(

    private val progresoRepository: ProgresoRepository
) : ViewModel() {


    val historialState: StateFlow<List<HistorialEntrenamiento>> =
        progresoRepository.obtenerTodoElHistorial()
            .stateIn(
                scope = viewModelScope,

                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
}
