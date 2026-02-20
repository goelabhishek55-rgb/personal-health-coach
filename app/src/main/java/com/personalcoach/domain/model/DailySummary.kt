package com.personalcoach.domain.model

data class DailySummary(
    val id: Int = 0,
    val date: Long,
    val moodRating: Int? = null,
    val energyRating: Int? = null,
    val notes: String? = null,
    val stepsFromSamsungHealth: Int? = null,
    val sleepMinutesFromSamsungHealth: Int? = null
)
