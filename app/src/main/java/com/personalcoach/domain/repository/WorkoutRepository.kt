package com.personalcoach.domain.repository

import com.personalcoach.domain.model.WorkoutSession
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllSessions(): Flow<List<WorkoutSession>>
    fun getSessionsForDay(startOfDay: Long, endOfDay: Long): Flow<List<WorkoutSession>>
    fun getSessionsInRange(startDate: Long, endDate: Long): Flow<List<WorkoutSession>>
    suspend fun getSessionById(id: Int): WorkoutSession?
    suspend fun insertSession(session: WorkoutSession): Long
    suspend fun updateSession(session: WorkoutSession)
    suspend fun deleteSession(session: WorkoutSession)
}
