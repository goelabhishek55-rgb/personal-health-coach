package com.personalcoach.domain.repository

import com.personalcoach.domain.model.MealLog
import com.personalcoach.domain.model.NutritionSummary
import kotlinx.coroutines.flow.Flow

interface NutritionRepository {
    fun getAllMeals(): Flow<List<MealLog>>
    fun getMealsForDay(startOfDay: Long, endOfDay: Long): Flow<List<MealLog>>
    fun getMealsInRange(startDate: Long, endDate: Long): Flow<List<MealLog>>
    suspend fun insertMeal(meal: MealLog): Long
    suspend fun updateMeal(meal: MealLog)
    suspend fun deleteMeal(meal: MealLog)
    fun getNutritionSummaryForDay(date: Long): Flow<NutritionSummary?>
    suspend fun saveNutritionSummary(summary: NutritionSummary)
}
