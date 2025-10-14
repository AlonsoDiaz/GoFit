package cl.duocuc.gofit.data.model

data class Serie(
    val numero: Int,
    var peso: String = "",
    var repeticiones: String = ""
)

data class EjercicioLog(
    val nombre: String,
    val series: MutableList<Serie> = mutableListOf(Serie(numero = 1)) // Cada ejercicio empieza con una serie vacía
)


