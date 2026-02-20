package com.personalcoach.domain.model

data class UserProfile(
    val id: Int = 0,
    val name: String = "",
    val age: Int = 0,
    val sex: String = "Male",
    val heightCm: Float = 170f,
    val primaryGoals: List<String> = emptyList(),
    val defaultUnits: String = "metric",
    val dailyCalorieTarget: Int = 2000,
    val dailyProteinTargetG: Float = 150f,
    val dailyCarbsTargetG: Float = 250f,
    val dailyFatsTargetG: Float = 65f,
    val dailyFiberTargetG: Float = 25f,
    val dailyWaterTargetMl: Int = 2500,
    val waterGlassSizeMl: Int = 250,
    val samsungHealthEnabled: Boolean = false,
    val onboardingCompleted: Boolean = false
)
