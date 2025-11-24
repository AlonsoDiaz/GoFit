package cl.duocuc.gofit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duocuc.gofit.data.local.UserEntity
import cl.duocuc.gofit.model.FormularioModel
import cl.duocuc.gofit.model.MensajesError
import cl.duocuc.gofit.repository.FormularioRepository
import kotlinx.coroutines.launch

class FormularioViewModel(
    private val repository: FormularioRepository
) : ViewModel() {

    var formulario: FormularioModel by mutableStateOf(FormularioModel())
        private set

    var mensajesError: MensajesError by mutableStateOf(MensajesError())
        private set

    var isSubmitEnabled: Boolean by mutableStateOf(false)
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var loginSuccess: Boolean by mutableStateOf(false)
        private set

    var loginError: String? by mutableStateOf(null)
        private set

    private val emailRegex = Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$")

    init {
        viewModelScope.launch {
            repository.getLastUser()?.let { user ->
                formulario = FormularioModel(
                    nombre = user.nombre,
                    correo = user.correo,
                    edad = user.edad.toString(),
                    terminos = user.terminosAceptados
                )
                mensajesError = MensajesError()
                updateSubmitEnabled()
            }
        }
    }

    fun onNombreChange(value: String) {
        formulario = formulario.copy(nombre = value)
        mensajesError = mensajesError.copy(nombre = validarNombre(value))
        loginError = null
        updateSubmitEnabled()
    }

    fun onCorreoChange(value: String) {
        formulario = formulario.copy(correo = value)
        mensajesError = mensajesError.copy(correo = validarCorreo(value))
        loginError = null
        updateSubmitEnabled()
    }

    fun onEdadChange(value: String) {
        formulario = formulario.copy(edad = value)
        mensajesError = mensajesError.copy(edad = validarEdad(value))
        loginError = null
        updateSubmitEnabled()
    }

    fun onTerminosChange(value: Boolean) {
        formulario = formulario.copy(terminos = value)
        mensajesError = mensajesError.copy(terminos = validarTerminos(value))
        loginError = null
        updateSubmitEnabled()
    }

    fun iniciarSesion() {
        val formularioValido = validarTodo()
        if (!formularioValido) {
            loginError = "Revisa la información ingresada."
            return
        }

        isLoading = true
        loginError = null

        viewModelScope.launch {
            try {
                val edadInt = formulario.edad.toIntOrNull()
                    ?: throw IllegalStateException("Edad inválida")

                val correoNormalizado = formulario.correo.trim().lowercase()
                val nombreNormalizado = formulario.nombre.trim()

                val usuarioExistente = repository.findUserByEmail(correoNormalizado)

                if (usuarioExistente != null && usuarioExistente.nombre != nombreNormalizado) {
                    loginError = "El correo ya está registrado con otro nombre."
                    return@launch
                }

                val entidad = (usuarioExistente ?: UserEntity(
                    nombre = nombreNormalizado,
                    correo = correoNormalizado,
                    edad = edadInt,
                    terminosAceptados = formulario.terminos
                )).copy(
                    nombre = nombreNormalizado,
                    correo = correoNormalizado,
                    edad = edadInt,
                    terminosAceptados = formulario.terminos,
                    lastLoginAt = System.currentTimeMillis()
                )

                repository.upsertUser(entidad)
                loginSuccess = true
            } catch (throwable: Throwable) {
                loginError = "Ocurrió un error al guardar los datos. Intenta nuevamente."
            } finally {
                isLoading = false
            }
        }
    }

    fun onLoginHandled() {
        loginSuccess = false
    }

    private fun validarTodo(): Boolean {
        val nombreError = validarNombre(formulario.nombre)
        val correoError = validarCorreo(formulario.correo)
        val edadError = validarEdad(formulario.edad)
        val terminosError = validarTerminos(formulario.terminos)

        mensajesError = MensajesError(
            nombre = nombreError,
            correo = correoError,
            edad = edadError,
            terminos = terminosError
        )

        updateSubmitEnabled()
        return nombreError.isEmpty() &&
                correoError.isEmpty() &&
                edadError.isEmpty() &&
                terminosError.isEmpty()
    }

    private fun validarNombre(value: String): String {
        return if (value.isBlank()) "El nombre no puede estar vacío" else ""
    }

    private fun validarCorreo(value: String): String {
        return if (value.isBlank() || !emailRegex.matches(value.trim().lowercase())) {
            "El correo no es válido"
        } else {
            ""
        }
    }

    private fun validarEdad(value: String): String {
        val edadInt = value.toIntOrNull()
        return if (edadInt == null || edadInt < 0 || edadInt > 90) {
            "La edad debe ser un número entre 0 y 90"
        } else {
            ""
        }
    }

    private fun validarTerminos(value: Boolean): String {
        return if (!value) "Debes aceptar los términos" else ""
    }

    private fun updateSubmitEnabled() {
        val edadInt = formulario.edad.toIntOrNull()
        val correoValido = formulario.correo.isNotBlank() && emailRegex.matches(formulario.correo.trim().lowercase())
        isSubmitEnabled = formulario.nombre.isNotBlank() &&
            correoValido &&
                edadInt != null && edadInt in 0..90 &&
                formulario.terminos
    }

    companion object {
        fun provideFactory(repository: FormularioRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(FormularioViewModel::class.java)) {
                        return FormularioViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}