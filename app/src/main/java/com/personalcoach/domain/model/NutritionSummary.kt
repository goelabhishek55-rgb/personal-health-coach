package com.personalcoach.domain.model

data class NutritionSummary(
    val id: Int = 0,
    val date: Long,
    val targetCalories: Int,
    val targetProteinG: Float,
    val targetCarbsG: Float,
    val targetFatsG: Float,
    val targetFiberG: Float,
    val actualCalories: Int = 0,
    val actualProteinG: Float = 0f,
    val actualCarbsG: Float = 0f,
    val actualFatsG: Float = 0f,
    val actualFiberG: Float = 0f,
    val goalAchieved: Boolean = false,
    val adherencePercent: Float = 0f,
    val reasonsForMiss: List<String> = emptyList(),
    val reasonNotes: String? = null
)
