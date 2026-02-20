package com.personalcoach.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val totalMl: Int = 0,
    val targetMl: Int,
    val glassCount: Int = 0
)
