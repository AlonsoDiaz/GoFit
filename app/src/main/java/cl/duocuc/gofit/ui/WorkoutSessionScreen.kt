package cl.duocuc.gofit.ui

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duocuc.gofit.data.model.EjercicioLog
import cl.duocuc.gofit.data.model.Serie
import cl.duocuc.gofit.viewmodel.TimerViewModel
import cl.duocuc.gofit.viewmodel.WorkoutViewModel

@Composable
fun WorkoutSessionScreen(
    navController: NavController,

    workoutViewModel: WorkoutViewModel = viewModel()
) {

    val ejercicios by workoutViewModel.ejercicios.collectAsState()
    var showTimerDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
        TimerDialog(
            onDismiss = { showTimerDialog = false }
        )
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


            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Serie", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Peso (kg)", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text("Reps", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
            }


            ejercicioLog.series.forEach { serie ->
                SerieRow(
                    serie = serie,
                    onPesoChange = { peso -> onPesoChange(serie, peso) },
                    onRepsChange = { reps -> onRepsChange(serie, reps) }
                )
            }


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

// ... (código anterior)

@Composable
fun TimerDialog(
    timerViewModel: TimerViewModel = viewModel(),
    onDismiss: () -> Unit
) {
    val tiempo by timerViewModel.tiempoRestante.collectAsState()
    val estaCorriendo by timerViewModel.estaCorriendo.collectAsState()
    val context = LocalContext.current // Obtenemos el contexto para acceder a servicios del sistema

    LaunchedEffect(Unit) {
        timerViewModel.iniciarTimer(segundos = 150) { // Usamos 5 segundos para probar rápido
            //Vibración
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {

                vibrator.vibrate(500)
            }

            //Sonido
            try {
                val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(context, notification)
                r.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ... (el resto del AlertDialog se mantiene igual)
    AlertDialog(
        onDismissRequest = {
            timerViewModel.detenerTimer()
            onDismiss()
        },
        title = { Text("Tiempo de Descanso") },
        text = {
            Text(
                text = tiempo,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    timerViewModel.detenerTimer()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Detener y Cerrar")
            }
        }
    )
}
