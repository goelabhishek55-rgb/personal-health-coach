package com.personalcoach.data.repository

import com.personalcoach.data.health.HealthConnectDataSource
import com.personalcoach.domain.repository.HealthConnectRepository
import java.time.Instant
import javax.inject.Inject

class HealthConnectRepositoryImpl @Inject constructor(
    private val dataSource: HealthConnectDataSource
) : HealthConnectRepository {

    override val isAvailable: Boolean
        get() = dataSource.isAvailable

    override suspend fun syncSteps(start: Long, end: Long): Long =
        dataSource.readSteps(Instant.ofEpochMilli(start), Instant.ofEpochMilli(end))

    override suspend fun syncSleepMinutes(start: Long, end: Long): Long {
        val sessions = dataSource.readSleepSessions(
            Instant.ofEpochMilli(start), Instant.ofEpochMilli(end)
        )
        return sessions.sumOf { session ->
            val startMs = session.startTime.toEpochMilli()
            val endMs = session.endTime.toEpochMilli()
            (endMs - startMs) / 60_000
        }
    }

    override suspend fun syncWeight(start: Long, end: Long): Float? {
        val records = dataSource.readWeightRecords(
            Instant.ofEpochMilli(start), Instant.ofEpochMilli(end)
        )
        return records.lastOrNull()?.weight?.inKilograms?.toFloat()
    }

    override suspend fun syncAverageHeartRate(start: Long, end: Long): Int? {
        val records = dataSource.readHeartRate(
            Instant.ofEpochMilli(start), Instant.ofEpochMilli(end)
        )
        val allSamples = records.flatMap { it.samples }
        return if (allSamples.isEmpty()) null
        else allSamples.map { it.beatsPerMinute }.average().toInt()
    }
}
