package cl.duocuc.gofit.repository


import cl.duocuc.gofit.model.HistorialEntrenamiento
import kotlinx.coroutines.flow.Flow


class ProgresoRepository(
) {
    private val listaHistorialMutable = mutableListOf<HistorialEntrenamiento>()


     fun guardarHistorialEntrenamiento(historial: HistorialEntrenamiento) {

        println("Guardando en el repositorio: ${historial.nombreRutina}")
        listaHistorialMutable.add(0, historial)
    }


    fun obtenerTodoElHistorial(): Flow<List<HistorialEntrenamiento>> {

        return kotlinx.coroutines.flow.flow {
            emit(listaHistorialMutable)
        }
    }



}
