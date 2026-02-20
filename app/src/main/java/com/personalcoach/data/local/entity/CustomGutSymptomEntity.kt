package com.personalcoach.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_gut_symptom")
data class CustomGutSymptomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val symptomName: String,
    val isActive: Boolean = true
)
