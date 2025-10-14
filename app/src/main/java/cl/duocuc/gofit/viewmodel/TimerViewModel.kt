package cl.duocuc.gofit.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerViewModel : ViewModel() {    private val _tiempoRestante = MutableStateFlow("00:00")
    val tiempoRestante = _tiempoRestante.asStateFlow()

    private val _estaCorriendo = MutableStateFlow(false)
    val estaCorriendo = _estaCorriendo.asStateFlow()

    private var countDownTimer: CountDownTimer? = null

    fun iniciarTimer(segundos: Int, alFinalizar: () -> Unit) {
        if (_estaCorriendo.value) return // Evita iniciar múltiples timers

        _estaCorriendo.value = true
        countDownTimer = object : CountDownTimer(segundos * 1000L, 1000) {
            override fun onTick(millisRestantes: Long) {
                val minutos = (millisRestantes / 1000) / 60
                val segundosTick = (millisRestantes / 1000) % 60
                _tiempoRestante.value = String.format("%02d:%02d", minutos, segundosTick)
            }

            override fun onFinish() {
                _tiempoRestante.value = "00:00"
                _estaCorriendo.value = false
                alFinalizar() // Llama a la acción de finalización (vibrar/sonar)
                resetTimer()
            }
        }.start()
    }

    fun detenerTimer() {
        countDownTimer?.cancel()
        _estaCorriendo.value = false
        resetTimer()
    }

    private fun resetTimer() {
        _tiempoRestante.value = "00:00"
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel() // Asegura que el timer se cancele si el ViewModel se destruye
    }
}
