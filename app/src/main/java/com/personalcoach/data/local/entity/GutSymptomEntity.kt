package com.personalcoach.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gut_symptom",
    foreignKeys = [
        ForeignKey(
            entity = GutCheckInEntity::class,
            parentColumns = ["id"],
            childColumns = ["checkInId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GutSymptomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(index = true) val checkInId: Int,
    val symptomName: String,
    val severity: Int
)
