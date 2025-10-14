package cl.duocuc.gofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.basico.viewmodel.FormularioViewModel
import cl.duocuc.gofit.ui.FormularioLogin
import cl.duocuc.gofit.ui.theme.GoFitTheme

class MainActivity : ComponentActivity() {

    private val viewModel = FormularioViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoFitTheme {
                FormularioLogin(viewModel)
            }
        }
    }
}
