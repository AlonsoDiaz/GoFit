package cl.duocuc.gofit.model



import cl.duocuc.gofit.data.model.EjercicioLog
import java.util.Date

data class HistorialEntrenamiento(
    val fecha: Date,
    val nombreRutina: String,
    val ejercicios: List<EjercicioLog>
)

