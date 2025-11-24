package cl.duocuc.gofit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duocuc.gofit.model.Rutina
import cl.duocuc.gofit.viewmodel.RoutinesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(navController: NavController, viewModel: RoutinesViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Rutinas") },
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Explora las rutinas disponibles y abre cada una para revisar sus detalles y ejercicios sugeridos.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            when {
                uiState.isLoading && uiState.data.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.data.isNotEmpty() -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(uiState.data, key = { it.id }) { rutina ->
                            RutinaCard(
                                rutina = rutina,
                                onClick = {
                                    navController.navigate("rutina_detail/${rutina.id}")
                                }
                            )
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val message = uiState.errorMessage ?: "No hay rutinas disponibles en este momento"
                        Text(text = message, modifier = Modifier.padding(16.dp))
                    }
                }
            }

            uiState.errorMessage?.takeIf { uiState.data.isNotEmpty() }?.let { mensaje ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(mensaje, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaCard(rutina: Rutina, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(rutina.nombre, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            if (rutina.descripcion.isNotBlank()) {
                Text(rutina.descripcion, style = MaterialTheme.typography.bodyMedium)
            }
            // Los campos 'dificultad' y 'tipoEntrenamiento' pueden no estar en tu modelo `Rutina`
            // Si no existen, puedes eliminar las siguientes líneas sin problema.
            // Si existen, asegúrate de que el modelo los tiene definidos.
            rutina.dificultad?.takeIf { it.isNotBlank() }?.let {
                Text("Dificultad: $it", style = MaterialTheme.typography.bodySmall)
            }
            rutina.tipoEntrenamiento?.takeIf { it.isNotBlank() }?.let {
                Text("Tipo de entrenamiento: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
