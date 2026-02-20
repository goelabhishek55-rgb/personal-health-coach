package com.personalcoach.domain.model

data class WaterIntake(
    val id: Int = 0,
    val date: Long,
    val totalMl: Int = 0,
    val targetMl: Int,
    val glassCount: Int = 0
)
