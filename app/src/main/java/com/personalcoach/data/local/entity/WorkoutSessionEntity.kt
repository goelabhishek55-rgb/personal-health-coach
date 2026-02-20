package com.personalcoach.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_session")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val category: String,
    val tags: String = "[]",
    val motivationBefore: Int = 5,
    val sessionQuality: Int = 5,
    val sessionNotes: String? = null,
    val durationMinutes: Int? = null,
    val averageHeartRate: Int? = null,
    val caloriesBurned: Int? = null,
    val distanceMeters: Float? = null,
    val createdAt: Long = System.currentTimeMillis()
)
