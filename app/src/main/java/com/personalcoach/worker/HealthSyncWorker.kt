package com.personalcoach.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.personalcoach.domain.repository.HealthConnectRepository
import com.personalcoach.util.Constants
import com.personalcoach.util.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class HealthSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val healthConnectRepository: HealthConnectRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        if (!healthConnectRepository.isAvailable) return Result.success()

        val todayStart = DateUtils.todayStartMs()
        val todayEnd = todayStart + DateUtils.DAY_MS

        runCatching {
            healthConnectRepository.syncSteps(todayStart, todayEnd)
            healthConnectRepository.syncSleepMinutes(todayStart, todayEnd)
            healthConnectRepository.syncWeight(todayStart, todayEnd)
        }

        return Result.success()
    }

    companion object {
        fun buildRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<HealthSyncWorker>(6, TimeUnit.HOURS)
                .addTag(Constants.WORKER_HEALTH_SYNC)
                .build()
    }
}
