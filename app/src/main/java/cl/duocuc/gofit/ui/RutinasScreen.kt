package cl.duocuc.gofit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duocuc.gofit.model.Rutina
import cl.duocuc.gofit.viewmodel.FakeRutinaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(navController: NavController) {
    // Usamos el repositorio falso para obtener los datos
    val rutinas = remember { FakeRutinaRepository().getRutinas() }

    Scaffold(
        // --- INICIO DE LA MODIFICACIÓN ---
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
        },
        // --- FIN DE LA MODIFICACIÓN ---
        floatingActionButton = {
            // ... (código del FAB se mantiene igual)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp) // Ajuste de padding
        ) {
            // El título que tenías aquí ya no es necesario, porque está en la TopAppBar.
            // Text("Mis Rutinas", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            // Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(rutinas, key = { it.id }) { rutina ->
                    RutinaCard(
                        rutina = rutina,
                        onClick = {
                            navController.navigate("rutina_detail/${rutina.id}")
                        }
                    )
                }
            }
        }
    }
}

// Modificamos RutinaCard para que acepte un lambda onClick
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaCard(rutina: Rutina, onClick: () -> Unit) { // Ahora 'Rutina' será reconocido
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(rutina.nombre, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            if (rutina.descripcion.isNotBlank()) {
                Text(rutina.descripcion, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
