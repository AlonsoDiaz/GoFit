package cl.duocuc.gofit.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.unit.sp


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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center // Cambiado para centrar todo verticalmente
    ) {
        Text("¡Bienvenido a GoFit!", fontSize = 28.sp, fontWeight = FontWeight.Bold)


        Button(
            onClick = { onStartWorkout("rutina_del_dia") }, // ID de ejemplo
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Text("Comenzar Entrenamiento de Hoy", fontSize = 20.sp)
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


        Button(
            onClick = {
                navController.navigate("login") { popUpTo("home") { inclusive = true } }
            }
        ) {
            Text("Cerrar Sesión")
        }
    }
}
