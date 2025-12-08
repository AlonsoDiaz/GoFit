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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import cl.duocuc.gofit.R
import cl.duocuc.gofit.viewmodel.FormularioViewModel

@Composable
fun FormularioLogin(navController: NavController, viewModel: FormularioViewModel) {
    val formulario = viewModel.formulario
    val mensajesError = viewModel.mensajesError
    val isLoading = viewModel.isLoading
    val loginError = viewModel.loginError
    val loginSuccess = viewModel.loginSuccess

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logotipo de GoFit",
            modifier = Modifier
                .height(120.dp)
                .padding(bottom = 16.dp)
        )
        Text("¡Bienvenido a GoFit!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Registra tus datos para guardar tu acceso y personalizar tu experiencia de entrenamiento.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = formulario.nombre,
            onValueChange = viewModel::onNombreChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Ingresa nombre") },
            isError = mensajesError.nombre.isNotEmpty(),
            supportingText = {
                if (mensajesError.nombre.isNotEmpty()) {
                    Text(mensajesError.nombre, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = formulario.correo,
            onValueChange = viewModel::onCorreoChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Ingresa correo") },
            isError = mensajesError.correo.isNotEmpty(),
            supportingText = {
                if (mensajesError.correo.isNotEmpty()) {
                    Text(mensajesError.correo, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = formulario.edad,
            onValueChange = viewModel::onEdadChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Ingresa edad") },
            isError = mensajesError.edad.isNotEmpty(),
            supportingText = {
                if (mensajesError.edad.isNotEmpty()) {
                    Text(mensajesError.edad, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = formulario.terminos,
                onCheckedChange = viewModel::onTerminosChange,
            )
            val termsText = buildAnnotatedString {
                append("He leído y acepto ")
                pushStringAnnotation(tag = "TERMS", annotation = "terms")
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("los términos y condiciones")
                }
                pop()
            }
            ClickableText(
                modifier = Modifier.padding(start = 8.dp),
                text = termsText,
                style = MaterialTheme.typography.bodyMedium,
                onClick = { offset ->
                    termsText
                        .getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                        .firstOrNull()
                        ?.let {
                            navController.navigate("terms")
                        }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = viewModel.isSubmitEnabled && !isLoading,
            onClick = { viewModel.iniciarSesion() }
        ) {
            Text(if (isLoading) "Enviando..." else "Enviar")
        }

        loginError?.let { mensaje ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    if (loginSuccess) {
        AlertDialog(
            onDismissRequest = { /* Bloquea cierre mientras está visible */ },
            title = { Text("Confirmación") },
            text = { Text("Formulario enviado correctamente") },
            confirmButton = {
                Button(onClick = {
                    viewModel.onLoginHandled()
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }) {
                    Text("OK")
                }
            }
        )
    }
}
