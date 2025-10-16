package cl.duocuc.gofit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duocuc.gofit.data.model.Ejercicio
import cl.duocuc.gofit.viewmodel.RutinaDetailViewModel

@Composable
fun RutinaDetailScreen(
    navController: NavController,
    rutinaId: String?,
    viewModel: RutinaDetailViewModel = viewModel()
) {
    // Carga los datos de la rutina la primera vez que la pantalla aparece
    LaunchedEffect(rutinaId) {
        if (rutinaId != null) {
            viewModel.cargarRutina(rutinaId)
        }
    }

    val rutina by viewModel.rutina.collectAsState()

    if (rutina == null) {
        // Muestra un indicador de carga mientras se obtienen los datos
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 1. Cabecera con el nombre y descripción
            item {
                Text(rutina!!.nombre, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                if (rutina!!.descripcion.isNotBlank()) {
                    Text(rutina!!.descripcion, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Ejercicios", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // 2. Lista de ejercicios
            if (rutina!!.ejercicios.isEmpty()) {
                item {
                    Text("Esta rutina no tiene ejercicios asignados.")
                }
            } else {
                items(rutina!!.ejercicios, key = { it.id }) { ejercicio ->
                    EjercicioDetailCard(ejercicio = ejercicio)
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


