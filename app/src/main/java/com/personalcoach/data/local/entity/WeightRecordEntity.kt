package com.personalcoach.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_record")
data class WeightRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: Long,
    val weightKg: Float,
    val bodyFatPercent: Float? = null,
    val bmr: Float? = null,
    val source: String = "manual"
)
