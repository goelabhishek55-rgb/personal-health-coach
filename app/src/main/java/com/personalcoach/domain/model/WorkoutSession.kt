package com.personalcoach.domain.model

data class WorkoutSession(
    val id: Int = 0,
    val date: Long,
    val category: String,
    val tags: List<String> = emptyList(),
    val motivationBefore: Int = 5,
    val sessionQuality: Int = 5,
    val sessionNotes: String? = null,
    val durationMinutes: Int? = null,
    val averageHeartRate: Int? = null,
    val caloriesBurned: Int? = null,
    val distanceMeters: Float? = null,
    val exercises: List<WorkoutExercise> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
