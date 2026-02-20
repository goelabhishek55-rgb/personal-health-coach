package com.personalcoach

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.personalcoach.util.Constants
import com.personalcoach.worker.HealthSyncWorker
import com.personalcoach.worker.ReminderWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PersonalCoachApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        setupWorkManager()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(
                NotificationChannel(
                    Constants.CHANNEL_REMINDERS,
                    "Health Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "Daily health tracking reminders" }
            )
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    Constants.CHANNEL_HEALTH_SYNC,
                    "Health Sync",
                    NotificationManager.IMPORTANCE_LOW
                ).apply { description = "Background health data sync" }
            )
        }
    }

    private fun setupWorkManager() {
        val workManager = WorkManager.getInstance(this)

        workManager.enqueueUniquePeriodicWork(
            Constants.WORKER_GUT_REMINDER,
            ExistingPeriodicWorkPolicy.KEEP,
            ReminderWorker.buildRequest(ReminderWorker.TYPE_GUT, 9, Constants.WORKER_GUT_REMINDER)
        )
        workManager.enqueueUniquePeriodicWork(
            Constants.WORKER_WATER_REMINDER,
            ExistingPeriodicWorkPolicy.KEEP,
            ReminderWorker.buildRequest(ReminderWorker.TYPE_WATER, 2, Constants.WORKER_WATER_REMINDER)
        )
        workManager.enqueueUniquePeriodicWork(
            Constants.WORKER_WEIGHT_REMINDER,
            ExistingPeriodicWorkPolicy.KEEP,
            ReminderWorker.buildRequest(ReminderWorker.TYPE_WEIGHT, 8, Constants.WORKER_WEIGHT_REMINDER)
        )
        workManager.enqueueUniquePeriodicWork(
            Constants.WORKER_HEALTH_SYNC,
            ExistingPeriodicWorkPolicy.KEEP,
            HealthSyncWorker.buildRequest()
        )
    }
}
