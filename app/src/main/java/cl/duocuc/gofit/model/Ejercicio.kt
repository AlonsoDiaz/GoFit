package cl.duocuc.gofit.data.model

data class Ejercicio(
    val id: String,
    val nombre: String,
    val series: String, // Ej: "4"
    val repeticiones: String, // Ej: "8-12"
    val nota: String? = null // Opcional
)
