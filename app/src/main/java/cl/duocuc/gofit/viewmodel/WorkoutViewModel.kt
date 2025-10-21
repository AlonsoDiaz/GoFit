package cl.duocuc.gofit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duocuc.gofit.data.model.EjercicioLog
import cl.duocuc.gofit.data.model.Serie
import cl.duocuc.gofit.model.HistorialEntrenamiento
import cl.duocuc.gofit.repository.ProgresoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import android.content.Context
import cl.duocuc.gofit.util.NotificationHelper


class WorkoutViewModel(
    private val progresoRepository: ProgresoRepository
) : ViewModel() {

    private val _ejercicios = MutableStateFlow<List<EjercicioLog>>(emptyList())
    val ejercicios: StateFlow<List<EjercicioLog>> = _ejercicios.asStateFlow()


    private var rutinaId: String? = null
    private var rutinaNombre: String? = null


    fun cargarEjerciciosDeRutina(id: String) {
        rutinaId = id

        when (id) {
            "rutina_del_dia" -> {
                rutinaNombre = "Entrenamiento de Pecho"
                _ejercicios.value = listOf(
                    EjercicioLog("Press de Banca"),
                    EjercicioLog("Press Inclinado"),
                    EjercicioLog("Aperturas con Mancuerna")
                )
            }
            // Agrega más casos para otras rutinas
            else -> {
                rutinaNombre = "Rutina General"
                _ejercicios.value = listOf(
                    EjercicioLog("Sentadillas"),
                    EjercicioLog("Flexiones")
                )
            }
        }
    }

    fun agregarSerie(nombreEjercicio: String) {
        _ejercicios.update { listaActual ->
            listaActual.map { ejercicioLog ->
                if (ejercicioLog.nombre == nombreEjercicio) {
                    val nuevaSerie = Serie(numero = ejercicioLog.series.size + 1)
                    ejercicioLog.copy(series = (ejercicioLog.series + nuevaSerie).toMutableList())
                } else {
                    ejercicioLog
                }
            }
        }
    }

    fun actualizarPeso(nombreEjercicio: String, numeroSerie: Int, peso: String) {
        _ejercicios.update { listaActual ->
            listaActual.map { ejercicioLog ->
                if (ejercicioLog.nombre == nombreEjercicio) {
                    val seriesActualizadas = ejercicioLog.series.map {
                        if (it.numero == numeroSerie) it.copy(peso = peso) else it
                    }
                    ejercicioLog.copy(series = seriesActualizadas.toMutableList())
                } else {
                    ejercicioLog
                }
            }
        }
    }

    fun actualizarRepeticiones(nombreEjercicio: String, numeroSerie: Int, reps: String) {
        _ejercicios.update { listaActual ->
            listaActual.map { ejercicioLog ->
                if (ejercicioLog.nombre == nombreEjercicio) {
                    val seriesActualizadas = ejercicioLog.series.map {
                        if (it.numero == numeroSerie) it.copy(repeticiones = reps) else it
                    }
                    ejercicioLog.copy(series = seriesActualizadas.toMutableList())
                } else {
                    ejercicioLog
                }
            }
        }
    }


    fun finalizarYGuardarEntrenamiento(context: Context) {
        viewModelScope.launch {
            val ejerciciosCompletados = _ejercicios.value.filter { it.series.isNotEmpty() }

            if (ejerciciosCompletados.isNotEmpty()) {
                val nombreRutinaGuardada = rutinaNombre ?: "Entrenamiento"
                val historial = HistorialEntrenamiento(
                    fecha = java.util.Date(System.currentTimeMillis()),
                    nombreRutina = nombreRutinaGuardada,
                    ejercicios = ejerciciosCompletados
                )

                progresoRepository.guardarHistorialEntrenamiento(historial)


                val notificationHelper = NotificationHelper(context)

                notificationHelper.showWorkoutCompletedNotification(nombreRutinaGuardada)
                // -----------------------------

                limpiarEstado()
            }
        }
    }


    private fun limpiarEstado() {
        _ejercicios.value = emptyList()
        rutinaId = null
        rutinaNombre = null
    }
}
