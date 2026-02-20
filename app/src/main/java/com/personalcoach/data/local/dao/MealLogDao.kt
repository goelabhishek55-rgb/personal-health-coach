package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.MealLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealLogDao {
    @Query("SELECT * FROM meal_log ORDER BY dateTime DESC")
    fun getAllMeals(): Flow<List<MealLogEntity>>

    @Query("SELECT * FROM meal_log WHERE dateTime >= :startOfDay AND dateTime < :endOfDay ORDER BY dateTime ASC")
    fun getMealsForDay(startOfDay: Long, endOfDay: Long): Flow<List<MealLogEntity>>

    @Query("SELECT * FROM meal_log WHERE dateTime >= :startDate AND dateTime <= :endDate ORDER BY dateTime DESC")
    fun getMealsInRange(startDate: Long, endDate: Long): Flow<List<MealLogEntity>>

    @Insert
    suspend fun insert(meal: MealLogEntity): Long

    @Update
    suspend fun update(meal: MealLogEntity)

    @Delete
    suspend fun delete(meal: MealLogEntity)

    @Query("SELECT * FROM meal_log WHERE description LIKE '%' || :query || '%' ORDER BY dateTime DESC LIMIT 20")
    suspend fun searchMeals(query: String): List<MealLogEntity>
}
