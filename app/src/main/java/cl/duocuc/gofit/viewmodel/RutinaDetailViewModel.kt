package cl.duocuc.gofit.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.gofit.data.model.Ejercicio

import cl.duocuc.gofit.model.Rutina
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RutinaDetailViewModel : ViewModel() {
    private val _rutina = MutableStateFlow<Rutina?>(null)
    val rutina = _rutina.asStateFlow()

    // Este es un repositorio falso para simular la obtención de datos
    private val fakeRepository = FakeRutinaRepository()

    fun cargarRutina(rutinaId: String) {
        // En una app real, llamarías al repositorio para obtener los datos de la DB
        _rutina.value = fakeRepository.getRutinaById(rutinaId)
    }
}

// --- Repositorio Falso para Simulación ---
// Esto lo creamos para tener datos de prueba sin configurar una base de datos aún.
class FakeRutinaRepository {
    private val rutinasDePrueba = listOf(
        Rutina(
            id = "1", nombre = "Día de Pecho y Tríceps", descripcion = "Enfoque en fuerza",
            ejercicios = listOf(
                Ejercicio(id = "e1", nombre = "Press de Banca", series = "4", repeticiones = "6-8"),
                Ejercicio(id = "e2", nombre = "Press Inclinado con Mancuernas", series = "3", repeticiones = "8-12"),
                Ejercicio(id = "e3", nombre = "Fondos para Tríceps", series = "3", repeticiones = "Al fallo")
            )
        ),
        Rutina(
            id = "2", nombre = "Pierna (Día Pesado)", descripcion = "Sentadillas, peso muerto y más",
            ejercicios = listOf(
                Ejercicio(id = "e4", nombre = "Sentadillas", series = "5", repeticiones = "5"),
                Ejercicio(id = "e5", nombre = "Peso Muerto Rumano", series = "3", repeticiones = "8-10"),
                Ejercicio(id = "e6", nombre = "Prensa de Piernas", series = "4", repeticiones = "10-12")
            )
        ),
        Rutina(id = "3", nombre = "Espalda y Bíceps", descripcion = "Rutina de hipertrofia") // Sin ejercicios para probar
    )

    fun getRutinas(): List<Rutina> = rutinasDePrueba
    fun getRutinaById(id: String): Rutina? = rutinasDePrueba.find { it.id == id }
}


