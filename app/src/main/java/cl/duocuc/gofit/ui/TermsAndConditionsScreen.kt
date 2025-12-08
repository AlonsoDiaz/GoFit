package cl.duocuc.gofit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos y Condiciones") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Bienvenido a GoFit",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Estos términos y condiciones regulan el uso de la aplicación GoFit. Al continuar, confirmas que comprendes y aceptas cada uno de los puntos descritos.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Uso personal",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "GoFit está diseñada para acompañar tu entrenamiento personal. No se garantiza que los planes sugeridos sustituyan la asesoría de profesionales de la salud o entrenamiento físico.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Responsabilidad del usuario",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Eres responsable de revisar tus condiciones físicas antes de iniciar cualquier rutina. Debes detenerte si sientes molestias y consultar a un especialista cuando sea necesario.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Privacidad y almacenamiento",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Los datos ingresados en la app (como tu perfil y registro de entrenamientos) se almacenan localmente en tu dispositivo. No compartimos esta información con terceros.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Cambios en la aplicación",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "GoFit puede actualizarse para agregar funciones o ajustar rutinas. Te avisaremos de cambios relevantes en estos términos a través de la misma aplicación.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Contacto",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Si tienes dudas o comentarios, puedes escribirnos al correo soporte@gofit.app. Nos interesa mejorar tu experiencia.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Al continuar, confirmas que has leído y aceptas estos términos y condiciones.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
