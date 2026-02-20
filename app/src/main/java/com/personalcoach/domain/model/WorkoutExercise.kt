package com.personalcoach.domain.model

data class WorkoutExercise(
    val id: Int = 0,
    val sessionId: Int,
    val exerciseName: String,
    val sets: Int? = null,
    val reps: Int? = null,
    val weightKg: Float? = null,
    val durationSeconds: Int? = null,
    val distanceMeters: Float? = null,
    val notes: String? = null,
    val orderIndex: Int = 0
)
