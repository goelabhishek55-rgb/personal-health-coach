package com.personalcoach.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.personalcoach.util.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val reminderType = inputData.getString(KEY_REMINDER_TYPE) ?: return Result.failure()
        showNotification(reminderType)
        return Result.success()
    }

    private fun showNotification(reminderType: String) {
        val (title, message, notifId) = when (reminderType) {
            TYPE_GUT -> Triple("Gut Check-In", "Time for your daily gut check-in!", Constants.NOTIFICATION_GUT_REMINDER)
            TYPE_WATER -> Triple("Stay Hydrated!", "Don't forget to log your water intake.", Constants.NOTIFICATION_WATER_REMINDER)
            TYPE_WEIGHT -> Triple("Weight Log", "Log your weight today to track your progress.", Constants.NOTIFICATION_WEIGHT_REMINDER)
            TYPE_NUTRITION -> Triple("Nutrition Review", "How did you eat today? Log your meals!", Constants.NOTIFICATION_NUTRITION_REVIEW)
            else -> return
        }

        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notifId, notification)
    }

    companion object {
        const val KEY_REMINDER_TYPE = "reminder_type"
        const val TYPE_GUT = "gut"
        const val TYPE_WATER = "water"
        const val TYPE_WEIGHT = "weight"
        const val TYPE_NUTRITION = "nutrition"

        fun buildRequest(type: String, delayHours: Long, tag: String): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delayHours, TimeUnit.HOURS)
                .setInputData(workDataOf(KEY_REMINDER_TYPE to type))
                .addTag(tag)
                .build()
    }
}
