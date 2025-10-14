package cl.duocuc.gofit.viewmodel



import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cl.duocuc.gofit.data.model.EjercicioLog
import cl.duocuc.gofit.data.model.Serie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WorkoutViewModel : ViewModel() {

    private val _ejercicios = MutableStateFlow<List<EjercicioLog>>(emptyList())
    val ejercicios = _ejercicios.asStateFlow()

    init {
        // Simula la carga de ejercicios de una rutina
        cargarEjercicios()
    }

    private fun cargarEjercicios() {
        _ejercicios.value = listOf(
            EjercicioLog("Press de Banca"),
            EjercicioLog("Press Inclinado"),
            EjercicioLog("Aperturas con Mancuerna")
        )
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
}
