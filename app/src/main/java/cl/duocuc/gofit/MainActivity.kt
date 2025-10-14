package cl.duocuc.gofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.basico.viewmodel.FormularioViewModel
import cl.duocuc.gofit.ui.FormularioLogin
import cl.duocuc.gofit.ui.HomeScreen
import cl.duocuc.gofit.ui.theme.GoFitTheme
import cl.duocuc.gofit.ui.WorkoutSessionScreen


class MainActivity : ComponentActivity() {
    // Ya no necesitas crear el viewModel aquí, lo haremos dentro de los composables.
    // private val viewModel = FormularioViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoFitTheme {
                // Llama a la función principal que contendrá la navegación
                GoFitApp()
            }
        }
    }
}

@Composable
fun GoFitApp() {
    // 1. Crea el NavController
    val navController = rememberNavController()// 2. Crea la instancia del ViewModel
    val formularioViewModel: FormularioViewModel = viewModel()

    // 3. Configura el NavHost
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 4. Define la ruta "login"
        composable("login") {
            FormularioLogin(
                navController = navController,
                viewModel = formularioViewModel
            )
        }

        // 5. Define la ruta "home" con todos sus parámetros
        composable("home") {
            // ▼▼▼ AQUÍ ESTÁ LA CORRECCIÓN ▼▼▼
            HomeScreen(
                navController = navController,
                onNavigateToRutinas = { navController.navigate("rutinas_list") },
                onNavigateToProgreso = { navController.navigate("progreso") },
                onStartWorkout = { rutinaId ->
                    navController.navigate("workout_session/$rutinaId")
                }
            )
        }

        // 6. (Opcional) Define las rutas de destino para que la navegación funcione
        composable("rutinas_list") {
            // Aquí llamarías a tu pantalla de rutinas cuando la crees
            // Por ahora, un Text sirve para probar la navegación
            Text("Pantalla de Lista de Rutinas")
        }

        composable("progreso") {
            Text("Pantalla de Progreso")
        }

        // La ruta de la sesión de entrenamiento. Importa WorkoutSessionScreen
        composable("workout_session/{rutinaId}") { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getString("rutinaId")
            WorkoutSessionScreen(navController = navController)
        }
    }
}
