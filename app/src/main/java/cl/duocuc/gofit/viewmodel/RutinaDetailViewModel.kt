package cl.duocuc.gofit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duocuc.gofit.model.Rutina
import cl.duocuc.gofit.repository.RoutinesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RutinaDetailViewModel(
    private val repository: RoutinesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RutinaDetailUiState())
    val uiState: StateFlow<RutinaDetailUiState> = _uiState.asStateFlow()

    fun cargarRutina(rutinaId: String) {
        _uiState.value = RutinaDetailUiState(isLoading = true)
        viewModelScope.launch {
            runCatching { repository.getRoutineById(rutinaId) }
                .onSuccess { rutina ->
                    if (rutina != null) {
                        _uiState.value = RutinaDetailUiState(rutina = rutina)
                    } else {
                        _uiState.value = RutinaDetailUiState(errorMessage = "Rutina no encontrada")
                    }
                }
                .onFailure { throwable ->
                    _uiState.value = RutinaDetailUiState(
                        errorMessage = throwable.message ?: "No se pudo cargar la rutina"
                    )
                }
        }
    }

    fun marcarError(message: String) {
        _uiState.value = RutinaDetailUiState(errorMessage = message)
    }

    companion object {
        fun provideFactory(repository: RoutinesRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(RutinaDetailViewModel::class.java)) {
                        return RutinaDetailViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

data class RutinaDetailUiState(
    val rutina: Rutina? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


