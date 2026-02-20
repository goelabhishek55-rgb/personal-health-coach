package com.personalcoach.domain.model

data class InjuryHealthCondition(
    val id: Int = 0,
    val name: String,
    val type: String,
    val affectedArea: String,
    val severity: Int,
    val notes: String? = null,
    val riskyExerciseCategories: List<String> = emptyList(),
    val riskyExerciseNames: List<String> = emptyList(),
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
