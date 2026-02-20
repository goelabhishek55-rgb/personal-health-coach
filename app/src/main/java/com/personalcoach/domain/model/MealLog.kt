package com.personalcoach.domain.model

data class MealLog(
    val id: Int = 0,
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
