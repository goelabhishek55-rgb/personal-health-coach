package com.personalcoach.domain.usecase

import com.personalcoach.domain.model.MealLog
import com.personalcoach.domain.repository.NutritionRepository
import javax.inject.Inject

class LogMealUseCase @Inject constructor(
    private val repository: NutritionRepository
) {
    suspend operator fun invoke(meal: MealLog): Long {
        return repository.insertMeal(meal)
    }
}
