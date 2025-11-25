package cl.duocuc.gofit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons // <-- 1. IMPORTAR ICONOS
import androidx.compose.material.icons.automirrored.filled.ArrowBack // <-- 1. IMPORTAR FLECHA
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duocuc.gofit.model.HistorialEntrenamiento
import cl.duocuc.gofit.viewmodel.ProgresoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgresoScreen(
    navController: NavController,
    progresoViewModel: ProgresoViewModel
) {
    val historial by progresoViewModel.historialState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Progreso") },

                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }

            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "Historial de Entrenamientos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "Consulta los entrenamientos completados y recuerda cómo avanzaste en cada sesión registrada.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (historial.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Aún no has completado ningún entrenamiento. ¡Termina uno para verlo aquí!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(historial) { entrenamiento ->
                        HistorialCard(entrenamiento = entrenamiento)
                    }
                }
            }
        }
    }
}

@Composable
fun HistorialCard(entrenamiento: HistorialEntrenamiento) {
    val dateFormatter = SimpleDateFormat("dd 'de' MMMM, yyyy 'a las' HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(entrenamiento.nombreRutina, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Text(
                "Fecha: ${dateFormatter.format(entrenamiento.fecha)}",
                style = MaterialTheme.typography.bodySmall
            )

        }
    }
}
