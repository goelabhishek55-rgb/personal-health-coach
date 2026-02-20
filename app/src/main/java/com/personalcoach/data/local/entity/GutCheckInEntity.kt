package com.personalcoach.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gut_check_in")
data class GutCheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: Long,
    val overallGutFeel: Int,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
