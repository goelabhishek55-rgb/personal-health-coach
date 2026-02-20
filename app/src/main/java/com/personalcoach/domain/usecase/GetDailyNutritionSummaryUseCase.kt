package com.personalcoach.domain.usecase

import com.personalcoach.domain.model.NutritionSummary
import com.personalcoach.domain.repository.NutritionRepository
import com.personalcoach.util.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDailyNutritionSummaryUseCase @Inject constructor(
    private val repository: NutritionRepository
) {
    operator fun invoke(date: Long = System.currentTimeMillis()): Flow<NutritionSummary?> {
        val startOfDay = DateUtils.startOfDay(date)
        return repository.getNutritionSummaryForDay(startOfDay)
    }
}
