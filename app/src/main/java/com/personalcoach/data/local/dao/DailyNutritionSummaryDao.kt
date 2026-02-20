package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.DailyNutritionSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyNutritionSummaryDao {
    @Query("SELECT * FROM daily_nutrition_summary WHERE date = :date LIMIT 1")
    fun getSummaryForDay(date: Long): Flow<DailyNutritionSummaryEntity?>

    @Query("SELECT * FROM daily_nutrition_summary WHERE date = :date LIMIT 1")
    suspend fun getSummaryForDayOnce(date: Long): DailyNutritionSummaryEntity?

    @Query("SELECT * FROM daily_nutrition_summary WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getSummariesInRange(startDate: Long, endDate: Long): Flow<List<DailyNutritionSummaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(summary: DailyNutritionSummaryEntity): Long

    @Delete
    suspend fun delete(summary: DailyNutritionSummaryEntity)
}
