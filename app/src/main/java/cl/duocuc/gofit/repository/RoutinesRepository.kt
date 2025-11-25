package cl.duocuc.gofit.repository

import cl.duocuc.gofit.data.local.LocalRoutineDataSource
import cl.duocuc.gofit.data.remote.RoutineApiService
import cl.duocuc.gofit.data.remote.RoutineDto
import cl.duocuc.gofit.model.Rutina
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoutinesRepository(
    private val apiService: RoutineApiService,
    private val localRoutineDataSource: LocalRoutineDataSource = LocalRoutineDataSource()
) {

    @Volatile
    private var cachedRoutines: List<Rutina> = emptyList()

    suspend fun getRoutines(forceRefresh: Boolean = false): List<Rutina> = withContext(Dispatchers.IO) {
        if (cachedRoutines.isNotEmpty() && !forceRefresh) {
            return@withContext cachedRoutines
        }

        val remoteResult = runCatching { apiService.getRoutines() }
        val remoteRoutines = remoteResult
            .getOrElse { emptyList() }
            .map { it.toDomain() }

        val localRoutines = localRoutineDataSource.getLocalRoutines()

        cachedRoutines = (remoteRoutines + localRoutines)
            .distinctBy { it.id }

        remoteResult.exceptionOrNull()?.let { throw it }

        cachedRoutines
    }

    suspend fun getRoutineById(id: String): Rutina? = withContext(Dispatchers.IO) {
        val routines = if (cachedRoutines.isEmpty()) {
            getRoutines(forceRefresh = false)
        } else {
            cachedRoutines
        }

        routines.find { it.id == id }
    }

    private fun RoutineDto.toDomain(): Rutina {
        return Rutina(
            id = id.toString(),
            nombre = name,
            descripcion = description ?: "",
            ejercicios = emptyList(),
            dificultad = difficulty,
            tipoEntrenamiento = trainingType,
            publicationDate = publicationDate
        )
    }
}
