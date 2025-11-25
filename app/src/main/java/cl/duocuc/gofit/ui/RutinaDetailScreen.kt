package cl.duocuc.gofit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duocuc.gofit.data.model.Ejercicio
import cl.duocuc.gofit.viewmodel.RutinaDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaDetailScreen(
    navController: NavController,
    rutinaId: String?,
    viewModel: RutinaDetailViewModel
) {

    LaunchedEffect(rutinaId) {
        if (rutinaId != null) {
            viewModel.cargarRutina(rutinaId)
        } else {
            viewModel.marcarError("Identificador de rutina inválido")
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val currentRutina = uiState.rutina


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = when {
                        currentRutina != null -> currentRutina.nombre
                        uiState.isLoading -> "Cargando..."
                        else -> "Rutina"
                    }
                    Text(title)
                },
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


        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            currentRutina != null -> {
                val rutina = currentRutina
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()

                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                // 1. Cabecera con el nombre y descripción
                item {

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(rutina.nombre, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    if (rutina.descripcion.isNotBlank()) {
                        Text(
                            rutina.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Text(
                        text = "Revisa la información clave de esta rutina y confirma los ejercicios sugeridos antes de comenzar.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    rutina.dificultad?.takeIf { it.isNotBlank() }?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Dificultad: $it", style = MaterialTheme.typography.bodyMedium)
                    }
                    rutina.tipoEntrenamiento?.takeIf { it.isNotBlank() }?.let {
                        Text("Tipo de entrenamiento: $it", style = MaterialTheme.typography.bodyMedium)
                    }
                    rutina.publicationDate?.takeIf { it.isNotBlank() }?.let {
                        Text("Publicado el: $it", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Ejercicios", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // 2. Lista de ejercicios
                if (rutina.ejercicios.isEmpty()) {
                    item {
                        Text("Esta rutina no tiene ejercicios asignados.")
                    }
                } else {
                    items(rutina.ejercicios, key = { it.id }) { ejercicio ->
                        EjercicioDetailCard(ejercicio = ejercicio)
                    }
                }
            }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(uiState.errorMessage ?: "No fue posible cargar la rutina")
                }
            }
        }
    }
}

@Composable
fun EjercicioDetailCard(ejercicio: Ejercicio) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(ejercicio.nombre, modifier = Modifier.weight(1f), fontSize = 18.sp)
            Text("${ejercicio.series} x ${ejercicio.repeticiones}", fontWeight = FontWeight.SemiBold)
        }
    }
}
