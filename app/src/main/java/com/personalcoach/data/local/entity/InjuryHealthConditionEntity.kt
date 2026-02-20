package com.personalcoach.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "injury_health_condition")
data class InjuryHealthConditionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val affectedArea: String,
    val severity: Int,
    val notes: String? = null,
    val riskyExerciseCategories: String = "[]",
    val riskyExerciseNames: String = "[]",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
