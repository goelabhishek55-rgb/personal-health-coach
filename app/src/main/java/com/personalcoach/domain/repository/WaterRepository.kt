package com.personalcoach.domain.repository

import com.personalcoach.domain.model.WaterIntake
import kotlinx.coroutines.flow.Flow

interface WaterRepository {
    fun getWaterIntakeForDay(date: Long): Flow<WaterIntake?>
    suspend fun getWaterIntakeForDayOnce(date: Long): WaterIntake?
    fun getWaterIntakeInRange(startDate: Long, endDate: Long): Flow<List<WaterIntake>>
    suspend fun saveWaterIntake(waterIntake: WaterIntake)
}
