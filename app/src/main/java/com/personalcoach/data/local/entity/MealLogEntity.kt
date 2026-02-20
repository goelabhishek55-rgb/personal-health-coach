package com.personalcoach.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_log")
data class MealLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: Long,
    val mealType: String,
    val description: String? = null,
    val calories: Int,
    val proteinG: Float,
    val carbsG: Float,
    val fatsG: Float,
    val fiberG: Float? = null,
    val qualityFlag: String = "on_plan",
    val barcode: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
