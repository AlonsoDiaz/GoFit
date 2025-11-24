package cl.duocuc.gofit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duocuc.gofit.R


@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToRutinas: () -> Unit,
    onNavigateToProgreso: () -> Unit,
    onStartWorkout: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("¡Comienza tu cambio!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Gestiona tus entrenamientos, revisa rutinas y haz seguimiento de tu progreso desde esta pantalla principal.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.gym),
                contentDescription = "Imagen motivacional de entrenamiento",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Button(
            onClick = { onStartWorkout("rutina_del_dia") },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Text("Empezar Entrenamiento de Hoy", fontSize = 20.sp)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = onNavigateToRutinas, modifier = Modifier.weight(1f)) {
                Text("Mis Rutinas")
            }
            Button(onClick = onNavigateToProgreso, modifier = Modifier.weight(1f)) {
                Text("Mi Progreso")
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = {
                navController.navigate("login") { popUpTo("home") { inclusive = true } }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar Sesión")
        }
    }
}
