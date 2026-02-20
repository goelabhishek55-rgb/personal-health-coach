package com.personalcoach.domain.model

data class GutCheckIn(
    val id: Int = 0,
    val dateTime: Long,
    val overallGutFeel: Int,
    val symptoms: List<GutSymptom> = emptyList(),
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class GutSymptom(
    val id: Int = 0,
    val checkInId: Int = 0,
    val symptomName: String,
    val severity: Int
)
