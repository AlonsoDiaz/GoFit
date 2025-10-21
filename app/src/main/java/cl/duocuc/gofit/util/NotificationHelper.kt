// En cl/duocuc/gofit/util/NotificationHelper.kt
package cl.duocuc.gofit.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import cl.duocuc.gofit.R

class NotificationHelper(private val context: Context) {

    private val channelId = "gofit_channel"
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "GoFit Entrenamientos"
            val descriptionText = "Notificaciones sobre entrenamientos completados"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showWorkoutCompletedNotification(rutinaNombre: String) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¡Entrenamiento Completado!")
            .setContentText("¡Felicidades! Has completado la rutina: $rutinaNombre")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        // Muestra la notificación
        notificationManager.notify(1, builder.build())
    }
}


