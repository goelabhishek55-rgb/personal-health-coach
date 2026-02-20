package com.personalcoach.domain.usecase

import com.personalcoach.domain.model.*
import com.personalcoach.domain.repository.*
import com.personalcoach.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class WeeklyAnalytics(
    val workoutSessions: List<WorkoutSession>,
    val gutCheckIns: List<GutCheckIn>,
    val meals: List<MealLog>,
    val waterIntakes: List<WaterIntake>,
    val weightRecords: List<WeightRecord>,
    val totalWorkouts: Int,
    val avgGutFeel: Float,
    val avgCaloriesPerDay: Float,
    val avgWaterMlPerDay: Float
)

class GetWeeklyAnalyticsUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val gutRepository: GutRepository,
    private val nutritionRepository: NutritionRepository,
    private val waterRepository: WaterRepository,
    private val weightRepository: WeightRepository
) {
    operator fun invoke(weekStartMs: Long): Flow<WeeklyAnalytics> {
        val weekEnd = weekStartMs + 7 * DateUtils.DAY_MS
        return combine(
            workoutRepository.getSessionsInRange(weekStartMs, weekEnd),
            gutRepository.getCheckInsInRange(weekStartMs, weekEnd),
            nutritionRepository.getMealsInRange(weekStartMs, weekEnd),
            waterRepository.getWaterIntakeInRange(weekStartMs, weekEnd),
            weightRepository.getWeightRecordsInRange(weekStartMs, weekEnd)
        ) { workouts, guts, meals, water, weights ->
            WeeklyAnalytics(
                workoutSessions = workouts,
                gutCheckIns = guts,
                meals = meals,
                waterIntakes = water,
                weightRecords = weights,
                totalWorkouts = workouts.size,
                avgGutFeel = if (guts.isEmpty()) 0f else guts.map { it.overallGutFeel }.average().toFloat(),
                avgCaloriesPerDay = if (meals.isEmpty()) 0f else meals.sumOf { it.calories }.toFloat() / 7f,
                avgWaterMlPerDay = if (water.isEmpty()) 0f else water.sumOf { it.totalMl }.toFloat() / 7f
            )
        }
    }
}
