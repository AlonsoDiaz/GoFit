package cl.duocuc.gofit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duocuc.gofit.data.model.EjercicioLog
import cl.duocuc.gofit.data.model.Serie
import cl.duocuc.gofit.viewmodel.WorkoutViewModel

@Composable
fun WorkoutSessionScreen(
    navController: NavController,
    // Inyectamos el ViewModel
    workoutViewModel: WorkoutViewModel = viewModel()
) {
    // Recolectamos el estado de los ejercicios desde el ViewModel
    val ejercicios by workoutViewModel.ejercicios.collectAsState()
    var showTimerDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Sesión: Rutina de Pecho", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        items(ejercicios, key = { it.nombre }) { ejercicio ->
            EjercicioCard(
                ejercicioLog = ejercicio,
                onAddSerie = { workoutViewModel.agregarSerie(ejercicio.nombre) },
                onPesoChange = { serie, peso -> workoutViewModel.actualizarPeso(ejercicio.nombre, serie.numero, peso) },
                onRepsChange = { serie, reps -> workoutViewModel.actualizarRepeticiones(ejercicio.nombre, serie.numero, reps) },
                onStartTimer = { showTimerDialog = true }
            )
        }

        item {
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Finalizar Entrenamiento")
            }
        }
    }

    if (showTimerDialog) {
        // ... (el código del TimerDialog se mantiene igual)
    }
}

@Composable
fun EjercicioCard(
    ejercicioLog: EjercicioLog,
    onAddSerie: () -> Unit,
    onPesoChange: (Serie, String) -> Unit,
    onRepsChange: (Serie, String) -> Unit,
    onStartTimer: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(ejercicioLog.nombre, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

            // Cabeceras de la tabla
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Serie", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Peso (kg)", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text("Reps", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
            }

            // Filas de series dinámicas
            ejercicioLog.series.forEach { serie ->
                SerieRow(
                    serie = serie,
                    onPesoChange = { peso -> onPesoChange(serie, peso) },
                    onRepsChange = { reps -> onRepsChange(serie, reps) }
                )
            }

            // Botones de acción
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onAddSerie) { Text("Añadir Serie") }
                Button(onClick = onStartTimer) { Text("Descanso") }
            }
        }
    }
}

@Composable
fun SerieRow(
    serie: Serie,
    onPesoChange: (String) -> Unit,
    onRepsChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(serie.numero.toString(), modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = serie.peso,
            onValueChange = onPesoChange,
            modifier = Modifier.weight(2f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = serie.repeticiones,
            onValueChange = onRepsChange,
            modifier = Modifier.weight(2f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
