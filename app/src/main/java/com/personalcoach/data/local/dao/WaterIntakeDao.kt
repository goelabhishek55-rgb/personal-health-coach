package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Query("SELECT * FROM water_intake WHERE date = :date LIMIT 1")
    fun getWaterIntakeForDay(date: Long): Flow<WaterIntakeEntity?>

    @Query("SELECT * FROM water_intake WHERE date = :date LIMIT 1")
    suspend fun getWaterIntakeForDayOnce(date: Long): WaterIntakeEntity?

    @Query("SELECT * FROM water_intake WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getWaterIntakeInRange(startDate: Long, endDate: Long): Flow<List<WaterIntakeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(waterIntake: WaterIntakeEntity): Long

    @Update
    suspend fun update(waterIntake: WaterIntakeEntity)

    @Delete
    suspend fun delete(waterIntake: WaterIntakeEntity)
}
