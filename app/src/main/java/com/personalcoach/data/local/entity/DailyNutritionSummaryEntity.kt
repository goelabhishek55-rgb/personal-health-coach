package com.personalcoach.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_nutrition_summary")
data class DailyNutritionSummaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
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
    val reasonsForMiss: String = "[]",
    val reasonNotes: String? = null
)
