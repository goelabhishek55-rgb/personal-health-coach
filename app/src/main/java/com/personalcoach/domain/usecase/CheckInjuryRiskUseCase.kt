package com.personalcoach.domain.usecase

import com.personalcoach.domain.repository.InjuryHealthConditionRepository
import javax.inject.Inject

data class InjuryWarning(
    val conditionName: String,
    val riskyCategory: String?,
    val riskyExercise: String?
)

class CheckInjuryRiskUseCase @Inject constructor(
    private val repository: InjuryHealthConditionRepository
) {
    suspend fun check(category: String, exerciseNames: List<String>): List<InjuryWarning> {
        val activeConditions = repository.getActiveConditionsOnce()
        val warnings = mutableListOf<InjuryWarning>()
        for (condition in activeConditions) {
            if (condition.riskyExerciseCategories.contains(category)) {
                warnings.add(InjuryWarning(condition.name, category, null))
            }
            for (exercise in exerciseNames) {
                if (condition.riskyExerciseNames.any { it.equals(exercise, ignoreCase = true) }) {
                    warnings.add(InjuryWarning(condition.name, null, exercise))
                }
            }
        }
        return warnings
    }
}
