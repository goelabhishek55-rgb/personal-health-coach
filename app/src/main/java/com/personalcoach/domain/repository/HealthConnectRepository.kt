package com.personalcoach.domain.repository

interface HealthConnectRepository {
    val isAvailable: Boolean
    suspend fun syncSteps(start: Long, end: Long): Long
    suspend fun syncSleepMinutes(start: Long, end: Long): Long
    suspend fun syncWeight(start: Long, end: Long): Float?
    suspend fun syncAverageHeartRate(start: Long, end: Long): Int?
}
