package com.personalcoach.domain.model

data class WeightRecord(
    val id: Int = 0,
    val dateTime: Long,
    val weightKg: Float,
    val bodyFatPercent: Float? = null,
    val bmr: Float? = null,
    val source: String = "manual"
)
