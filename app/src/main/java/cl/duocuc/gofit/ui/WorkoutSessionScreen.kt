package cl.duocuc.gofit.ui

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duocuc.gofit.data.model.EjercicioLog
import cl.duocuc.gofit.data.model.Serie
import cl.duocuc.gofit.viewmodel.TimerViewModel
import cl.duocuc.gofit.viewmodel.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    rutinaId: String,
    workoutViewModel: WorkoutViewModel,
    onFinishWorkout: () -> Unit,
    // --- 1. SE AÑADE NAVCONTROLLER PARA LA NAVEGACIÓN ---
    navController: NavController
) {
    RequestNotificationPermission()

    val ejercicios by workoutViewModel.ejercicios.collectAsState()
    var showTimerDialog by remember { mutableStateOf(false) }
    // --- 2. ESTADO PARA CONTROLAR EL DIÁLOGO DE CONFIRMACIÓN ---
    var showExitConfirmationDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Carga los ejercicios cuando la pantalla se muestra por primera vez
    LaunchedEffect(rutinaId) {
        workoutViewModel.cargarEjerciciosDeRutina(rutinaId)
    }

    // --- 3. LÓGICA PARA INTERCEPTAR EL BOTÓN "ATRÁS" DEL SISTEMA ---
    BackHandler {
        showExitConfirmationDialog = true
    }

    // --- 4. SE ENVUELVE TODO EN UN SCAFFOLD ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sesión de Entrenamiento") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Muestra el diálogo en lugar de navegar directamente
                        showExitConfirmationDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplicar padding del Scaffold
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // El texto del título ahora está en la TopAppBar, así que este item se puede eliminar si se desea.
            // item {
            //    Text("Sesión de Entrenamiento", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            // }

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
                Button(
                    onClick = {
                        workoutViewModel.finalizarYGuardarEntrenamiento(context)
                        onFinishWorkout()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar Entrenamiento")
                }
            }
        }
    }

    if (showTimerDialog) {
        TimerDialog(onDismiss = { showTimerDialog = false })
    }

    // --- 5. MOSTRAR EL DIÁLOGO DE CONFIRMACIÓN SI ES NECESARIO ---
    if (showExitConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showExitConfirmationDialog = false },
            title = { Text("¿Salir del entrenamiento?") },
            text = { Text("Si sales ahora, perderás todo el progreso no guardado de esta sesión.") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitConfirmationDialog = false
                        navController.navigateUp() // Salir de verdad
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                Button(onClick = { showExitConfirmationDialog = false }) {
                    Text("Cancelar")
                }
            }
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

                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(animationSpec = tween(300)),
                    exit = shrinkVertically(animationSpec = tween(300))
                ) {
                    SerieRow(
                        serie = serie,
                        onPesoChange = { peso -> onPesoChange(serie, peso) },
                        onRepsChange = { reps -> onRepsChange(serie, reps) }
                    )
                }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(serie.numero.toString(), modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = serie.peso,
            onValueChange = onPesoChange,
            modifier = Modifier.weight(2f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true // Para mejor UI
        )
        OutlinedTextField(
            value = serie.repeticiones,
            onValueChange = onRepsChange,
            modifier = Modifier.weight(2f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true // Para mejor UI
        )
    }
}

@Composable
fun TimerDialog(
    timerViewModel: TimerViewModel = viewModel(),
    onDismiss: () -> Unit
) {
    val tiempo by timerViewModel.tiempoRestante.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        timerViewModel.iniciarTimer(segundos = 150) { // Duración del temporizador
            // Vibración al finalizar
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }

            // Sonido de notificación al finalizar
            try {
                val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(context, notification)
                r.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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
                Text("Omitir y Cerrar")
            }
        }
    )
}

@Composable
fun RequestNotificationPermission() {
    // Solo necesitamos pedir permiso en Android 13 (API 33) o superior
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {

                } else {

                }
            }
        )


        LaunchedEffect(Unit) {
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
