package com.personalcoach.domain.repository

import com.personalcoach.domain.model.GutCheckIn
import kotlinx.coroutines.flow.Flow

interface GutRepository {
    fun getAllCheckIns(): Flow<List<GutCheckIn>>
    fun getCheckInsForDay(startOfDay: Long, endOfDay: Long): Flow<List<GutCheckIn>>
    fun getCheckInsInRange(startDate: Long, endDate: Long): Flow<List<GutCheckIn>>
    suspend fun getCheckInById(id: Int): GutCheckIn?
    suspend fun insertCheckIn(checkIn: GutCheckIn): Long
    suspend fun updateCheckIn(checkIn: GutCheckIn)
    suspend fun deleteCheckIn(checkIn: GutCheckIn)
}
