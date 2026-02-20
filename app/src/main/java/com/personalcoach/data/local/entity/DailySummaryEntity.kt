package com.personalcoach.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_summary")
data class DailySummaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val moodRating: Int? = null,
    val energyRating: Int? = null,
    val notes: String? = null,
    val stepsFromSamsungHealth: Int? = null,
    val sleepMinutesFromSamsungHealth: Int? = null
)
