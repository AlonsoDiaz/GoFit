package cl.duocuc.gofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import cl.duoc.basico.viewmodel.FormularioViewModel
// Importa las clases necesarias
import cl.duocuc.gofit.repository.ProgresoRepository
import cl.duocuc.gofit.ui.*
import cl.duocuc.gofit.ui.theme.GoFitTheme
import cl.duocuc.gofit.uiimport.ProgresoScreen
import cl.duocuc.gofit.viewmodel.ProgresoViewModel
import cl.duocuc.gofit.viewmodel.WorkoutViewModel

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


    val progresoRepository = ProgresoRepository()


    val workoutViewModel: WorkoutViewModel = viewModel { WorkoutViewModel(progresoRepository) }
    val progresoViewModel: ProgresoViewModel = viewModel { ProgresoViewModel(progresoRepository) }


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
            ProgresoScreen(
                navController = navController,
                progresoViewModel = progresoViewModel
            )
        }

        // 5. Ruta para una Sesión de Entrenamiento
        composable(
            route = "workout_session/{rutinaId}",
            arguments = listOf(navArgument("rutinaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getString("rutinaId")
            WorkoutSessionScreen(
                rutinaId = rutinaId ?: "default_rutina_id",
                workoutViewModel = workoutViewModel,
                onFinishWorkout = {
                    navController.navigate("progreso") {
                        popUpTo("home")
                    }
                }
            )
        }
    }
}
