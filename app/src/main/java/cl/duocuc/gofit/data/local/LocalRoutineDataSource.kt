package cl.duocuc.gofit.data.local

import cl.duocuc.gofit.data.model.Ejercicio
import cl.duocuc.gofit.model.Rutina

class LocalRoutineDataSource {
    fun getLocalRoutines(): List<Rutina> = listOf(
        Rutina(
            id = "local-1",
            nombre = "Día de Pecho y Tríceps",
            descripcion = "Enfoque en fuerza",
            ejercicios = listOf(
                Ejercicio(id = "e1", nombre = "Press de Banca", series = "4", repeticiones = "6-8"),
                Ejercicio(id = "e2", nombre = "Press Inclinado con Mancuernas", series = "3", repeticiones = "8-12"),
                Ejercicio(id = "e3", nombre = "Fondos para Tríceps", series = "3", repeticiones = "Al fallo")
            )
        ),
        Rutina(
            id = "local-2",
            nombre = "Pierna (Día Pesado)",
            descripcion = "Sentadillas, peso muerto y más",
            ejercicios = listOf(
                Ejercicio(id = "e4", nombre = "Sentadillas", series = "5", repeticiones = "5"),
                Ejercicio(id = "e5", nombre = "Peso Muerto Rumano", series = "3", repeticiones = "8-10"),
                Ejercicio(id = "e6", nombre = "Prensa de Piernas", series = "4", repeticiones = "10-12")
            )
        ),
        Rutina(
            id = "local-3",
            nombre = "Espalda y Bíceps",
            descripcion = "Rutina de hipertrofia"
        )
    )
}
