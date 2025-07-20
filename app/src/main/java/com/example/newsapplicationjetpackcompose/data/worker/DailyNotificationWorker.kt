package com.example.newsapplicationjetpackcompose.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.newsapplicationjetpackcompose.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
): Worker(context, params) {

    override fun doWork(): Result {
        return try {
            Log.e("DailyNotificationWorker", "Start")
            showDailyNotification()
            Log.e("DailyNotificationWorker", "Working")
            Result.success()
        } catch (e: Exception){
            Log.e("DailyNotificationWorker", "Error!")
            Result.failure(Data.Builder().putString("error", e.toString()).build())
        }
    }

    private fun showDailyNotification(){
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Daily Reminder")
            .setContentText("Don't forget to read your saved articles!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Daily Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily reminder notification"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "daily_notifications_channel"
        private const val NOTIFICATION_ID = 1
    }

}