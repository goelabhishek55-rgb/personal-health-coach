package com.personalcoach.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.personalcoach.data.local.dao.DailyNutritionSummaryDao
import com.personalcoach.data.local.dao.MealLogDao
import com.personalcoach.data.local.entity.DailyNutritionSummaryEntity
import com.personalcoach.data.local.entity.MealLogEntity
import com.personalcoach.domain.model.MealLog
import com.personalcoach.domain.model.NutritionSummary
import com.personalcoach.domain.repository.NutritionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NutritionRepositoryImpl @Inject constructor(
    private val mealLogDao: MealLogDao,
    private val summaryDao: DailyNutritionSummaryDao
) : NutritionRepository {

    private val gson = Gson()

    override fun getAllMeals(): Flow<List<MealLog>> =
        mealLogDao.getAllMeals().map { list -> list.map { it.toDomain() } }

    override fun getMealsForDay(startOfDay: Long, endOfDay: Long): Flow<List<MealLog>> =
        mealLogDao.getMealsForDay(startOfDay, endOfDay).map { list -> list.map { it.toDomain() } }

    override fun getMealsInRange(startDate: Long, endDate: Long): Flow<List<MealLog>> =
        mealLogDao.getMealsInRange(startDate, endDate).map { list -> list.map { it.toDomain() } }

    override suspend fun insertMeal(meal: MealLog): Long = mealLogDao.insert(meal.toEntity())

    override suspend fun updateMeal(meal: MealLog) = mealLogDao.update(meal.toEntity())

    override suspend fun deleteMeal(meal: MealLog) = mealLogDao.delete(meal.toEntity())

    override fun getNutritionSummaryForDay(date: Long): Flow<NutritionSummary?> =
        summaryDao.getSummaryForDay(date).map { it?.toDomain() }

    override suspend fun saveNutritionSummary(summary: NutritionSummary) {
        summaryDao.insertOrUpdate(summary.toEntity())
    }

    private fun MealLogEntity.toDomain() = MealLog(
        id = id, dateTime = dateTime, mealType = mealType, description = description,
        calories = calories, proteinG = proteinG, carbsG = carbsG, fatsG = fatsG,
        fiberG = fiberG, qualityFlag = qualityFlag, barcode = barcode, createdAt = createdAt
    )

    private fun MealLog.toEntity() = MealLogEntity(
        id = id, dateTime = dateTime, mealType = mealType, description = description,
        calories = calories, proteinG = proteinG, carbsG = carbsG, fatsG = fatsG,
        fiberG = fiberG, qualityFlag = qualityFlag, barcode = barcode, createdAt = createdAt
    )

    private fun DailyNutritionSummaryEntity.toDomain() = NutritionSummary(
        id = id, date = date, targetCalories = targetCalories, targetProteinG = targetProteinG,
        targetCarbsG = targetCarbsG, targetFatsG = targetFatsG, targetFiberG = targetFiberG,
        actualCalories = actualCalories, actualProteinG = actualProteinG,
        actualCarbsG = actualCarbsG, actualFatsG = actualFatsG, actualFiberG = actualFiberG,
        goalAchieved = goalAchieved, adherencePercent = adherencePercent,
        reasonsForMiss = gson.fromJson(reasonsForMiss, object : TypeToken<List<String>>() {}.type) ?: emptyList(),
        reasonNotes = reasonNotes
    )

    private fun NutritionSummary.toEntity() = DailyNutritionSummaryEntity(
        id = id, date = date, targetCalories = targetCalories, targetProteinG = targetProteinG,
        targetCarbsG = targetCarbsG, targetFatsG = targetFatsG, targetFiberG = targetFiberG,
        actualCalories = actualCalories, actualProteinG = actualProteinG,
        actualCarbsG = actualCarbsG, actualFatsG = actualFatsG, actualFiberG = actualFiberG,
        goalAchieved = goalAchieved, adherencePercent = adherencePercent,
        reasonsForMiss = gson.toJson(reasonsForMiss), reasonNotes = reasonNotes
    )
}
