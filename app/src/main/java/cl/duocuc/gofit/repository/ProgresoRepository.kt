package cl.duocuc.gofit.repository

import cl.duocuc.gofit.data.local.WorkoutExerciseEntity
import cl.duocuc.gofit.data.local.WorkoutExerciseWithSeries
import cl.duocuc.gofit.data.local.WorkoutHistoryDao
import cl.duocuc.gofit.data.local.WorkoutHistoryEntity
import cl.duocuc.gofit.data.local.WorkoutHistoryWithExercises
import cl.duocuc.gofit.data.local.WorkoutSerieEntity
import cl.duocuc.gofit.data.model.EjercicioLog
import cl.duocuc.gofit.data.model.Serie
import cl.duocuc.gofit.model.HistorialEntrenamiento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class ProgresoRepository(
    private val workoutHistoryDao: WorkoutHistoryDao
) {

    suspend fun guardarHistorialEntrenamiento(historial: HistorialEntrenamiento) {
        val historyId = workoutHistoryDao.insertHistory(
            WorkoutHistoryEntity(
                fechaMillis = historial.fecha.time,
                nombreRutina = historial.nombreRutina
            )
        )

        val exerciseEntities = historial.ejercicios.map { ejercicio ->
            WorkoutExerciseEntity(
                historyId = historyId,
                nombre = ejercicio.nombre
            )
        }

        val exerciseIds = if (exerciseEntities.isNotEmpty()) {
            workoutHistoryDao.insertExercises(exerciseEntities)
        } else {
            emptyList()
        }

        if (exerciseIds.isNotEmpty()) {
            val seriesEntities = exerciseIds.flatMapIndexed { index, exerciseId ->
                val ejercicioLog = historial.ejercicios[index]
                ejercicioLog.series.map { serie ->
                    WorkoutSerieEntity(
                        exerciseId = exerciseId,
                        numero = serie.numero,
                        peso = serie.peso,
                        repeticiones = serie.repeticiones
                    )
                }
            }

            if (seriesEntities.isNotEmpty()) {
                workoutHistoryDao.insertSeries(seriesEntities)
            }
        }
    }

    fun obtenerTodoElHistorial(): Flow<List<HistorialEntrenamiento>> {
        return workoutHistoryDao.observeWorkoutHistory().map { histories ->
            histories.map { it.toDomain() }
        }
    }

    private fun WorkoutHistoryWithExercises.toDomain(): HistorialEntrenamiento {
        return HistorialEntrenamiento(
            fecha = Date(history.fechaMillis),
            nombreRutina = history.nombreRutina,
            ejercicios = exercises.map { it.toDomain() }
        )
    }

    private fun WorkoutExerciseWithSeries.toDomain(): EjercicioLog {
        return EjercicioLog(
            nombre = exercise.nombre,
            series = series.map { serieEntity ->
                Serie(
                    numero = serieEntity.numero,
                    peso = serieEntity.peso,
                    repeticiones = serieEntity.repeticiones
                )
            }.toMutableList()
        )
    }
}
