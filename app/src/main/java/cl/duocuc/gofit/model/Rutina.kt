package cl.duocuc.gofit.model

import cl.duocuc.gofit.data.model.Ejercicio


data class Rutina(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val ejercicios: List<Ejercicio> = emptyList(),
    val dificultad: String? = null,
    val tipoEntrenamiento: String? = null,
    val publicationDate: String? = null
)
