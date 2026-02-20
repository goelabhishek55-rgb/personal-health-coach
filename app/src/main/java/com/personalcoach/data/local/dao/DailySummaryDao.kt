package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.DailySummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailySummaryDao {
    @Query("SELECT * FROM daily_summary WHERE date = :date LIMIT 1")
    fun getSummaryForDay(date: Long): Flow<DailySummaryEntity?>

    @Query("SELECT * FROM daily_summary WHERE date = :date LIMIT 1")
    suspend fun getSummaryForDayOnce(date: Long): DailySummaryEntity?

    @Query("SELECT * FROM daily_summary WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getSummariesInRange(startDate: Long, endDate: Long): Flow<List<DailySummaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(summary: DailySummaryEntity): Long

    @Update
    suspend fun update(summary: DailySummaryEntity)

    @Delete
    suspend fun delete(summary: DailySummaryEntity)
}
