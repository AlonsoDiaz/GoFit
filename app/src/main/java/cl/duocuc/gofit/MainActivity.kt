package cl.duocuc.gofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.basico.viewmodel.FormularioViewModel
import cl.duocuc.gofit.ui.FormularioLogin
import cl.duocuc.gofit.ui.HomeScreen
import cl.duocuc.gofit.ui.RutinaDetailScreen
import cl.duocuc.gofit.ui.RutinasScreen
import cl.duocuc.gofit.ui.theme.GoFitTheme
import cl.duocuc.gofit.ui.WorkoutSessionScreen


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoFitTheme {

                GoFitApp()
            }
        }
    }
}


@Composable
fun GoFitApp() {
    val navController = rememberNavController()
    val formularioViewModel: FormularioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 1. Ruta de Login
        composable("login") {
            FormularioLogin(
                navController = navController,
                viewModel = formularioViewModel
            )
        }

        // 2. Ruta de la Pantalla Principal (Home)
        composable("home") {
            HomeScreen(
                navController = navController,
                onNavigateToRutinas = { navController.navigate("rutinas_list") },
                onNavigateToProgreso = { navController.navigate("progreso") },
                onStartWorkout = { rutinaId ->
                    navController.navigate("workout_session/$rutinaId")
                }
            )
        }

        // 3. Ruta para la lista de Rutinas
        composable("rutinas_list") {
            RutinasScreen(navController = navController)
        }

        composable(
            route = "rutina_detail/{rutinaId}",
            arguments = listOf(navArgument("rutinaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getString("rutinaId")
            RutinaDetailScreen(navController = navController, rutinaId = rutinaId)
        }

        // 4. Ruta para la pantalla de Progreso
        composable("progreso") {
            // Este es un marcador de posición, puedes crear la pantalla más adelante
            Text("Pantalla de Progreso")
        }

        // 5. Ruta para una Sesión de Entrenamiento
        composable("workout_session/{rutinaId}") {
            // El backStackEntry ya no se usa, pero la ruta se mantiene
            WorkoutSessionScreen(navController = navController)
        }
    }
}

