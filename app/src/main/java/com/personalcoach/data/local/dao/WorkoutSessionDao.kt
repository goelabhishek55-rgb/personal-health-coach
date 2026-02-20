package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {
    @Query("SELECT * FROM workout_session ORDER BY date DESC")
    fun getAllSessions(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_session WHERE date >= :startOfDay AND date < :endOfDay")
    fun getSessionsForDay(startOfDay: Long, endOfDay: Long): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_session WHERE id = :id")
    suspend fun getSessionById(id: Int): WorkoutSessionEntity?

    @Insert
    suspend fun insert(session: WorkoutSessionEntity): Long

    @Update
    suspend fun update(session: WorkoutSessionEntity)

    @Delete
    suspend fun delete(session: WorkoutSessionEntity)

    @Query("SELECT * FROM workout_session WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getSessionsInRange(startDate: Long, endDate: Long): Flow<List<WorkoutSessionEntity>>
}
