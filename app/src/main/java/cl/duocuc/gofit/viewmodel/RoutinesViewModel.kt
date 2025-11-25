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

class RoutinesViewModel(
    private val repository: RoutinesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutinesUiState())
    val uiState: StateFlow<RoutinesUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            runCatching { repository.getRoutines(forceRefresh = true) }
                .onSuccess { routines ->
                    _uiState.value = RoutinesUiState(data = routines)
                }
                .onFailure { throwable ->
                    val fallback = runCatching { repository.getRoutines(forceRefresh = false) }
                        .getOrDefault(_uiState.value.data)

                    _uiState.value = RoutinesUiState(
                        data = fallback,
                        errorMessage = throwable.message ?: "No se pudo cargar la información"
                    )
                }
        }
    }

    companion object {
        fun provideFactory(repository: RoutinesRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(RoutinesViewModel::class.java)) {
                        return RoutinesViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

data class RoutinesUiState(
    val data: List<Rutina> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
