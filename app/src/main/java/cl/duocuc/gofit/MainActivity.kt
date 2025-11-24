package cl.duocuc.gofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duocuc.gofit.data.local.AppDatabase
import cl.duocuc.gofit.data.remote.RoutineApiService
import cl.duocuc.gofit.repository.FormularioRepository
import cl.duocuc.gofit.repository.ProgresoRepository
import cl.duocuc.gofit.repository.RoutinesRepository
import cl.duocuc.gofit.ui.*
import cl.duocuc.gofit.ui.theme.GoFitTheme
import cl.duocuc.gofit.viewmodel.FormularioViewModel
import cl.duocuc.gofit.viewmodel.ProgresoViewModel
import cl.duocuc.gofit.viewmodel.RutinaDetailViewModel
import cl.duocuc.gofit.viewmodel.RoutinesViewModel
import cl.duocuc.gofit.viewmodel.WorkoutViewModel

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getInstance(applicationContext) }
    private val formularioRepository by lazy { FormularioRepository(database.userDao()) }
    private val progresoRepository by lazy { ProgresoRepository(database.workoutHistoryDao()) }
    private val routineApiService by lazy { RoutineApiService.create() }
    private val routinesRepository by lazy { RoutinesRepository(routineApiService) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoFitTheme {
                GoFitApp(
                    formularioRepository = formularioRepository,
                    progresoRepository = progresoRepository,
                    routinesRepository = routinesRepository
                )
            }
        }
    }
}

@Composable
fun GoFitApp(
    formularioRepository: FormularioRepository,
    progresoRepository: ProgresoRepository,
    routinesRepository: RoutinesRepository
) {
    val navController = rememberNavController()

    val formularioViewModel: FormularioViewModel = viewModel(
        factory = FormularioViewModel.provideFactory(formularioRepository)
    )

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
        composable("rutinas_list") { backStackEntry ->
            val screenViewModel: RoutinesViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = RoutinesViewModel.provideFactory(routinesRepository)
            )
            RutinasScreen(navController = navController, viewModel = screenViewModel)
        }

        composable(
            route = "rutina_detail/{rutinaId}",
            arguments = listOf(navArgument("rutinaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getString("rutinaId")
            val detailViewModel: RutinaDetailViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = RutinaDetailViewModel.provideFactory(routinesRepository)
            )
            RutinaDetailScreen(navController = navController, rutinaId = rutinaId, viewModel = detailViewModel)
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
                },
                navController = navController // <-- ¡AQUÍ ESTÁ LA CORRECCIÓN!
            )
        }
    }
}
