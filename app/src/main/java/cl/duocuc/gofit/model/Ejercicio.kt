package cl.duocuc.gofit.data.model

data class Ejercicio(
    val id: String,
    val nombre: String,
    val series: String,
    val repeticiones: String,
    val nota: String? = null
)
