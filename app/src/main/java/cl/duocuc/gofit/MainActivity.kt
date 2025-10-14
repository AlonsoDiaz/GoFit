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

        composable("login") {
            FormularioLogin(
                navController = navController,
                viewModel = formularioViewModel
            )
        }


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


        composable("rutinas_list") {

            Text("Pantalla de Lista de Rutinas")
        }

        composable("progreso") {
            Text("Pantalla de Progreso")
        }


        composable("workout_session/{rutinaId}") { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getString("rutinaId")
            WorkoutSessionScreen(navController = navController)
        }
    }
}
